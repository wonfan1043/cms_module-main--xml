package com.inext.manage_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTopicReq {

    private String topicId;

    private String topicName;

    private String updater;

}
