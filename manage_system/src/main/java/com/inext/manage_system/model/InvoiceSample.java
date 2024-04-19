package com.inext.manage_system.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSample {

    private int corpId;

    private String invoiceNo;
    
    private String creater;

    private LocalDateTime createDateTime;

    private String updater;

    private LocalDateTime updateDateTime;

    private boolean delete;

}
