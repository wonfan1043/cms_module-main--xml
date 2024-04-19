package com.inext.manage_system.service;

import java.util.List;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateInvoiceReq;
import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceRes;

public interface InvoiceService {

    // 新規作成
    public BaseRes createInvoice(CreateOrUpdateInvoiceReq req);

    // 請求書更新
    public BaseRes updateInvoice(CreateOrUpdateInvoiceReq req);

    // 請求書検索
    public List<SearchInvoiceRes> searchInvoice(int type, int year, int month, int corpId);

    // 請求書詳細確認
    public InvoiceContentRes checkInvoice(String invoiceNo);

    //請求書削除
    public BaseRes deleteInvoice(CreateOrUpdateInvoiceReq req);

}
