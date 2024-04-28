package com.inext.manage_system.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;
import com.inext.manage_system.model.Topic;
import com.inext.manage_system.service.TopicService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@Controller
public class TopicServiceController {

    @Autowired
    private TopicService topicService;

     // 主旨作成
    @PostMapping("invoice/create_topic")    
    public BaseRes createTopic(@RequestBody Topic topic){
        return topicService.createTopic(topic);
    }

    // 主旨更新
    @PostMapping("invoice/update_topic")
    public BaseRes updateTopic(@RequestBody Topic topic){
        return topicService.updateTopic(topic);
    }

    // 主旨検索
    @PostMapping("invoice/search_topic")
    public List<SearchTopicRes> searchTopic(@RequestBody SearchTopicReq req){
        return topicService.searchTopic(req);
    }

    // 主旨詳細チェッ
    @GetMapping("invoce/check_topic")
    public TopicContentRes checkTopic(@RequestParam(name="topic_id", required = true) String topicId){
        return topicService.checkTopic(topicId);
    }

    // 主旨削除
    @PostMapping("invoice/delete_topic")
    public BaseRes deleteTopic(@RequestBody Topic topic){
        return topicService.deleteTopic(topic);
    }

}
