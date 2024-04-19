package com.inext.manage_system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private String invoiceNo;

    private int corpId;

    private String topicId;

    private int bankId;

    private String memo;

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
