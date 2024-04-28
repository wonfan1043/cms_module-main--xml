package com.inext.manage_system.dto;

import com.inext.manage_system.enums.DateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchInvoiceReq {

    private DateType dateType;

    private int year;

    private int month;

    private int corpId;

}
