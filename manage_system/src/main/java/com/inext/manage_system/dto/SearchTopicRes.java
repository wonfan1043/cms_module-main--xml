package com.inext.manage_system.dto;

import java.time.LocalDateTime;

import com.inext.manage_system.enums.CommonMessage;

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

    private CommonMessage message;

    public SearchTopicRes(CommonMessage message) {
        this.message = message;
    }

}
