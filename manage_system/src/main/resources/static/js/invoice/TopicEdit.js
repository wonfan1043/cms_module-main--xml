$(document).ready(function(){

    // 取消ボタンのクリックイベント処理
    $(".cancelBtn").click(function(){
        // 再確認アラート
        if(confirm("編集した内容は保存されませんが、よろしいでしょうか？")){
            // 主旨リストページへ戻る
            window.location.href = "/manager/invoice/topic_list";
        }
    })
    
    // 保存ボタンのクリックイベント処理
    $(".saveBtn").click(function(){
        // 主旨名を取得する
        const topicInput = $(".topic").val();
        // 主旨IDを取得する
        const topicIdInput = $(".topicId").val();
        // コメントを取得する
        const commentInput = $(".comment").val();
        // 入力チェック
        if(topicInput.length == 0){
            return alert("請求書主旨を入力してください。");
        }
        if(topicIdInput.length == 0){
            return alert("請求書主旨IDを入力してください。");
        }
        // 主旨モデルを宣言する
        let topic = {
            topicId: topicIdInput,
            topicName: topicInput,
            comment: commentInput,
            updater: "admin",
        }
        // 主旨を保存する
        $.ajax({
            url: '/manager/invoice/update_topic',
            type: 'POST',
            data: JSON.stringify(topic),
            contentType: "application/json",
            success: function(data) {
                // 成功の場合、メッセージを表示して、主旨リストページへ戻る
                if(data.code == 1008){
                    if(confirm("主旨編集しました！")){
                        window.location.href = '/manager/invoice/topic_list';
                    }
                } else {
                    // エラーの場合はメッセージを表示する
                    alert(`${data.message}!\nもう一度やってみてください。`)
                }
            }
        })
    })

})