package com.inext.manage_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.inext.manage_system.model.ChargeContent;

import lombok.Data;

@Data
public class CreateOrUpdateInvoiceReq {
    
    private String invoiceNo;

    private int corpId;

    private String topicId;

    private int bankId;

    private String memo;

    private List<ChargeContent> products;

    private float tax;

    private String other;

    private LocalDate dueDate;

    private LocalDate chargeDate;

    private LocalDate payDate;

    private String creater;
    
    private LocalDateTime createDateTime;

    private String updater;

    private LocalDateTime updateDateTime;

    private boolean delete;

}
