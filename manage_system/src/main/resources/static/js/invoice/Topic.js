$(document).ready(function(){

    // 検索条件：年のoptionを作成する
    // 2020をベースにして、毎年新しいoptionを追加する
    let baseYear = 2020;
    for(let i = baseYear; i < (new Date().getFullYear() + 1); i++){
        let yearOption = $(`<option value="${i}">${i}</option>`);
        $("#year").append(yearOption);
    }

    // 検索条件の年と月のデフォルト設定
    // (1)現時点の年と月を取得する
    let year = new Date().getFullYear();
    let month = new Date().getMonth() + 1;
    // (2)検索条件の年と月を設定する
    $('#year').val(year);
    $('#month').val(month);
    
    // 検索ボタンのクリックイベント処理
    $(".searchBtn").click(function(){
        // ページ番号を1にする
        let pageNumData = 1;
        // 検索条件の年、月とキーワードを取得する
        let yearData = $("#year").val();
        let monthData = $("#month").val();
        let keywordData = $("#keyword").val();
        // 検索オブジェクトを定義する
        let searchTopicReq = {
            pageNum: pageNumData,
            year: yearData,
            month: monthData,
            keyword: keywordData
        }
        // 主旨を検索する
        $.ajax({
            url: "/manager/invoice/search_topic",
            type: 'POST',
            data: JSON.stringify(searchTopicReq),
            contentType: 'application/json',
            success: function (data) {
                if(!data.includes('<div class="topic" id="topic">')){
                    $("#list").empty();
                    return swal({
                        title: "残念です！",
                        text: "主旨見つかりませんでした。",
                        icon: "info"
                    });
                }
                $("#list").html(data);
            }
        })
    })

    // 次ページボタンのクリックイベント処理
    $(".nextPageBtn").click(function(){        
        // 画面にすでに情報なしの場合、検索しない
        if($("#topic").length == 0){
            return swal({
                title: "他のページありません！",
                text: "主旨を検索してください。",
                icon: "info"
            });
        }
        // 現在のページが最後のページの場合、検索しない
        let currentPageNum = $('#currentPageNum').val();
        let lastPageNum = $('#lastPageNum').val();
        if(currentPageNum == lastPageNum){
            return swal({
                title: "もうないです！",
                text: "現在のページは最後のページです。",
                icon: "info"
            });
        }
        // 検索条件の年、月とキーワードを取得する
        let yearData = $("#year").val();
        let monthData = $("#month").val();
        let keywordData = $("#keyword").val();
        // 検索ページ番号は現在のページページ番号に1を加える
        let pageNumData = currentPageNum + 1;
        // 検索オブジェクトを定義する
        let searchTopicReq = {
            pageNum: pageNumData,
            year: yearData,
            month: monthData,
            keyword: keywordData
        }
        // 主旨を検索する
        $.ajax({
            url: "/manager/invoice/search_topic",
            type: 'POST',
            data: JSON.stringify(searchTopicReq),
            contentType: 'application/json',
            success: function (data) {
                $("#list").html(data);
            }
        })
    })

    // 前ページボタンのクリックイベント処理
    $(".previousPageBtn").click(function(){
        // 画面にすでに情報なしの場合、検索しない
        if($("#topic").length == 0){
            return swal({
                title: "他のページありません！",
                text: "主旨を検索してください。",
                icon: "info"
            });
        }
        // 現在のページが最初のページの場合、検索しない
        let currentPageNum = $('#currentPageNum').val();
        if(currentPageNum == 1){
            return swal({
                title: "もうないです！",
                text: "現在のページは最初のページです。",
                icon: "info"
            });
        }
        // 検索条件の年、月とキーワードを取得する
        let yearData = $("#year").val();
        let monthData = $("#month").val();
        let keywordData = $("#keyword").val();
        // 検索ページ番号は現在のページページ番号に1を減らす
        let pageNumData = currentPageNum - 1;
        // 検索オブジェクトを定義する
        let searchTopicReq = {
            pageNum: pageNumData,
            year: yearData,
            month: monthData,
            keyword: keywordData
        }
        // 主旨を検索する
        $.ajax({
            url: "/manager/invoice/search_topic",
            type: 'POST',
            data: JSON.stringify(searchTopicReq),
            contentType: 'application/json',
            // 検索結果を画面に表示する
            success: function (data) {
                $("#list").html(data);
            }
        })
    })

    // 編集ボタンのクリックイベント処理
    $(".eachTopic").on("click", ".editBtn", function(){
        // 主旨IDを取得する
        let topicIdData = $(this).data("topic-to-edit");
        // 主旨編集ページへ
        window.location.href = `/manager/invoice/edit_topic?topicId=${topicIdData}`;
    })

    //削除ボタンのクリックイベント処理
    $(".eachTopic").on("click", ".deleteBtn", function(){
        // 主旨IDと主旨名を取得
        const topicToDeleteData = $(this).data("topic-to-delete");
        let topicIdData = topicToDeleteData.split('|')[0];
        let topicNameData = topicToDeleteData.split('|')[1];
        // 再確認アラート
        swal({
            title: "よろしいですか？",
            text: `主旨「${topicNameData}」を削除してよろしいでしょうか？`,
            icon: "warning",
            buttons: {
                confirm: {
                    text: "はい",
                    visible: true
                },
                cancel: {
                    text: "いいえ",
                    visible: true
                }
            }
        }).then((res) => {
            if(res == true){
                // 主旨モデルを宣言する
                let topic = {
                    topicId: topicIdData,
                    topicName: topicNameData,
                    updater: 'admin'
                };
                // 主旨を削除する
                $.ajax({
                    url: "/manager/invoice/delete_topic",
                    type: 'POST',
                    data: JSON.stringify(topic),
                    contentType: "application/json",
                    success: function(data){
                        // 成功の場合、メッセージを表示して、主旨リストを検索して画面を更新する
                        if (data.code === 1006) {
                            // 成功のメッセージ
                            swal({
                                title: "完了です！",
                                text: "主旨は削除されました。",
                                icon: "info"
                            });
                            // 検索条件の年、月とキーワードを取得する
                            let yearData = $("#year").val();
                            let monthData = $("#month").val();
                            let keywordData = $("#keyword").val();
                            // ページ番号を取得する
                            let pageNumData = $('#currentPageNum').val();
                            // 検査オブジェクトを定義する
                            let searchTopicReq = {
                                pageNum: pageNumData,
                                year: yearData,
                                month: monthData,
                                keyword: keywordData
                            }
                            // 主旨を検索する
                            $.ajax({
                                url: '/manager/invoice/search_topic',
                                type: 'POST',
                                data: JSON.stringify(searchTopicReq),
                                contentType: "application/json",
                                success: function (data) {
                                    if(!data.includes('<div class="topic" id="topic">')){
                                        $("#list").empty();
                                        return null;
                                    }
                                    $("#list").html(data);
                                }
                            })
                        }
                    }
                })
            }
        })
    });
})