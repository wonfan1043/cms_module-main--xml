package com.inext.manage_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inext.manage_system.dao.ChargeContentDao;
import com.inext.manage_system.dao.InvoiceDao;
import com.inext.manage_system.dao.InvoiceNoDao;
import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.ChargeContent;
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateInvoiceReq;
import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceRes;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    InvoiceNoDao invoiceNoDao;

    @Autowired
    ChargeContentDao chargeContentDao;

    // 新規作成
    @Override
    public BaseRes createInvoice(CreateOrUpdateInvoiceReq req) {
        // 引数チェック
        if (checkParam(req.getInvoiceNo(), req.getCorpId(), req.getTopicId(), req.getBankId(), req.getTax(), req.getCreater(), req.getCreateDateTime(), req.getProducts()) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 請求書がすでに存在するかを確認
        int exist = invoiceDao.searchInvoiceByInvoiceNo(req.getInvoiceNo());
        if (exist == 0) {
            String[] type = req.getInvoiceNo().split("");
            invoiceNoDao.addNumber(type[0]);
            invoiceDao.createInvoice(req.getInvoiceNo(), req.getCorpId(), req.getTopicId(), req.getBankId(),
                    req.getMemo(), req.getTax(), req.getOther(), req.getDueDate(), req.getChargeDate(),
                    req.getCreater(), req.getCreateDateTime());
            for (ChargeContent item : req.getProducts()) {
                chargeContentDao.addContent(item.getInvoiceNo(), item.getItemId(), item.getQuantity(),
                        item.getUnitPrice(), item.getCreater(), item.getCreateDateTime());
            }
            return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());
        } else {
            return new BaseRes(CommonMessage.INVOICE_ALREADY_EXISTS.getCode(), CommonMessage.INVOICE_ALREADY_EXISTS.getMessage());
        }
    }

    // 請求書更新
    @Override
    public BaseRes updateInvoice(CreateOrUpdateInvoiceReq req) {
        // 引数チェック
        if (checkParam(req.getInvoiceNo(), req.getCorpId(), req.getTopicId(), req.getBankId(), req.getTax(), req.getCreater(), req.getCreateDateTime(), req.getProducts()) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 請求書存在するかを確認
        int exist = invoiceDao.searchInvoiceByInvoiceNo(req.getInvoiceNo());
        if (exist == 1) {
            invoiceDao.updateInvoice(req.getInvoiceNo(), req.getCorpId(), req.getTopicId(), req.getBankId(),
                    req.getMemo(), req.getTax(), req.getOther(), req.getDueDate(), req.getChargeDate(),
                    req.getCreater(), req.getCreateDateTime());
            chargeContentDao.deleteAllContent(req.getInvoiceNo());
            for (ChargeContent item : req.getProducts()) {
                chargeContentDao.addContent(item.getInvoiceNo(), item.getItemId(), item.getQuantity(),
                        item.getUnitPrice(), item.getCreater(), item.getCreateDateTime());
            }
            return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
        } else {
            return new BaseRes(CommonMessage.INVOICE_NOT_FOUND.getCode(), CommonMessage.INVOICE_NOT_FOUND.getMessage());
        }
    }

    // 請求書検索
    @Override
    public List<SearchInvoiceRes> searchInvoice(int type, int year, int month, int corpId) {
        // 日付タイプ＋選択した年と月でデータ抽出
        List<SearchInvoiceRes> firstSearch = new ArrayList<>();
        switch (type) {
            case 0: // 作成日付
                if (checkTime(type, year, month) == false) {
                    firstSearch.add(new SearchInvoiceRes(CommonMessage.PARAM_ERROR));
                    return firstSearch;
                }
                firstSearch = invoiceDao.searchInvoiceByCreateDate(year, month);
                break;
            case 1: // 決済期限
                if (checkTime(type, year, month) == false) {
                    firstSearch.add(new SearchInvoiceRes(CommonMessage.PARAM_ERROR));
                    return firstSearch;
                }
                firstSearch = invoiceDao.searchInvoiceByDueDate(year, month);
                break;
            case 2: // 請求日付
                if (checkTime(type, year, month) == false) {
                    firstSearch.add(new SearchInvoiceRes(CommonMessage.PARAM_ERROR));
                    return firstSearch;
                }
                firstSearch = invoiceDao.searchInvoiceByChargeDate(year, month);
                break;
            case 3: // 決済日付
                if (checkTime(type, year, month) == false) {
                    firstSearch.add(new SearchInvoiceRes(CommonMessage.PARAM_ERROR));
                    return firstSearch;
                }
                firstSearch = invoiceDao.searchInvoiceByPayDate(year, month);
                break;
        }

        // corpIdあるかどうかによってフィルター
        if (corpId == 0) {
            return firstSearch;
        }
        List<SearchInvoiceRes> finalSearch = new ArrayList<>();
        for (SearchInvoiceRes item : firstSearch) {
            if (item.getCorpId() == corpId) {
                finalSearch.add(item);
            }
        }
        return finalSearch;
    }

    // 請求書詳細確認
    @Override
    public InvoiceContentRes checkInvoice(String invoiceNo) {
        InvoiceContentRes result = invoiceDao.checkInvoice(invoiceNo);
        if (result == null) {
            return new InvoiceContentRes(CommonMessage.INVOICE_NOT_FOUND);
        }
        return result;
    }

    // 請求書削除
    @Override
    public BaseRes deleteInvoice(CreateOrUpdateInvoiceReq req) {
        int exist = invoiceDao.searchInvoiceByInvoiceNo(req.getInvoiceNo());
        if (exist == 1) {
            invoiceDao.deleteInvoice(req.getInvoiceNo(), req.getUpdater(), req.getUpdateDateTime());
            chargeContentDao.deleteContent(req.getInvoiceNo(), req.getUpdater(), req.getUpdateDateTime());
            return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
        }
        return new BaseRes(CommonMessage.INVOICE_NOT_FOUND.getCode(), CommonMessage.INVOICE_NOT_FOUND.getMessage());
    }



    // 引数チェック
    private boolean checkParam(String invoiceNo, int corpId, String topicId, int bankId, float tax, String creater, LocalDateTime createDateTime, List<ChargeContent> products) {
        if (!StringUtils.hasText(invoiceNo) || corpId == 0 || !StringUtils.hasText(topicId)
                || bankId == 0 || tax == 0 || !StringUtils.hasText(creater)
                || createDateTime == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(products)) {
            return false;
        }
        for (ChargeContent item : products) {
            if (item.getItemId() == 0 || item.getQuantity() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTime(int type, int year, int month) {
        switch (type) {
            case 1: // 決済期限
                if (year == 0 || month == 0) {
                    return false;
                }
            case 0, 2, 3: // 作成日付、請求日付、決済日付
                int yearNow = LocalDate.now().getYear();
                int monthNow = LocalDate.now().getMonthValue();
                if (yearNow < year || monthNow < month) {
                    return false;
                }
                if (year == 0 || month == 0) {
                    return false;
                }
        }
        return true;
    }

}
