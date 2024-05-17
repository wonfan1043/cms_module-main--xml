$(document).ready(function(){

    // 作成日を設定する
    // (1)現時点の時間を取得する
    let now = new Date();
    let year = now.getFullYear();
    let month = (now.getMonth() + 1).toString().padStart(2, '0');
    let date = now.getDate().toString().padStart(2, '0');
    let hour = now.getHours().toString().padStart(2, '0');
    let min = now.getMinutes().toString().padStart(2, '0');
    let sec = now.getSeconds().toString().padStart(2, '0');
    // (2)画面に表示する
    $(".currentTime").text(year + "/" + month + "/" + date + "　" + hour + ":" + min + ":" + sec);

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
        let topicPattern = /^[\p{L}\d]+$/u;
        if(!topicPattern.test(topicInput)){
            return alert("主旨は文字のみ入力してください。");
        }
        if(topicIdInput.length == 0){
            return alert("請求書主旨IDを入力してください。");
        }
        let topicIdPattern = /^[A-Za-z]+$/;
        if(!topicIdPattern.test(topicIdInput)){
            return alert("主旨IDは英語のみ入力してください。");
        }
        let commentPattern = /^[\p{L}]+$/u;
        if(!commentPattern.test(commentInput)){
            return alert("コメントは文字のみ入力してください。");
        }
        // 主旨モデルを宣言する
        let topic = {
            topicId: topicIdInput,
            topicName: topicInput,
            comment: commentInput,
            creater: "admin",
        }
        // 主旨を保存する
        $.ajax({
            url: '/manager/invoice/save_topic',
            type: 'POST',
            data: JSON.stringify(topic),
            contentType: "application/json",
            success: function(data) {
                // 成功の場合、メッセージを表示して、主旨リストページへ戻る
                if(data.code == 1010){
                    if(confirm("主旨作成しました！")){
                        window.location.href = '/manager/invoice/topic_list';
                    }
                } else {
                    // エラーの場合はメッセージを表示する
                    alert(`${data.message}!`)
                }
            }
        })
    })
    
})