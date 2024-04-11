package com.inext.manage_system.service;

import java.util.List;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateTopicReq;
import com.inext.manage_system.dto.DeleteTopicReq;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;
import com.inext.manage_system.dto.UpdateTopicReq;

public interface TopicService {

     // 主旨作成
    public BaseRes createTopic(CreateTopicReq req);

    // 主旨更新
    public BaseRes updateTopic(UpdateTopicReq req);

    // 主旨検索
    public List<SearchTopicRes> searchTopic(SearchTopicReq req);

    // 主旨チェック
    public TopicContentRes checkTopic(String topicId);

    //主旨削除
    public BaseRes deleteTopic(DeleteTopicReq req);

}
