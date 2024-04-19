package com.inext.manage_system.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import com.inext.manage_system.service.InvoiceNoService;

public class InvoiceNoController {

    @Autowired
    private InvoiceNoService invoiceNoService;

    //新しい請求書番号を作成
    @PostMapping(value = "invoice/create_invoice_no")
    public int createNo(){
        return invoiceNoService.createNo();
    }

}
