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

    // @Select({"SELECT topic_id FROM topic WHERE topic_id = #{topicId}"})
    public String searchTopicByTopicId(@Param("topicId") String topicId);

    @Select({"SELECT topic_name FROM topic WHERE topic_name = #{topicName}"})
    public String searchTopicByTopicName(String topicName);

    @Insert({"INSERT INTO topic (topic_id, topic_name, comment, creater, create_datetime) VALUES (#{topicId}, #{topicName}, #{comment}, #{creater}, #{createDateTime})"})
    public int createTopic(String topicId, String topicName, String comment, String creater, LocalDateTime createDateTime);

    @Update({"UPDATE topic SET topic_name = #{topicName}, comment = #{comment}, updater = #{updater}, update_datetime = #{updateDateTime} WHERE topic_id = #{topicId} AND `delete` = 0"})
    public int updateTopic(String topicId, String topicName, String comment, String updater, LocalDateTime updateDateTime);

    @Select({"SELECT topic_id, topic_name, create_datetime FROM topic WHERE `delete` = 0"})
    public List<SearchTopicRes> searchAllTopics();

    @Select({"SELECT topic_id, topic_name, create_datetime FROM topic WHERE YEAR(create_datetime) = #{year} AND MONTH(create_datetime) = #{month} AND topic_name LIKE '%#{keyword}%' AND `delete` = 0"})
    public List<SearchTopicRes> searchTopicByReq(int year, int month, String keyword);

    @Select({"SELECT * FROM topic WHERE topic_id = #{topicId} AND `delete` = 0"})
    public TopicContentRes checkTopic(String topicId);

    @Update({"UPDATE topic SET updater = #{updater}, update_datetime = #{updateDateTime}, `delete` = 1 WHERE topic_id = #{topicId}"})
    public int deleteTopic(String topicId, String updater, LocalDateTime updateDateTime);

}
