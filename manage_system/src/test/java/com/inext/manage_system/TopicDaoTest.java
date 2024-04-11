package com.inext.manage_system;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.inext.manage_system.dao.TopicDao;

@SpringBootTest
public class TopicDaoTest {

    @Autowired
    private TopicDao topicDao;

    @Test
    public void searchTopicByTopicIdTest(){
        String a = topicDao.searchTopicByTopicId("party");
        System.out.println(a);
    }

    @Test
    public void createTopicTest(){
        int a = topicDao.createTopic("topicId", "topicName", "comment", "AAA", LocalDateTime.now());
        System.out.println(a);
    }

    @Test
    public void updateTopicTest(){
        int a = topicDao.updateTopic("topicId", "topicName", "new comment", "BBB", LocalDateTime.now());
        System.out.println(a);
    }

    @Test
    public void deleteTopicTest(){
        int a = topicDao.deleteTopic("topicId", "CCC", LocalDateTime.now());
        System.out.println(a);
    }
}
