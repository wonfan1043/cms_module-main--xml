package com.inext.manage_system.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;

@Mapper
public interface TopicDao {

    public String searchTopicByTopicId(@Param("topicId") String topicId);

    public String searchTopicByTopicName(@Param("topicName") String topicName);

    public int createTopic(@Param("topicId") String topicId, @Param("topicName") String topicName, //
    @Param("comment") String comment, @Param("creater") String creater, //
    @Param("createDateTime") LocalDateTime createDateTime);

    public int updateTopic(@Param("topicId") String topicId, @Param("topicName") String topicName, //
    @Param("comment") String comment, @Param("updater") String updater, @Param("updateDateTime") LocalDateTime updateDateTime);

    public List<SearchTopicRes> searchAllTopics();

    public List<SearchTopicRes> searchTopicByReq(@Param("year") int year, @Param("month") int month, @Param("keyword") String keyword);

    public TopicContentRes checkTopic(@Param("topicId") String topicId);

    public int deleteTopic(@Param("topicId") String topicId, @Param("updater") String updater, @Param("updateDateTime") LocalDateTime updateDateTime);

}
