package com.inext.manage_system.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateOrUpdateSampleReq {

    private String invoiceNo;

    private int corpId;

    private String creater;
    
    private LocalDateTime createDateTime;

    private String updater;

    private LocalDateTime updateDateTime;

    private boolean delete;

}