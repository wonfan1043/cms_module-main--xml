package com.inext.manage_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inext.manage_system.dao.InvoiceDao;
import com.inext.manage_system.dao.InvoiceSampleDao;
import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateSampleReq;

import org.springframework.util.StringUtils;

@Service
public class InvoiceSampleServiceImpl implements InvoiceSampleService {

    @Autowired
    InvoiceSampleDao invoiceSampleDao;

    @Autowired
    InvoiceDao invoiceDao;

    // サンプル作成
    @Override
    public BaseRes createSample(CreateOrUpdateSampleReq req) {
        // 引数チェック
        BaseRes checkResult = checkParam(req);
        if (checkResult.getCode() != 200) {
            return new BaseRes(checkResult.getCode(), checkResult.getMessage());
        }
        
        // サンプルがすでに存在するか確認
        int sampleExist = invoiceSampleDao.searchInvoiceSampleByCorpId(req.getCorpId());
        if (sampleExist != 0) {
            return new BaseRes(CommonMessage.SAMPLE_ALREADY_EXISTS.getCode(), CommonMessage.SAMPLE_ALREADY_EXISTS.getMessage());
        }
        
        // invoiceテーブルに請求書が存在するか確認
        int invoiceExist = invoiceDao.searchInvoiceByInvoiceNo(req.getInvoiceNo());
        if(invoiceExist == 0){
            return new BaseRes(CommonMessage.CREATE_INVOICE_FIRST.getCode(), CommonMessage.CREATE_INVOICE_FIRST.getMessage());
        }

        // 作成  
        invoiceSampleDao.createSample(req.getCorpId(), req.getInvoiceNo(), req.getCreater(), req.getCreateDateTime());
        return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());

    }

    // サンプル更新
    @Override
    public BaseRes updateSample(CreateOrUpdateSampleReq req) {
        // 引数チェック
        BaseRes checkResult = checkParam(req);
        if(checkResult.getCode() != 200){
            return new BaseRes(checkResult.getCode(), checkResult.getMessage());
        }

        // サンプルが存在するか確認
        int sampleExist = invoiceSampleDao.searchInvoiceSampleByCorpId(req.getCorpId());
        if (sampleExist == 0) {
            return new BaseRes(CommonMessage.SAMPLE_NOT_FOUND.getCode(), CommonMessage.SAMPLE_NOT_FOUND.getMessage());
        }

        // invoiceテーブルに請求書が存在するか確認
        int invoiceExist = invoiceDao.searchInvoiceByInvoiceNo(req.getInvoiceNo());
        if(invoiceExist == 0){
            return new BaseRes(CommonMessage.CREATE_INVOICE_FIRST.getCode(), CommonMessage.CREATE_INVOICE_FIRST.getMessage());
        }

        // 更新 
        invoiceSampleDao.updateSample(req.getCorpId(), req.getInvoiceNo(), req.getUpdater(), req.getUpdateDateTime());
        return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
    }

        // サンプル削除
        @Override
        public BaseRes deleteSample(CreateOrUpdateSampleReq req) {
            if(req.getCorpId() == 0){
                return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
            }
            invoiceSampleDao.deleteSample(req.getCorpId(), req.getUpdater(), req.getUpdateDateTime());
            return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
        }


    // 引数チェック
    private BaseRes checkParam(CreateOrUpdateSampleReq req) {
        if (req.getCorpId() == 0 || !StringUtils.hasText(req.getInvoiceNo()) || !StringUtils.hasText(req.getCreater())
                || req.getCreateDateTime() == null) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        return new BaseRes(CommonMessage.PARAM_OKAY.getCode());
    }

}
