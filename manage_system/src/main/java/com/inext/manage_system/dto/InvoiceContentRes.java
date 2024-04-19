package com.inext.manage_system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.inext.manage_system.enums.CommonMessage;

@Data
public class InvoiceContentRes {

    private int corpId;

    private String topicId;

    private int bankId;

    private String memo;

    private float tax;

    private String other;

    private LocalDate dueDate;

    private LocalDateTime createDateTime;

    private int itemId;

    private int quantity;

    private int unitPrice;

    private CommonMessage message;
    
    public InvoiceContentRes(CommonMessage message) {
    }

}
