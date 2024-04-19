package com.inext.manage_system.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateTopicReq;
import com.inext.manage_system.dto.DeleteTopicReq;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;
import com.inext.manage_system.dto.UpdateTopicReq;
import com.inext.manage_system.service.TopicService;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin
@RestController
public class TopicController {

    @Autowired
    private TopicService topicService;

    // 主旨作成
    @PostMapping(value = "topic/create_topic")
    public BaseRes createTopic(@RequestBody CreateTopicReq req){
        return topicService.createTopic(req);
    }

    // 主旨更新
    @PostMapping(value = "topic/update_topic")
    public BaseRes updateTopic(@RequestBody UpdateTopicReq req){
        return topicService.updateTopic(req);
    }

    // 主旨検索
    @PostMapping(value = "topic/search_topic")
    public List<SearchTopicRes> searchTopic(@RequestBody SearchTopicReq req){
        return topicService.searchTopic(req);
    }

    // 主旨チェック
    @PostMapping(value = "topic/check_topic")
    public TopicContentRes checkTopic(@RequestParam(value = "topic_id")String topicId){
        return topicService.checkTopic(topicId);
    }

    // 主旨削除
    @PostMapping(value = "topic/delete_topic")
    public BaseRes deleteTopic(@RequestBody DeleteTopicReq req){
        return topicService.deleteTopic(req);
    };

}
