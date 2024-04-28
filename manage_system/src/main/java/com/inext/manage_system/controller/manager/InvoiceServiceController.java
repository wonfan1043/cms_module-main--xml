package com.inext.manage_system.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateInvoiceReq;
import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceReq;
import com.inext.manage_system.dto.SearchInvoiceRes;
import com.inext.manage_system.model.Invoice;
import com.inext.manage_system.service.InvoiceService;

@CrossOrigin
@Controller
public class InvoiceServiceController {

    @Autowired
    private InvoiceService invoiceService;

    // 採番取得
    @GetMapping("invoice/get_new_number")
    public int getNewNumber(){
        return invoiceService.getNewNumber();
    }

    // 新規作成
    @PostMapping("invoice/create_invoice")
    public BaseRes createInvoice(@RequestBody CreateOrUpdateInvoiceReq req){
        return invoiceService.createInvoice(req);
    }

    // 請求書更新
    @PostMapping("invoice/update_invoice")
    public BaseRes updateInvoice(@RequestBody CreateOrUpdateInvoiceReq req){
        return invoiceService.updateInvoice(req);
    }

    // 請求書検索
    @PostMapping("invoice/search_invoice")
    public List<SearchInvoiceRes> searchInvoice(@RequestBody SearchInvoiceReq req){
        return invoiceService.searchInvoice(req);
    }

    // 請求書詳細確認
    @GetMapping("invoice/check_invoice")
    public InvoiceContentRes checkInvoice(@RequestParam(name = "invoice_no", required = true) String invoiceNo){
        return invoiceService.checkInvoice(invoiceNo);
    }

    // 請求書削除
    @PostMapping("invoice/delete_invoice")
    public BaseRes deleteInvoice(@RequestBody Invoice invoice){
        return invoiceService.deleteInvoice(invoice);
    }

}
