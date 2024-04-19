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
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateTopicReq;
import com.inext.manage_system.dto.DeleteTopicReq;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;
import com.inext.manage_system.dto.UpdateTopicReq;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    // 主旨作成
    @Override
    public BaseRes createTopic(CreateTopicReq req) {
        // 引数チェック＋データバイトチェック＋存在チェック
        if (checkParam(req.getTopicId(), req.getTopicName(), req.getCreater()) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        if (checkByte(req.getTopicId(), req.getTopicName(), req.getComment(), req.getCreater()) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        if (checkExist(req.getTopicId(), req.getTopicName(), true) == false) {
            return new BaseRes(CommonMessage.TOPIC_ALREADY_EXISTS.getCode(), CommonMessage.TOPIC_ALREADY_EXISTS.getMessage());
        }

        // 作成
        int result = topicDao.createTopic(req.getTopicId(), req.getTopicName(), req.getComment(), req.getCreater(), LocalDateTime.now());
        if(result != 1){
            return new BaseRes(CommonMessage.ERROR.getCode(), CommonMessage.ERROR.getMessage());
        }
        return new BaseRes(CommonMessage.CREATE_SUCCESS.getCode(), CommonMessage.CREATE_SUCCESS.getMessage());
    }

    // 主旨更新
    @Override
    public BaseRes updateTopic(UpdateTopicReq req) {
        // 引数チェック＋データバイトチェック＋存在チェック
        if (checkParam(req.getTopicId(), req.getTopicName(), req.getUpdater()) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        if (checkByte(req.getTopicId(), req.getTopicName(), req.getComment(), req.getUpdater()) == false) {
            return new BaseRes(CommonMessage.DATA_TOO_BIG.getCode(), CommonMessage.DATA_TOO_BIG.getMessage());
        }
        if (checkExist(req.getTopicId(), req.getTopicName(), false) == false) {
            return new BaseRes(CommonMessage.TOPIC_ALREADY_EXISTS.getCode(), CommonMessage.TOPIC_ALREADY_EXISTS.getMessage());
        }

        // 更新
        int result = topicDao.updateTopic(req.getTopicId(), req.getTopicName(), req.getComment(), req.getUpdater(), LocalDateTime.now());
        if(result != 1){
            return new BaseRes(CommonMessage.ERROR.getCode(), CommonMessage.ERROR.getMessage());
        }
        return new BaseRes(CommonMessage.UPDATE_SUCCESS.getCode(), CommonMessage.UPDATE_SUCCESS.getMessage());
    }

    // 主旨検索
    @Override
    public List<SearchTopicRes> searchTopic(SearchTopicReq req) {
        // 時間帯チェック
        if(req.getYear() > LocalDate.now().getYear() || req.getMonth() > LocalDate.now().getMonthValue() || req.getMonth() < 1 || req.getMonth() > 12){
            List<SearchTopicRes> result = new ArrayList<>();
            result.add(new SearchTopicRes(CommonMessage.PARAM_ERROR));
            return result;
        }

        // Nullでの検索ができないので、キーワードがなければ空文字列にする
        if(!StringUtils.hasText(req.getKeyword())){
            req.setKeyword("");
        }

        // 検索
        List<SearchTopicRes> result = topicDao.searchTopicByReq(req.getYear(), req.getMonth(), req.getKeyword());
        if (result.isEmpty()) {
            result.add(new SearchTopicRes(CommonMessage.TOPIC_NOT_FOUND));
        }
        return result;
    }

    // 主旨チェック
    @Override
    public TopicContentRes checkTopic(String topicId) {
        // 引数チェック
        if (!StringUtils.hasText(topicId)) {
            return new TopicContentRes(CommonMessage.PARAM_ERROR);
        }

        // データ抽出
        TopicContentRes result = topicDao.checkTopic(topicId);
        if (result == null) {
            return new TopicContentRes(CommonMessage.TOPIC_NOT_FOUND);
        }
        return result;
    }

    // 主旨削除
    @Override
    public BaseRes deleteTopic(DeleteTopicReq req) {
        // 引数チェック＋存在チェック
        if (checkParam(req.getTopicId(), req.getTopicName(), req.getUpdater()) == false) {
            return new BaseRes(CommonMessage.PARAM_ERROR.getCode(), CommonMessage.PARAM_ERROR.getMessage());
        }
        if (checkExist(req.getTopicId(), req.getTopicName(), false) == false) {
            return new BaseRes(CommonMessage.TOPIC_NOT_FOUND.getCode(), CommonMessage.TOPIC_NOT_FOUND.getMessage());
        }

        // 削除
        int result = topicDao.deleteTopic(req.getTopicId(), req.getUpdater(), LocalDateTime.now());
        if(result != 1){
            return new BaseRes(CommonMessage.ERROR.getCode(), CommonMessage.ERROR.getMessage());
        }
        return new BaseRes(CommonMessage.REMOVE_SUCCESS.getCode(), CommonMessage.REMOVE_SUCCESS.getMessage());
    }



    // 引数チェック
    private boolean checkParam(String topicId, String topicName, String operator) {
        if (!StringUtils.hasText(topicId) || !StringUtils.hasText(topicName)
                || !StringUtils.hasText(operator)) {
            return false;
        }
        return true;
    }

    // データバイトチェック
    private boolean checkByte(String topicId, String topicName, String comment, String operator) {
        if (topicId.length() > 20 || topicName.length() > 20 || comment.length() > 50|| operator.length() > 20) {
            return false;
        }
        return true;
    }

    // 存在チェック
    private boolean checkExist(String topicId, String topicName, boolean isCreate) {
        int idLength = topicDao.searchTopicByTopicId(topicId).length();
        int nameLength = topicDao.searchTopicByTopicName(topicName).length();
        if (isCreate) {
            // 新規の場合、idLengthとnameLengthは全部0のはず
            if (idLength != 0 || nameLength != 0) {
                return false;
            }
        } else {
            // 更新と削除の場合、idLengthとnameLengthは全部0ではないはず
            if (idLength == 0 || nameLength == 0) {
                return false;
            }
        }
        return true;
    }
}
