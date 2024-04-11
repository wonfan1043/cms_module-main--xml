package com.inext.manage_system.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    private String topId;

    private String topicName;

    private String comment;

    private String creater;

    private LocalDateTime createDateTime;

    private String updater;

    private LocalDateTime updateDateTime;

    public Topic(String topicId) {
    }

}
