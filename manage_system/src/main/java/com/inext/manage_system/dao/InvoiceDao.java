package com.inext.manage_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceReq;
import com.inext.manage_system.model.Invoice;

@Mapper
public interface InvoiceDao {

    // 請求書番号取得 by invoiceNo
    public String selectInvoiceNoByInvoiceNo(@Param("invoiceNo") String invoiceNo);

    // 請求書追加
    public void insertInvoice(@Param("invoice") Invoice invoice);

    // 請求書内容更新
    public void updateInvoice(@Param("invoice") Invoice invoice);

    // 請求書取得 by dateType, corpId
    public List<Invoice> selectInvoiceByDateTypeCorpId(@Param("req") SearchInvoiceReq req);

    // 請求書詳細取得
    public InvoiceContentRes selectInvoiceAndChargeContent(@Param("invoiceNo") String invoiceNo);
    
    // 請求書状態編集
    public void updateInvoiceStatus(@Param("invoice") Invoice invoice);
}

