$(document).ready(function(){

    // 検索条件：年のoptionを作成する
    // 2020をベースにして、毎年新しいoptionを追加する
    let baseYear = 2020;
    for(let i = baseYear; i < (new Date().getFullYear() + 1); i++){
        let yearOption = $(`<option value="${i}">${i}</option>`);
        $("#year").append(yearOption);
    }
    
    // 検索条件の年と月のデフォルト設定
    let year = new Date().getFullYear();
    let month = new Date().getMonth() + 1;
    $("#year").val(year);
    $("#month").val(month);

    // 新規ボタンのクリックイベント処理
    $(".addBtn").click(function(){
        // 新規作成ページへ飛ばす
        window.location.href = "/manager/invoice/create_invoice";
    })
    
    // 検索ボタンのクリックイベント処理
    $(".searchBtn").click(function(){
        // 日付タイプを取得する
        let dateTypeData = $("#dateType").val();
        // 年を取得する
        let yearData = $("#year").val();
        // 月を取得する
        let monthData = $("#month").val();
        // 協力会社名を取得する
        let corpData;
        if($("#corp").val() == "null"){
            corpData = null;
        } else {
            corpData = $("#corp").val();
        }
        // ページ番号を1にする
        let pageNumData = 1;
        // 検索条件のオブジェクトを宣言する
        let req = {
            pageNum: pageNumData,
            dateType: dateTypeData,
            year: yearData,
            month: monthData,
            corpName: corpData
        }
        // 請求書を検索する
        $.ajax({
            url: '/manager/invoice/search_invoice',
            type: 'POST',
            data: JSON.stringify(req),
            contentType: "application/json",
            // 検索結果を画面に表示する
            success: function (data) {
                if(!data.includes('<div class="invoice">')){
                    $("#list").empty();
                    return swal({
                        title: "残念です！",
                        text: "請求書見つかりませんでした。",
                        icon: "info"
                    });
                }
                $("#list").html(data);
            }
        })
    })
    
    // 詳細ボタンのクリックイベント処理
    $(".eachInvoice").on('click', '.viewBtn', function(){
        // 選択された請求書の番号を取得する
        let invoiceIdData = $(this).data("invoice-to-view");
        // 請求書詳細を取得して、詳細ページへ飛ばす
        window.location.href = `/manager/invoice/invoice_view?invoiceNo=${invoiceIdData}`;
    })
    
    // 削除ボタンのクリックイベント処理
    $(".eachInvoice").on('click', '.deleteBtn', function(){
        // 選択された請求書の番号を取得する
        let invoiceIdData = $(this).data("invoice-to-delete");
        // 再確認アラート
        swal({
            title: "よろしいですか？",
            text: `請求書「${invoiceIdData}」を削除してよろしいでしょうか？`,
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
                let invoice = {
                    invoiceNo: invoiceIdData,
                    updater: 'admin',
                };
                // 請求書を削除する
                $.ajax({
                    url: '/manager/invoice/delete_invoice',
                    type: 'POST',
                    data: JSON.stringify(invoice),
                    contentType: "application/json",
                    success: function(data){
                        // 成功の場合、メッセージを表示する
                        if (data.code === 1006) {
                            swal({
                                title: "完了です！",
                                text: "請求書は削除されました。",
                                icon: "info"
                            });
                            // 画面更新のため再度請求書を検索する
                            // (1)選択された検索条件を取得する
                            // 日付タイプ
                            let dateTypeData = $("#dateType").val(); 
                            // 年
                            let yearData = $("#year").val();
                            // 月
                            let monthData = $("#month").val();
                            // 協力会社名
                            let corpData; 
                            // 選択されていない場合はヌルにする
                            if($("#corp").val() == "null"){
                                corpData = null;
                            } else {
                                corpData = $("#corp").val();
                            }
                            // ページ番号
                            let pageNumData = $("#currentPageNum").val();
                            // (2)検索条件のオブジェクトを宣言する
                            let req = {
                                pageNum: pageNumData,
                                dateType: dateTypeData,
                                year: yearData,
                                month: monthData,
                                corpName: corpData
                            }
                            // (3)請求書を検索する
                            $.ajax({
                                url: '/manager/invoice/search_invoice',
                                type: 'POST',
                                data: JSON.stringify(req),
                                contentType: "application/json",
                                // 検索結果を画面に表示する
                                success: function (data) {
                                    if(!data.includes('<div class="invoice">')){
                                        $("#list").empty();
                                        return null;
                                    }
                                    $("#list").html(data);
                                }
                            })
                        } else {
                            // 失敗の場合、エラーメッセージを表示する
                            swal({
                                title: "残念です！",
                                text: `${data.message}!`,
                                icon: "error"
                            })
                        }
                    }
                })
            }
        })
    })
    
    // 次ページボタンのクリックイベント処理
    $(".nextPageBtn").click(function(){
        // 画面にすでに情報なしの場合、検索しない
        if($(".invoice").length == 0){
            return swal({
                title: "他のページありません！",
                text: "請求書を検索してください。",
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
        // 日付タイプを取得する
        let dateTypeData = $("#dateType").val();
        // 年を取得する
        let yearData = $("#year").val();
        // 月を取得する
        let monthData = $("#month").val();
        // 協力会社名を取得する
        let corpData;
        // 選択されていない場合はヌルにする
        if($("#corp").val() == "null"){
            corpData = null;
        } else {
            corpData = $("#corp").val();
        }
        // 検索ページ番号は現在のページページ番号に1を加える
        let pageNumData = Number($("#currentPageNum").val()) + 1;
        // 検索条件のオブジェクトを宣言する
        let req = {
            pageNum: pageNumData,
            dateType: dateTypeData,
            year: yearData,
            month: monthData,
            corpName: corpData
        }
        // 請求書を検索する
        $.ajax({
            url: '/manager/invoice/search_invoice',
            type: 'POST',
            data: JSON.stringify(req),
            contentType: "application/json",
            // 検索結果を画面に表示する
            success: function (data) {
                $("#list").html(data);
            }
        })
    })
    
    // 前ページボタンのクリックイベント処理
    $(".previousPageBtn").click(function(){
        // 画面にすでに情報なしの場合、検索しない
        if($(".invoice").length == 0){
            return swal({
                title: "他のページありません！",
                text: "請求書を検索してください。",
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
        // 日付タイプを取得する
        let dateTypeData = $("#dateType").val();
        // 年を取得する
        let yearData = $("#year").val();
        // 月を取得する
        let monthData = $("#month").val();
        // 協力会社名を取得する
        let corpData;
        // 選択されていない場合はヌルにする
        if($("#corp").val() == "null"){
            corpData = null;
        } else {
            corpData = $("#corp").val();
        }
        // 検索ページ番号は現在のページページ番号に1を減らす
        let pageNumData = Number($("#currentPageNum").val()) - 1;
        // 検索条件のオブジェクトを宣言する
        let req = {
            pageNum: pageNumData,
            dateType: dateTypeData,
            year: yearData,
            month: monthData,
            corpName: corpData
        }
        // 請求書を検索する
        $.ajax({
            url: '/manager/invoice/search_invoice',
            type: 'POST',
            data: JSON.stringify(req),
            contentType: "application/json",
            // 検索結果を画面に表示する
            success: function (data) {
                $("#list").html(data);
            }
        })
    })

})