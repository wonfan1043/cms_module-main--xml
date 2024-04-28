package com.inext.manage_system.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inext.manage_system.dao.InvoiceSampleDao;
import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.InvoiceSample;
import com.inext.manage_system.dto.BaseRes;

import org.springframework.util.StringUtils;

@Service
public class InvoiceSampleServiceImpl implements InvoiceSampleService {

    @Autowired
    InvoiceSampleDao invoiceSampleDao;

    // サンプル作成
    @Override
    public BaseRes createSample(InvoiceSample sample) {
        // パラメータチェック
        if (checkParam(sample, true) ==  false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(),CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if(checkByte(sample, true) == false){
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(),CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // DBにサンプルがすでにあるかチェック
        if(checkExist(sample, true) == false){
            return new BaseRes(CommonMessage.SAMPLE_ALREADY_EXISTS.getCode(),CommonMessage.SAMPLE_ALREADY_EXISTS.getMessage());
        }
        // InvoiceSampleDaoの「サンプル追加」メソッドでサンプル追加
        sample.setCreateDatetime(LocalDateTime.now()); // 作成時間を現時点にします
        invoiceSampleDao.insertInvoiceSample(sample);
        return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());

    }

    // サンプル更新
    @Override
    public BaseRes updateSample(InvoiceSample sample) {
        // パラメータチェック
        if (checkParam(sample, false) ==  false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(),CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if(checkByte(sample, false) == false){
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(),CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // DBにサンプルがあるかチェック
        if(checkExist(sample, false)){
            return new BaseRes(CommonMessage.SAMPLE_NOT_FOUND.getCode(),CommonMessage.SAMPLE_NOT_FOUND.getMessage());
        }
        // InvoiceSampleDaoの「サンプル編集」メソッドでサンプル編集
        sample.setUpdateDatetime(LocalDateTime.now()); // 編集時間を現時点にします
        invoiceSampleDao.updateInvoiceSampleByCorpId(sample);
        return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
    }

        // サンプル削除
        @Override
        public BaseRes deleteSample(InvoiceSample sample) {
            // 会社IDと編集者が入力されたかチェック
            if (sample.getCorpId() <= 0 || !StringUtils.hasText(sample.getUpdater())) {
                return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
            }
            // 編集者の桁数チェック
            if (sample.getUpdater().length() > 20) {
                return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
            }
            // DBにサンプルがあるかチェック
            if (checkExist(sample, false) == false) {
                return new BaseRes(CommonMessage.INVOICE_NOT_FOUND.getCode(), CommonMessage.INVOICE_NOT_FOUND.getMessage());
            }
            // InvoiceSampleDaoの「サンプル状態編集」メソッドで削除フラグを1に編集
            sample.setUpdateDatetime(LocalDateTime.now()); // 編集時間を現時点にします
            invoiceSampleDao.updateInvoiceSampleStatusByCorpId(sample);
            return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
        }


    // パラメータチェック
    private boolean checkParam(InvoiceSample sample, boolean isCreate) {
        // 必須項目が入力されているか確認します
        if (sample.getCorpId() <= 0 || !StringUtils.hasText(sample.getTopicId()) || sample.getBankId() <= 0 || sample.getTax() <= 0) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者が入力されたか確認します
            if(!StringUtils.hasText(sample.getCreater())){
                return false;
            }
        } else {
            // 編集の場合は編集者が入力されたか確認します
            if (!StringUtils.hasText(sample.getUpdater())) {
                return false;
            }
        }
        return true;
    }

    // 桁数チェック
    private boolean checkByte(InvoiceSample sample, boolean isCreate) {
        // 主旨IDが桁数以内であることを確認します
        if (sample.getTopicId().length() > 20) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者の桁数を確認します
            if(sample.getCreater().length() > 20){
                return false;
            }
        } else {
            // 編集の場合は編集者の桁数を確認します
            if (sample.getUpdater().length() > 20) {
                return false;
            }
        }
        return true;
    }

    // DBにサンプルがあるかチェック
    private boolean checkExist(InvoiceSample sample, boolean isCreate) {
        InvoiceSample data = invoiceSampleDao.selectInvoiceSampleByCorpId(sample.getCorpId());
        if (isCreate) {
            // 新規の場合、ヌルのはず
            if (data != null) {
                return false;
            }
        } else {
            // 更新と削除の場合、ヌではないはず
            if (data == null) {
                return false;
            }
        }
        return true;
    }

}
