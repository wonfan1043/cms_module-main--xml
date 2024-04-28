package com.inext.manage_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.inext.manage_system.dao.TopicDao;
import com.inext.manage_system.enums.CommonMessage;
import com.inext.manage_system.model.Topic;
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    // 主旨作成
    @Override
    public BaseRes createTopic(Topic topic) {
        // パラメータチェック
        if (checkParam(topic, true) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if (checkByte(topic, true) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // DBに同じ主旨がないことを確認します
        if (checkExist(topic, true) == false) {
            return new BaseRes(CommonMessage.TOPIC_ALREADY_EXISTS.getCode(), CommonMessage.TOPIC_ALREADY_EXISTS.getMessage());
        }
        // 作成時間を現時点にします
        topic.setCreateDateTime(LocalDateTime.now());
        // TopicDaoの「主旨追加」メソッドで主旨追加します
        topicDao.insertTopic(topic);
        return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());
    }

    // 主旨編集
    @Override
    public BaseRes updateTopic(Topic topic) {
        // パラメータチェック
        if (checkParam(topic, false) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // 桁数チェック
        if (checkByte(topic, false) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        // DBに主旨があることを確認します
        if (checkExist(topic, false) == false) {
            return new BaseRes(CommonMessage.TOPIC_NOT_FOUND.getCode(), CommonMessage.TOPIC_NOT_FOUND.getMessage());
        }
        // 編集時間を現時点にします
        topic.setUpdateDateTime(LocalDateTime.now());
        // TopicDaoの「主旨編集 by topicId」メソッドで主旨編集します
        topicDao.updateTopicByTopicId(topic);
        return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
    }

    // 主旨検索
    @Override
    public List<SearchTopicRes> searchTopic(SearchTopicReq req) {
        // 結果を返すためにsearchResultを宣言しておきます
        List<SearchTopicRes> searchResult = new ArrayList<>();
        // 時間帯チェック
        // (1)入力された年と月、及び1日でtimeRangeを宣言します
        LocalDate timeRange = LocalDate.of(req.getYear(), req.getMonth(), 1);
        // (2)timeRangeが現時点より遅い場合はエラーメッセージを返します
        if(timeRange.isAfter(LocalDate.now())){
            searchResult.add(new SearchTopicRes(null, CommonMessage.INVALID_TIME_RANGE));
            return searchResult;
        }
        // キーワードがなければ空文字列にします
        if(!StringUtils.hasText(req.getKeyword())){
            req.setKeyword("");
        }
        // TopicDaoの「主旨取得 by topicName, createDatetime」メソッドで主旨リスト取得します
        List<Topic> topicList = topicDao.selectTopicByTopicNameCreateDatetime(req);
        if (topicList.isEmpty()) {
            // topicListがヌルの場合はエラーメッセージを追加します
            searchResult.add(new SearchTopicRes(topicList, CommonMessage.TOPIC_NOT_FOUND));
        } else {
            // topicListがヌルではない場合は、取得したリストと成功メッセージを追加します
            searchResult.add(new SearchTopicRes(topicList, CommonMessage.SUCCESS));
        }
        return searchResult;
    }

    // 主旨詳細チェック
    @Override
    public TopicContentRes checkTopic(String topicId) {
        // topicIdが入力されたか確認します
        if (!StringUtils.hasText(topicId)) {
            return new TopicContentRes(null, CommonMessage.PARAM_ERROR);
        }
        // TopicDaoの「主旨取得 by topicId」メソッドで主旨詳細を取得します
        Topic topic = topicDao.selectTopicByTopicId(topicId);
        if (topic == null) {
            // topicがヌルの場合はエラーメッセージを返します
            return new TopicContentRes(topic, CommonMessage.TOPIC_NOT_FOUND);
        }
        // topicがヌルではない場合は、取得したデータと成功メッセージを返します
        return new TopicContentRes(topic, CommonMessage.SUCCESS);
    }

    // 主旨削除
    @Override
    public BaseRes deleteTopic(Topic topic) {
        // topicIdと編集者が入力されたか確認します
        if (!StringUtils.hasText(topic.getTopicId()) || !StringUtils.hasText(topic.getUpdater())) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        // DBに主旨があることを確認します
        if (checkExist(topic, false) == false) {
            return new BaseRes(CommonMessage.TOPIC_NOT_FOUND.getCode(), CommonMessage.TOPIC_NOT_FOUND.getMessage());
        }
        // TopicDaoの「主旨状態編集」メソッドで主旨状態編集
        topicDao.updateTopiceStatusByTopicId(topic);
        return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
    }



    // パラメータチェック
    private boolean checkParam(Topic topic, boolean isCreate) {
        // 必須項目が入力されているか確認します
        if (!StringUtils.hasText(topic.getTopicId()) || !StringUtils.hasText(topic.getTopicName())) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者が入力されたか確認します
            if(!StringUtils.hasText(topic.getCreater())){
                return false;
            }
        } else {
            // 編集の場合は編集者が入力されたか確認します
            if (!StringUtils.hasText(topic.getUpdater())) {
                return false;
            }
        }
        return true;
    }

    // 桁数チェック
    private boolean checkByte(Topic topic, boolean isCreate) {
        //各パラメータが桁数以内であることを確認します
        if (topic.getTopicId().length() > 20 || topic.getTopicName().length() > 20 || topic.getComment().length() > 50) {
            return false;
        }
        // 作成や編集によって作成者と編集者をチェック
        if (isCreate) {
            // 作成の場合は作成者の桁数を確認します
            if(topic.getCreater().length() > 20){
                return false;
            }
        } else {
            // 編集の場合は編集者の桁数を確認します
            if (topic.getUpdater().length() > 20) {
                return false;
            }
        }
        return true;
    }

    // DBに主旨があるか確認
    private boolean checkExist(Topic topic, boolean isCreate) {
        // TopicDaoの「主旨取得 by topicId」メソッドで主旨詳細を取得します
        Topic selectedTopic = topicDao.selectTopicByTopicId(topic.getTopicId());
        // TopicDaoの「主旨取得 by topicName」メソッドで主旨詳細を取得して、長さを確認
        int topicNameLength = topicDao.selectTopicByTopicName(topic.getTopicName()).length();
        // 作成かどうかによって違う確認を行う
        if (isCreate) {
            // 新規の場合、topicはヌル、topicNameLengthは0
            if (selectedTopic != null || topicNameLength != 0) {
                return false;
            }
        } else {
            // 編集と削除の場合、topicはヌルではない、topicNameLengthは0ではない
            if (selectedTopic == null || topicNameLength == 0) {
                return false;
            }
        }
        return true;
    }
}
