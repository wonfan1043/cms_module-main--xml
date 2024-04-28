package com.inext.manage_system.dto;

import java.util.List;

import com.inext.manage_system.model.ChargeContent;
import com.inext.manage_system.model.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateInvoiceReq {

    private Invoice invoice;
    
    private List<ChargeContent> products;

}
