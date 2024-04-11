package com.inext.manage_system.dto;

import java.time.LocalDateTime;

import com.inext.manage_system.enums.RtnCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTopicRes {

    private LocalDateTime createDateTime;

    private String topicName;
    
    private String topicId;

    private RtnCode rtnCode;

    public SearchTopicRes(RtnCode rtnCode) {
        this.rtnCode = rtnCode;
    }

}
