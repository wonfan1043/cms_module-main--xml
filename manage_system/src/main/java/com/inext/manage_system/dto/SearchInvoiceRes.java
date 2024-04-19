package com.inext.manage_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.inext.manage_system.enums.CommonMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchInvoiceRes {

    private String invoiceNo;

    private int corpId;

    private LocalDate chargeDate;

    private LocalDateTime createDateTime;

    private CommonMessage message;

    public SearchInvoiceRes(CommonMessage message) {
    }

}
