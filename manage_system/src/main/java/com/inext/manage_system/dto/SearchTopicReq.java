package com.inext.manage_system.dto;

import lombok.Data;

@Data
public class SearchTopicReq {

    private int year;

    private int month;

    private String keyword;

}
