package com.inext.manage_system.dto;

import java.util.List;

import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchInvoiceRes {

    private List<Invoice> invoiceList;

    private CommonMessage message;

    public SearchInvoiceRes(CommonMessage message) {
    }

}
