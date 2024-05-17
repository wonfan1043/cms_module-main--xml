package com.inext.manage_system.controller.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.SearchTopicReq;
import com.inext.manage_system.dto.SearchTopicRes;
import com.inext.manage_system.dto.TopicContentRes;
import com.inext.manage_system.model.Topic;
import com.inext.manage_system.service.TopicService;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TopicServiceController {

    @Autowired
    private TopicService topicService;

    /**
     * 主旨リストページ
     * 
     * @param model モデルオブジェクト
     * @return テンプレートのパス
     */
    @GetMapping("/manager/invoice/topic_list")
    public String topicListPage(Model model){
        // 検索条件：年を「当年」に設定する
        int year = LocalDate.now().getYear();
        // 検索条件：月を「当月」に設定する
        int month = LocalDate.now().getMonthValue();
        // 検索条件：キーワードを「ヌル」に設定する
        String keyword = null;

        // ページ番号
        int pageNum = 1;
        // ページ数
        int pageSize = 7;

        // 主旨リストを取得
        SearchTopicReq req = new SearchTopicReq(pageNum, year, month, keyword);
        PageHelper.startPage(pageNum, pageSize);
        SearchTopicRes searchResult = topicService.searchTopic(req);
        PageInfo<Topic> topicList = new PageInfo<>(searchResult.getTopicList());

        // 主旨リストを追加する
        model.addAttribute("topicList", topicList);

        return "/manager/Topic";
    }
    
    /**
     * 主旨を検索する
     * 
     * @param model モデルオブジェクト
     * @return テンプレートのパス
     */
    @PostMapping("/manager/invoice/search_topic")
    public String searchTopic(@RequestBody SearchTopicReq req, Model model){
        // ページ番号
        int defaultPageNumber = 1;
        // 実際のページ番号
        Integer finalPageNumber = req.getPageNum() != null ? req.getPageNum() : defaultPageNumber;
        // ページ数
        int pageSize = 7;

        // 主旨リストを取得
        PageHelper.startPage(finalPageNumber, pageSize);
        SearchTopicRes searchResult = topicService.searchTopic(req);

        // 主旨リストがnullでないことを確認
        List<Topic> topicList = searchResult.getCode() == 1012 ? searchResult.getTopicList() : new ArrayList<>();

        // 主旨リストを追加する
        model.addAttribute("topicList", new PageInfo<>(topicList));

        return "/manager/Topic::list";
    }

    /**
     * 主旨削除
     * 
     * @param topic 削除する主旨
     * @return 結果メッセージオブジェクト
     */
    @PostMapping("/manager/invoice/delete_topic")
    @ResponseBody
    public BaseRes deleteTopic(@RequestBody Topic topic){
        // 主旨を削除して、結果を取得する
        return topicService.deleteTopic(topic);
    }

    /**
     * 主旨作成ページ
     * 
     * @return テンプレートのパス
     */
    @GetMapping("/manager/invoice/create_topic")
    public String createTopicPage(){
        return "/manager/TopicCreate";

    }
    
    /**
     * 主旨作成
     * 
     * @param topic 作成する主旨
     * @return 結果メッセージオブジェクト
     */
    @PostMapping("/manager/invoice/save_topic")   
    @ResponseBody
    public BaseRes createTopic(@RequestBody Topic topic){
        // 主旨を作成して結果を取得する
        return topicService.createTopic(topic);
    }
    
    /**
     * 主旨編集ページ
     * 
     * @param topicId 主旨ID
     * @param model モデルオブジェクト
     * @return  テンプレートのパス
     */
    @GetMapping("/manager/invoice/edit_topic")
    public String topicEdit(@RequestParam(required = true) String topicId, Model model){
        // 主旨詳細を取得する
        TopicContentRes searchResult = topicService.checkTopicContent(topicId);

        // 主旨詳細を追加する
        model.addAttribute("topicData", searchResult.getTopic());

        return "/manager/TopicEdit";
    }
    
    /**
     * 主旨を編集する
     * 
     * @param topic 編集する主旨
     * @return 結果メッセージオブジェクト
     */
    @PostMapping("/manager/invoice/update_topic")
    @ResponseBody
    public BaseRes updateTopic(@RequestBody Topic topic){
        // 主旨を編集して結果を取得する
        return topicService.updateTopic(topic);
    }

}
