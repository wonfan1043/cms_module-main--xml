package com.inext.manage_system.dto;

import com.inext.manage_system.enums.RtnCode;
import com.inext.manage_system.model.Topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicContentRes {

    private Topic topic;

    private RtnCode rtnCode;

    public TopicContentRes(RtnCode rtnCode) {
        this.rtnCode = rtnCode;
    }

}
