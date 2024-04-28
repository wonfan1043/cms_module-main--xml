package com.inext.manage_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inext.manage_system.dao.ChargeContentDao;
import com.inext.manage_system.dao.InvoiceDao;
import com.inext.manage_system.dao.InvoiceNoDao;
import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.ChargeContent;
import com.inext.manage_system.model.Invoice;
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateInvoiceReq;
import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceReq;
import com.inext.manage_system.dto.SearchInvoiceRes;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    InvoiceNoDao invoiceNoDao;

    @Autowired
    ChargeContentDao chargeContentDao;

    // 採番取得
    @Override
    public int getNewNumber(){
        // invoiceNoDaoのselectInvoiceNoByOrderで最後の番号を取得します
        int lastNum = invoiceNoDao.selectInvoiceNoByOrder();
        // 新番号を作成
        int newNum = lastNum + 1;
        // 新番号を返します
        return newNum;
    }

    // 新規作成
    @Override
    public BaseRes createInvoice(CreateOrUpdateInvoiceReq req) {
        // パラメータチェック
        if (checkParam(req, true) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if (checkByte(req, true) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // 請求書番号フォーマットチェック
        String pattern = "^[A-Z]{1}\\d{8}$";
        if(!req.getInvoice().getInvoiceNo().matches(pattern)){
            return new BaseRes(CommonMessage.INVALID_INVOICE_NO.getCode(), CommonMessage.INVALID_INVOICE_NO.getMessage());
        }
        // DBに請求書があるかチェック
        if(checkExist(req.getInvoice().getInvoiceNo(), true) == false){
            return new BaseRes(CommonMessage.INVOICE_ALREADY_EXISTS.getCode(), CommonMessage.INVOICE_ALREADY_EXISTS.getMessage());
        }
        // 請求書追加
        // (1)請求書番号の英語大文字1桁を取得
        String[] invoiceNoArr = req.getInvoice().getInvoiceNo().split("");
        // (2)InvoiceNoDaoの「新番号追加」メソッドで新番号を追加
        invoiceNoDao.insertInvoiceNo(invoiceNoArr[0]);
        // (3)請求書を保存 
        req.getInvoice().setCreateDateTime(LocalDateTime.now()); //作成時間を現時点にします
        invoiceDao.insertInvoice(req.getInvoice());
        // (4)請求内容を保存
        chargeContentDao.insertChargeContent(req.getProducts());
        return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());
    }

    // 請求書編集
    @Override
    public BaseRes updateInvoice(CreateOrUpdateInvoiceReq req) {
        // パラメータチェック
        if (checkParam(req, false) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if (checkByte(req, false) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // DBに請求書があるかチェック
        if(checkExist(req.getInvoice().getInvoiceNo(), false) == false){
            return new BaseRes(CommonMessage.INVOICE_NOT_FOUND.getCode(), CommonMessage.INVOICE_NOT_FOUND.getMessage());
        }
        // 編集を行います
        // (1)請求書を編集
        req.getInvoice().setUpdateDateTime(LocalDateTime.now()); //編集時間を現時点にします
        invoiceDao.updateInvoice(req.getInvoice());
        // (2)請求内容を編集
        // (2-1)ChargeContentDaoの「請求内容取得 by invoiceNo」メソッドでDBにデータがすでにあるか確認します。あれば物理削除します
        int chargeContentNum = chargeContentDao.selectChargeContentByInvoiceNo(req.getInvoice().getInvoiceNo());
        if(chargeContentNum > 0){
            chargeContentDao.deleteChargeContentByInvoiceNo(req.getInvoice().getInvoiceNo());
        }
        // (2-2)ChargeContentDaoの「請求内容追加」メソッドで新しい請求内容を保存
        chargeContentDao.insertChargeContent(req.getProducts());
        return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
    }

    // 請求書検索
    @Override
    public List<SearchInvoiceRes> searchInvoice(SearchInvoiceReq req) {
        // 結果を返すためにsearchResultを宣言しておきます
        List<SearchInvoiceRes> searchResult = new ArrayList<>();
        // パラメータチェック
        if(req.getDateType() == null || req.getDateType().getCode() < 1 || req.getDateType().getCode() > 4 || req.getCorpId() <= 0){
            searchResult.add(new SearchInvoiceRes(CommonMessage.PARAM_ERROR));
            return searchResult;
        }
        // 時間帯チェック
        // (1)入力された年と月、及び1日でtimeRangeを宣言します
        LocalDate timeRange = LocalDate.of(req.getYear(), req.getMonth(), 1);
        // (2)timeRangeが現時点より遅い場合はエラーメッセージを返します
        if(timeRange.isAfter(LocalDate.now())){
            searchResult.add(new SearchInvoiceRes(CommonMessage.INVALID_TIME_RANGE));
            return searchResult;
        }
        // InvoiceDaoの「請求書取得 by dateType, corpId」メソッドで請求書を取得します
        List<Invoice> invoiceList = invoiceDao.selectInvoiceByDateTypeCorpId(req);
        if (invoiceList.isEmpty()) {
            // invoiceListがヌルの場合はエラーメッセージを追加します
            searchResult.add(new SearchInvoiceRes(invoiceList, CommonMessage.INVOICE_NOT_FOUND));
        } else {
            // invoiceListがヌルではない場合は、取得したリストと成功メッセージを追加します
            searchResult.add(new SearchInvoiceRes(invoiceList, CommonMessage.SUCCESS));
        }
        return searchResult;
    }

    // 請求書詳細確認
    @SuppressWarnings("null")
    @Override
    public InvoiceContentRes checkInvoice(String invoiceNo) {
        // invoiceNoが入力されたか確認します
        if (!StringUtils.hasText(invoiceNo)) {
            return new InvoiceContentRes(CommonMessage.PARAM_ERROR);
        }
        // フォーマットが正しいであるか確認します
        String pattern = "^[A-Z]{1}\\d{8}$";
        if (!invoiceNo.matches(pattern)) {
            return new InvoiceContentRes(CommonMessage.INVALID_INVOICE_NO);
        }
        // InvoiceDaoの「請求書詳細取得」メソッドで請求書内容と請求内容を取得します
        InvoiceContentRes result = invoiceDao.selectInvoiceAndChargeContent(invoiceNo);
        if (result == null) {
            result.setMessage(CommonMessage.INVOICE_NOT_FOUND);;
            return result;
        }
        result.setMessage(CommonMessage.SUCCESS);
        return result;
    }

    // 請求書削除
    @Override
    public BaseRes deleteInvoice(Invoice invoice) {
        // 請求書番号と編集者が入力されたか確認します
        if (!StringUtils.hasText(invoice.getInvoiceNo()) || !StringUtils.hasText(invoice.getUpdater())) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // フォーマットが正しいであるか確認します
        String pattern = "^[A-Z]{1}\\d{8}$";
        if (!invoice.getInvoiceNo().matches(pattern)) {
            return new BaseRes(CommonMessage.INVALID_INVOICE_NO.getCode(), CommonMessage.INVALID_INVOICE_NO.getMessage());
        }
        // DBに請求書があるかチェック
        if (checkExist(invoice.getInvoiceNo(), false) == false) {
            return new BaseRes(CommonMessage.INVOICE_NOT_FOUND.getCode(), CommonMessage.INVOICE_NOT_FOUND.getMessage());
        }
        // InvoiceDaoの「請求書状態編集」メソッドで削除フラグを1に編集します
        invoiceDao.updateInvoiceStatus(invoice);
        // ChargeContentDaoの「請求内容取得 by invoiceNo」メソッドでDBに請求内容があるか確認します
        int chargeContentNum = chargeContentDao.selectChargeContentByInvoiceNo(invoice.getInvoiceNo());
        // DBに請求内容があれば、ChargeContentDaoの「請求内容状態編集」メソッドで削除フラグを1に編集します
        if (chargeContentNum > 0) {
            ChargeContent contentToDelete = new ChargeContent();
            contentToDelete.setInvoiceNo(invoice.getInvoiceNo()); // 請求書番号を設定します
            contentToDelete.setUpdater(invoice.getUpdater()); // 編集者を設定します
            contentToDelete.setUpdateDateTime(LocalDateTime.now()); // 編集時間を現時点にします
            chargeContentDao.updateChargeContentStatusByInvoiceNo(contentToDelete); // 状態編集
        }
        return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
    }



    // パラメータチェック
    private boolean checkParam(CreateOrUpdateInvoiceReq req, boolean isCreate) {
        // 必須項目が入力されているか確認します
        if (!StringUtils.hasText(req.getInvoice().getInvoiceNo()) || req.getInvoice().getCorpId() <= 0
            || !StringUtils.hasText(req.getInvoice().getTopicId())|| req.getInvoice().getBankId() <= 0 || req.getInvoice().getTax() <= 0
            || CollectionUtils.isEmpty(req.getProducts())) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者が入力されたか確認します
            if(!StringUtils.hasText(req.getInvoice().getCreater())){
                return false;
            }
        } else {
            // 編集の場合は編集者が入力されたか確認します
            if (!StringUtils.hasText(req.getInvoice().getUpdater())) {
                return false;
            }
        }
        // 請求内容の商品番号と数量が入力されているか確認します
        for (ChargeContent item : req.getProducts()) {
            if (item.getItemId() <= 0 || item.getQuantity() <= 0) {
                return false;
            }
        }
        return true;
    }

    // 桁数チェック
    private boolean checkByte(CreateOrUpdateInvoiceReq req, boolean isCreate) {
        // 各パラメータが桁数以内であるか確認します
        if (req.getInvoice().getInvoiceNo().length() != 9 || req.getInvoice().getTopicId().length() > 20
            || req.getInvoice().getMemo().length() > 50) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者の桁数を確認します
            if(req.getInvoice().getCreater().length() > 20){
                return false;
            }
        } else {
            // 編集の場合は編集者の桁数を確認します
            if (req.getInvoice().getUpdater().length() > 20) {
                return false;
            }
        }
        return true;
    }

    // DBに請求書があるかチェック
    private boolean checkExist(String invoiceNo, boolean isCreate) {
        // InvoiceDaoの「請求書番号取得 by invoiceNo」メソッドで番号を取得して、長さを取得します
        int length = invoiceDao.selectInvoiceNoByInvoiceNo(invoiceNo).length();
        if (isCreate) {
            // 新規の場合、lengthは0のはず
            if (length != 0) {
                return false;
            }
        } else {
            // 編集と削除の場合、lengthは全部0ではないはず
            if (length == 0) {
                return false;
            }
        }
        return true;
    }

}
