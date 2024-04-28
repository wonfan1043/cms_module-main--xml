package com.inext.manage_system.dto;

import java.util.List;

import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.Topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTopicRes {

    /** 主旨リスト **/
    private List<Topic> topicList;

    /** Common Message **/
    private CommonMessage message;
    
}
