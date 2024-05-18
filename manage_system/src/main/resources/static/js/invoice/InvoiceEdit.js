$(document).ready(function(){

    // よく使う要素を取得しておく
    // 振込銀行選択
    let selectedBank = $("#bankList");
    // 振込銀行
    let bankName = $("#bankName");
    // 振込銀行住所
    let bankAddress = $("#bankAddress");
    // SWIFT CODE
    let swiftCode = $("#swiftCode");
    // 口座名
    let holder = $("#holder");
    // 受取人住所
    let holderAddress = $("#holderAddress");
    // 口座Ｎo.
    let account = $("#account");
    // 決済期限：月
    let month = $("#month");
    // 消費税
    let tax = $("#tax");
    // 金額（税抜）
    let beforeTax = $("#beforeTax");
    // 合計（税込）
    let afterTax = $("#afterTax");

    // 会社サンプルドロップダウンリストの変更イベント処理
    $("#corp").change(function(){
        // 選択された協力会社を取得する
        let selectedSample = $("#corp").val();
        // 会社サンプル取得のAPIを呼び出す
        $.ajax({
            url: `/manager/invoice/get_sample?corpName=${selectedSample}`,
            type: "GET",
            success: function(data){
                // 成功の場合、サンプルの詳細を該当する要素に表示する
                if(data.code == 1012){
                    // サンプル詳細を取得する
                    let corpSample = data.sample;
                    // 発注先を表示する
                    $("option.corpOption").each(function(index, opt){
                        if($(opt).val().includes(selectedSample)){
                            $("#supplier").val(opt.val());
                            $("#supplier").trigger('change');
                        }
                    })
                    // 主旨を表示する
                    let topicOptions = $(".topicOpt");
                    let topicIsDeleted = false;
                    for(let i = 0; i < topicOptions.length; i++){
                        if(topicOptions[i].value == corpSample.topicName){
                            return topicIsDeleted = true;
                        }
                    }
                    if(topicIsDeleted == true){
                        $("#topic").val(corpSample.topicName);
                    } else {
                        swal({
                            title: "残念です！",
                            text: `主旨「${corpSample.topicName}」はすでに削除されました。\n他の主旨を選択してください。`,
                            icon: "info"
                        });                    }
                    // 発送先を表示する
                    $("#receiver").val(corpSample.receiver);
                    // 発送先都道府県を表示する
                    $("#county").val(corpSample.county);
                    // 発送先郵便番号を表示する
                    $("#postcode").val(corpSample.postcode);
                    // 発送先市区町村を表示する
                    $("#town").val(corpSample.town);
                    // 発送先住所を表示する
                    $("#address").val(corpSample.address);
                    // 発送先ビル名はヌルではない場合のみ表示する
                    if(corpSample.building != null){
                        $("#building").val(corpSample.building);
                    }
                    // 振込銀行選択を表示する
                    $("#bankList").val(corpSample.bankId);
                    // bankListのchangeイベントを手動で実行して口座情報を取得する
                    $("#bankList").trigger('change');
                    // 消費税を表示する
                    $("#tax").val(corpSample.tax);
                } else {
                    // エラーの場合はエラーメッセージを表示する
                    swal({
                        title: "残念です！",
                        text: `${data.message.message}!\nやり直しお願いします。`,
                        icon: "error"
                    });
                }
            }
        })
    })

    // サプライヤードロップダウンリストの変更イベントの処理
    $("#supplier").change(function(){
        // bankList要素を有効にする
        $("#bankList").prop("disabled", false);
        $("#bankList").val(undefined);
        // 銀行と口座情報をリセットする
        bankName.val(null);
        bankAddress.val(null);
        swiftCode.val(null);
        holder.val(null);
        holderAddress.val(null);
        account.val(null);
        // 選択された協力会社を取得する
        let corp = $("#supplier").val();
        let selectedCorp = corp.split("|")[0];
        // 全ての銀行optionを取得する
        let allBanks = $("#bankList option");
        for(let i = 0; i < allBanks.length; i++){
            allBanks[i].style.display = "unset";
        }    
        // 選択された協力会社の銀行/口座ではないoptionを非表示にする
        for(let i = 0; i < allBanks.length; i++){
            if(!allBanks[i].value.includes(selectedCorp)){
                allBanks[i].style.display = "none";
            }
        }    
    })

    // 銀行ドロップダウンリストの変更イベントの処理
    $("#bankList").on('change', function(){
        // 選択された銀行のIDを取得する
        let selectedBankId = selectedBank.val();
        // 口座取得APIを呼び出す
        $.ajax({
            url: `/manager/invoice/get_bank_info?bankId=${selectedBankId}`,
            type: 'GET',
            success: function (data) {
                // 取得した銀行情報を表示する
                // (1)台湾の銀行の場合
                if(data.bankArea == "tw"){
                    bankName.val(data.bankName);
                    bankAddress.val(data.twBankPostCode + "　" + data.twBankCounty + data.twBankTown + data.twBankRoad + data.twBankAddress + data.twBankHouseName);
                    swiftCode.val(data.swiftCode);
                    holder.val(data.accountName);
                    holderAddress.val(data.twReceivePostCode + "　" + data.twReceiveCounty + data.twReceiveTown + data.twReceiveRoad + data.twReceiveAddress + data.twReceiveHouseName);
                    account.val(data.accountNumber);
                } else {
                    // (2)日本の銀行の場合
                    bankName.val(data.bankName);
                    bankAddress.val(data.jpBankPostCode + "　" + data.jpBankCounty + data.jpBankTown + data.jpBankAddress + data.jpBankHouseName);
                    swiftCode.val(data.swiftCode);
                    holder.val(data.accountName);
                    holderAddress.val(data.jpReceivePostCode + "　" + data.jpReceiveCounty + data.jpReceiveTown + data.jpReceiveAddress + data.jpReceiveHouseName);
                    account.val(data.accountNumber);
                }
                // 振込銀行住所に「null」を含む場合は削除する
                if(bankAddress.val().includes("null")){
                    bankAddress.val(bankAddress.val().replace("null", ""));
                }
                // 受取人住所に「null」を含む場合は削除する
                if(holderAddress.val().includes("null")){
                    holderAddress.val(holderAddress.val().replace("null", ""));
                }
            }
        })
    })

    // 商品ドロップダウンリストの設定
    // 商品リストを取得する
    let productList = $("#data").data("product-list");
    // 配列に変換する
    let productListArr = productList.slice(1, (productList.length-1)).split(" ");
    // 商品名と価格の配列を定義する
    let itemName = [];
    let itemPrice = [];
    // 商品名と価格をそれぞれの配列に入れる
    for(let i = 3; i < productListArr.length; i += 10){
        itemName.push(productListArr[i].slice(9, productListArr[i].length));
        itemPrice.push(productListArr[i+1].slice(12, productListArr[i+1].length));
    }
    // optionを追加する
    for(let i = 0; i < itemName.length; i++){
        // 商品名を取得する
        let productName = itemName[i].slice(0, (itemName[i].length-1))
        // optionを定義する
        let option = $(`<option value="${i}|${productName}">${productName}</option>`);
        // optionを追加する
        $("#product").append(option);
    }

    // 商品ドロップダウンリストの変更イベントの処理
    $(".chargeContent").on('change', '#product', function(e){
        // 選択された商品のindexを取得する
        let selectedItemIndex = e.target.value.split("|")[0];
        // 価格を設定する
        $(this).closest(".eachItem").find("#unitPrice").val(parseInt(itemPrice[selectedItemIndex]));
    })

    // 追加ボタンのクリックイベント処理
    $(".addBtn").click(function(){
        // 追加するhtml内容を定義する
        let newChargeContent = $('<div class="eachItem"><div class="row"><p>内容</p><select name="product" id="product" class="product"><option value="default" selected disabled>商品</option></select></div><div class="row"><p>数量</p><input type="number" id="quantity" class="quantity" /></div><div class="row"><p>単価</p><input type="number" id="unitPrice" class="unitPrice" /><button type="button" class="deleteBtn">削除</button></div><div class="gap"></div></div>');
        // chargeContentに追加する
        $(".chargeContent").append(newChargeContent);
        // 商品ドロップダウンリストを追加する
        for(let i = 0; i < itemName.length; i++){
            // 商品名を取得する
            let productName = itemName[i].slice(0, (itemName[i].length-1))
            // optionを定義する
            let option = $(`<option value="${i}|${productName}">${productName}</option>`);
            // optionを追加する
            $(".chargeContent select").append(option);
        }
    })

    // chargeContent内の削除ボタンのクリックイベント処理
    $(".chargeContent").on('click', '.deleteBtn', function(){
        // 最も近い.eachItem要素を削除する
        $(this).closest(".eachItem").remove();
        // 金額（税抜）と合計（税込）をリセット
        beforeTax.val(0);
        afterTax.val(0);
        // 数量と単価の要素を取得する
        const quantity = $("#quantity");
        const unitPrice = $("#unitPrice");
        // 金額（税抜）を計算する
        for(let i = 0; i < quantity.length; i++){
            beforeTax.val(parseInt(beforeTax.val()) + parseInt(quantity[i].value * unitPrice[i].value));
        }
        // 税率を取得する
        let taxRate = tax.val();
        // 合計（税込）を計算する
        afterTax.val(parseInt(beforeTax.val() * (1 + parseFloat(taxRate))));
    })

    // chargeContent内の内容input、数量input、単価inputの変更イベント処理
    $(".chargeContent").on('change', '#product, #quantity, #unitPrice', function(){
        // 金額（税抜）と合計（税込）をリセット
        beforeTax.val(0);
        afterTax.val(0);
        // 数量と単価の要素を取得する
        let quantity = $(".chargeContent #quantity");
        let unitPrice = $(".chargeContent #unitPrice");
        // 1つの場合は直接計算する
        if(quantity.length == 1){
            beforeTax.val(parseInt(beforeTax.val()) + parseInt(quantity.val() * unitPrice.val()));
        } else {
            // 複数の場合は合計する
            for(let i = 0; i < quantity.length; i++){
                beforeTax.val(parseInt(beforeTax.val()) + parseInt(quantity[i].value * unitPrice[i].value));
            }
        }
        // 税率を取得する
        let taxRate = tax.val();
        // 合計（税込）を計算する
        afterTax.val(parseInt(beforeTax.val() * (1 + parseFloat(taxRate))));
    })

    // 消費税の変更イベント処理
    $("#tax").change(function(){
        // 選択された税率を取得する
        let selectedTax = $("#tax").val();
        // 合計（税込）をリセットする
        afterTax.val(0);
        // 合計（税込）を計算する
        afterTax.val(parseInt(beforeTax.val() * (1 + parseFloat(selectedTax))));
    })

    // 消費税追加ボタンのクリックイベント処理
    $(".addTax").click(function(){
        // 入力内容を取得する
        let taxInput = $(".taxInput").val();
        // 長さで内容あるかチェックする
        if(taxInput.length == 0){
            $(".taxInput").val(null);
            return swal({
                title: "まだまだ！",
                text: "税率を入力してください。",
                icon: "info"
            });
        }
        // 1-99の正の整数であるかチェックする
        let taxRatePattern = /^(0?[1-9]|[1-9][0-9])$/;
        if(taxRatePattern.test(taxInput) == false){
            $(".taxInput").val(null);
            return swal({
                title: "お願い！",
                text: "無効な税率です。1~99の正の整数を入力してください。",
                icon: "info"
            });
        }
        // 重複する税率であるかチェックする
        let newTaxRate = parseFloat(parseInt(taxInput)/100);
        let isDuplicated = false;
        $("#tax option").each(function(index, tax){
            if(tax.value == newTaxRate){
                isDuplicated = true;
            }
        })
        if(isDuplicated == true){
            $(".taxInput").val(null);
            return swal({
                title: "お願い！",
                text: "重複する税率です。\n直接ドロップダウンリストから選択してください。",
                icon: "info"
            });
        }
        // 新しい選択肢を追加する
        let newTaxText = parseInt(taxInput);
        let newTaxOption = $(`<option value="${newTaxRate}">${newTaxText}%</option>`);
        $("#tax").append(newTaxOption);
        $(".taxInput").val(null);
        swal({
            title: "ナイス！",
            text: "税率を追加しました！",
            icon: "info"
        });
    })

    // 決済期限の年の変更イベント処理
    $("#year").on('change', function(){
        // 決済期限の月をリセットする
        month.val("default");
        // 決済期限の日付をリセットする
        $("#date").empty();
    })

    // 決済期限の月の変更イベント処理
    $("#month").on('change', function(){
        // 決済期限の日付をリセットする
        $("#date").empty();
        // 選択された月を取得する
        let monthValue = $("#month").val();
        // 選択された月によって日付のoptionを作成する
        switch(monthValue){
            // 1、3、5、7、8、10、12の場合は、1～31日を作成する
            case '01':
            case '03':
            case '05':
            case '07':
            case '08':
            case '10':
            case '12':
                for(let i = 1; i < 32; i++){
                    // 1～9の場合は「01」、「02」のようにする
                    if(i < 10){
                        let day = $(`<option value="0${i}">${i}</option>`);
                        $("#date").append(day);
                    } else {
                        let day = $(`<option value="${i}">${i}</option>`);
                        $("#date").append(day);
                    }
                }
                break;
            // 2の場合は、1～28日を作成する
            case '02':
                for(let i = 1; i < 29; i++){
                    // 1～9の場合は「01」、「02」のようにする
                    if(i < 10){
                        let day = $(`<option value="0${i}">${i}</option>`);
                        $("#date").append(day);
                    } else{
                        let day = $(`<option value="${i}">${i}</option>`);
                        $("#date").append(day);
                    }
                }
                break;
            // 4、6、9、11の場合は、1～30日を作成する
            case '04':
            case '06':
            case '09':
            case '11':
                for(let i = 1; i < 31; i++){
                    // 1～9の場合は「01」、「02」のようにする
                    if(i < 10){
                        let day = $(`<option value="0${i}">${i}</option>`);
                        $("#date").append(day);
                    } else {
                        let day = $(`<option value="${i}">${i}</option>`);
                        $("#date").append(day);
                    }
                }
                break;
        }
        // 閏年かつ2月の場合、29日を追加する
        if($("#year").val() % 4 == 0 && monthValue == 2){
            let extraDay = $(`<option value="29">29</option>`);
            $("#date").append(extraDay);
        }
    })

    // 請求書詳細を該当するところに表示する
    // 請求書番号を設定する
    $("#invoiceNo").text(localStorage.getItem("invoiceNo"));
    // 作成日を設定する
    $("#createDate").text(localStorage.getItem("createDate"));
    // 発注先を取得する
    let invoiceCorpName = localStorage.getItem("corpName");
    // 発注先ドロップダウンリストを設定する
    $(".corpOption").each(function(index, item) {
        if(item.value.includes(invoiceCorpName)){
            $("#supplier").val(item.value);
            // 発注先ドロップダウンリストの変更イベントを手動で実行する
        }
    })
    $("#supplier").trigger('change');
    // 主旨を設定する
    let originalTopic = localStorage.getItem("topicName");
    let topicOptions = $(".topicOpt");
    let topicExists = false;
    $(".topicOpt").each(function(index, topic){
        if(topic.value == originalTopic){
            topicExists = true;
            return false;
        }
    })
    if(topicExists == true){
        $("#topic").val(originalTopic);
    } else {
        swal({
            title: "残念です！",
            text: `主旨「${corpSample.topicName}」はすでに削除されました。\n他の主旨を選択してください。`,
            icon: "info"
        });
    }
    // 発送先を設定する
    $("#receiver").val(localStorage.getItem("receiver"));
    // 発送先都道府県を設定する
    $("#county").val(localStorage.getItem("county"));
    // 発送先郵便番号を設定する
    $("#postcode").val(localStorage.getItem("postcode"));
    // 発送先市区町村を設定する
    $("#town").val(localStorage.getItem("town"));
    // 発送先住所を設定する
    $("#address").val(localStorage.getItem("address"));
    // 発送先ビル名を設定する
    $("#building").val(localStorage.getItem("building"));
    // 銀行を取得する
    let invoiceBankName  = localStorage.getItem("bankName");
    // 振込銀行選択ドロップダウンリストを設定する
    $("#bankList #bank").each(function(index, item) {
        if(item.innerText.includes(invoiceBankName) && item.style.display == "unset"){
            $("#bankList").val(item.value);
        }
    })
    // 振込銀行を設定する
    $("#bankName").val(localStorage.getItem("bankName"));
    // 振込銀行住所を設定する
    $("#bankAddress").val(localStorage.getItem("bankAddress"));
    // SWIFT CODEを設定する
    $("#swiftCode").val(localStorage.getItem("swiftCodeData"));
    // 口座名を設定する
    $("#holder").val(localStorage.getItem("holder"));
    // 受取人住所を設定する
    $("#holderAddress").val(localStorage.getItem("receiveAddress"));
    // 口座Ｎo.を設定する
    $("#account").val(localStorage.getItem("account"));
    // 商品名を取得して配列を定義する
    let itemNameArr  = localStorage.getItem("itemName").split(",");
    // 数量を取得して配列を定義する
    let quantityArr  = localStorage.getItem("quantity").split(",");
    // 単価を取得して配列を定義する
    let unitPriceArr  = localStorage.getItem("unitPrice").split(",");
    // 商品名ドロップダウンリストを設定する
    for(let i = 0; i < itemNameArr.length; i++){
        // 一番目の商品は直接に設定する
        if(i == 0){
            // 一致するoptionを見つけて設定する
            $("#product option").each(function(index, item) {
                if(item.innerText == itemNameArr[i]){
                    $("#product").val(item.value);
                }
            })
            // 数量を設定する
            $("#quantity").val(quantityArr[i]);
            // 単価を設定する
            $("#unitPrice").val(unitPriceArr[i]);
        } else {
            // 二番目からの商品は要素を追加してから設定する
            // 追加するhtml内容を定義する
            let newChargeContent = $('<div class="eachItem"><div class="row"><p>内容<span class="necessary">*</span></p><select name="product" id="product" class="product"><option value="default" selected disabled>商品</option></select></div><div class="row"><p>数量<span class="necessary">*</span></p><input type="number" id="quantity" class="quantity" /></div><div class="row"><p>単価<span class="necessary">*</span></p><input type="number" id="unitPrice" class="unitPrice" /><button type="button" class="deleteBtn">削除</button></div><div class="gap"></div></div>');
            // chargeContentに追加する
            $(".chargeContent").append(newChargeContent);
            // 商品ドロップダウンリストを追加する
            for(let i = 0; i < itemName.length; i++){
                // 商品名を取得する
                let productName = itemName[i].slice(0, (itemName[i].length-1))
                // optionを定義する
                let option = $(`<option value="${i}|${productName}">${productName}</option>`);
                // optionを追加する
                newChargeContent.find('#product').append(option);
            }
            // 一致するoptionを見つけて設定する
            $("#product option").each(function(index, item) {
                if(item.innerText == itemNameArr[i])
                newChargeContent.find('#product').val(item.value);
            })
            // 数量を設定する
            newChargeContent.find('#quantity').val(quantityArr[i]);
            // 単価を設定する
            newChargeContent.find('#unitPrice').val(unitPriceArr[i]);
        }
    }
    // 数量の変更イベントを手動で実行する
    $("#quantity").trigger('change');
    // 消費税を設定する
    $("#tax").val(parseInt(localStorage.getItem("tax"))/100);
    // 消費税の変更イベントを手動で実行する
    $("#tax").trigger('change');
    // 決済期限を取得して配列にする
    let dueDateArr = localStorage.getItem("paymentDue").split("　");
    // 決済期限の年を設定して、変更イベントを手動で実行する
    $("#year").val(dueDateArr[0]);
    $("#year").trigger('change');
    // 決済期限の月を設定して、変更イベントを手動で実行する
    $("#month").val(dueDateArr[3]);
    $("#month").trigger('change');
    // 決済期限の日付を設定する
    $("#date").val(dueDateArr[6]);
    // メモを設定する
    $("#memo").val(localStorage.getItem("memo"));
    // 設定完了、localStorageにある情報を削除する
    localStorage.removeItem("invoiceNo");
    localStorage.removeItem("createDate");
    localStorage.removeItem("corpName");
    localStorage.removeItem("topicName");
    localStorage.removeItem("receiver");
    localStorage.removeItem("county");
    localStorage.removeItem("postcode");
    localStorage.removeItem("town");
    localStorage.removeItem("address");
    localStorage.removeItem("building");
    localStorage.removeItem("bankName");
    localStorage.removeItem("bankAddress");
    localStorage.removeItem("swiftCodeData");
    localStorage.removeItem("holder");
    localStorage.removeItem("receiveAddress");
    localStorage.removeItem("account");
    localStorage.removeItem("itemName");
    localStorage.removeItem("quantity");
    localStorage.removeItem("unitPrice");
    localStorage.removeItem("tax");
    localStorage.removeItem("paymentDue");
    localStorage.removeItem("memo");

    // モード保存ボタンのクリックイベント
    $(".sampleBtn").click(function(){
        // 選択された発注先を取得する
        let corpNameData = $("#supplier").val();
        if(corpNameData == undefined){
            return swal({
                title: "まだまだ！",
                text: "発注先を選択してください。",
                icon: "info"
            });
        }
        corpNameData = corpNameData.split("|")[1];
        // 選択された主旨を取得する
        let topicNameData = $("#topic").val();
        if(topicNameData == undefined){
            return swal({
                title: "まだまだ！",
                text: "主旨を選択してください。",
                icon: "info"
            });
        }
        // 選択された発送先を取得する
        let receiverData = $("#receiver").val();
        if(receiverData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先を入力してください。",
                icon: "info"
            });
        }
        // 選択された発送先都道府県を取得する
        let postcodeData = $("#postcode").val();
        if(countyData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先都道府県を入力してください。",
                icon: "info"
            });
        }
        // 選択された発送先郵便番号を取得する
        let countyData = $("#county").val();
        if(postcodeData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先郵便番号を入力してください。",
                icon: "info"
            });
        }
        // 選択された発送先市区町村を取得する
        let townData = $("#town").val();
        if(townData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先市区町村を入力してください。",
                icon: "info"
            });
        }
        // 選択された発送先住所を取得する
        let addressData = $("#address").val();
        if(addressData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先住所を入力してください。",
                icon: "info"
            });
        }
        // 発送先ビル名が入力された場合は内容を取得する
        let buildingData;
        if($("#building").length > 0){
            buildingData = $("#building").val();
        }
        // 選択された銀行を取得する
        let bankIdData = selectedBank.val();
        if(bankIdData == undefined){
            return swal({
                title: "まだまだ！",
                text: "振込銀行選択を入力してください。",
                icon: "info"
            });
        }
        // 選択された税率を取得する
        let taxRate = tax.val();
        // 会社サンプルのモデルを宣言する
        let invoiceSample = {
            corpName: corpNameData,
            receiver: receiverData,
            postcode: postcodeData,
            county: countyData,
            town: townData,
            address: addressData,
            building: buildingData,
            topicName: topicNameData,
            bankId: bankIdData,
            tax: taxRate,
            creator: "admin",
            updater: "admin"
        }
        // サンプルを保存する
        $.ajax({
            url: "/manager/invoice/save_sample",
            type: 'POST',
            data: JSON.stringify(invoiceSample),
            contentType: 'application/json',
            success: function(data){
                // 成功時の処理
                if(data.code == 1010 || data.code == 1008){
                    // 成功メッセージを表示し、ホームページに戻るかどうか確認する
                    swal({
                        title: "ナイス！",
                        text: `${data.message}!\nホームページに戻りますか？`,
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
                            window.location.href = "/manager/invoice/invoice_list";
                        }
                    })
                } else {
                    swal({
                        titl: "残念です！",
                        text: `${data.message}!\nやり直しお願いします。`,
                        icon: "error"
                    })
                }
            }
        })
    })

    // 保存ボタンのクリックイベント処理
    $(".saveBtn").click(function(){
        // 各要素の入力チェックと内容の取得
        // 請求書番号
        let invoiceNoData = $("#invoiceNo").text();
        // 発注先
        let corpNameData = $("#supplier").val();
        if(corpNameData == undefined){
            return swal({
                title: "まだまだ！",
                text: "発注先を選択してください。",
                icon: "info"
            });
        }
        corpNameData = corpNameData.split("|")[1];
        // 主旨
        let topicNameData = $("#topic").val();
        if(topicNameData == undefined){
            return swal({
                title: "まだまだ！",
                text: "主旨を選択してください。",
                icon: "info"
            });
        }
        // 発送先
        let receiverData = $("#receiver").val();
        if(receiverData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先を入力してください。",
                icon: "info"
            });
        }
        // 発送先都道府県
        let countyData = $("#county").val();
        if(countyData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先都道府県を入力してください。",
                icon: "info"
            });
        }
        // 発送先郵便番号
        let postcodeData = $("#postcode").val();
        if(postcodeData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先郵便番号を入力してください。",
                icon: "info"
            });
        }
        // 発送先市区町村
        let townData = $("#town").val();
        if(townData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先市区町村を入力してください。",
                icon: "info"
            });
        }
        // 発送先住所
        let addressData = $("#address").val();
        if(addressData.length == 0){
            return swal({
                title: "まだまだ！",
                text: "発送先住所を入力してください。",
                icon: "info"
            });
        }
        // 発送先ビル名
        let buildingData;
        if($("#building").length > 0){
            buildingData = $("#building").val();
        }
        // 振込銀行選択
        let bankIdData = selectedBank.val();
        if(bankIdData == undefined){
            return swal({
                title: "まだまだ！",
                text: "振込銀行選択を入力してください。",
                icon: "info"
            });
        }
        // 商品名
        let itemNameData = $("#product");
        // 商品数量
        let quantityData = $("#quantity");
        // 商品単価
        let unitPriceData = $("#unitPrice");
        // 請求内容モデルの配列を定義する
        let chargeContent = [];
        for(let i = 0; i < itemNameData.length; i++){
            if(itemNameData[i].value.length == 0 || quantityData[i].value.length == 0 || unitPriceData[i].value.length == 0){
                return swal({
                    title: "まだまだ！",
                    text: "請求内容を入力してください。",
                    icon: "info"
                });
            }
            // 1つずつを配列に追加する
            let itemData = {
                invoiceNo: invoiceNoData,
                itemName: itemNameData[i].value.split("|")[1],
                quantity: quantityData[i].value,
                unitPrice: unitPriceData[i].value,
                creator: "admin"
            }
            chargeContent.push(itemData);
        }
        // 税率
        let taxRate = tax.val();
        // 決済期限
        if($("#year").val() == undefined || $("#month").val() == undefined || $("#date").val() == undefined){
            return swal({
                title: "まだまだ！",
                text: "決済期限を再確認してください。",
                icon: "info"
            });
        }
        let timeZoneOptions = {timeZone: 'Asia/Tokyo', year: 'numeric', month: '2-digit', day: '2-digit'};
        let jpCurrentTime = new Intl.DateTimeFormat('ja-JP', timeZoneOptions).format(new Date());
        let timeArr = jpCurrentTime.split("/");
        let currentTime = parseInt(timeArr[0]+timeArr[1]+timeArr[2]);
        let paymentDueInput = parseInt($("#year").val() + $("#month").val() + $("#date").val());
        if(currentTime > paymentDueInput){
            swal({
                title: "よろしいですか？",
                text: `決済期限は過去の時間となっております。\n編集する場合、「いいえ」を押してください。`,
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
                    let paymentDue = $("#year").val() + '-' + $("#month").val() + '-' + $("#date").val();
                    // メモを取得する
                    let memoData = $("#memo").val();
                    // 保存reqのオブジェクトを定義する
                    let req = {
                        invoice: {
                            invoiceNo: invoiceNoData,
                            corpName: corpNameData,
                            receiver: receiverData,
                            postcode: postcodeData,
                            county: countyData,
                            town: townData,
                            address: addressData,
                            building: buildingData,
                            topicName: topicNameData,
                            bankId: bankIdData,
                            tax: taxRate,
                            memo: memoData,
                            dueDate: paymentDue,
                            chargeDate: paymentDue,
                            updater: "admin"
                        },
                        products: chargeContent
                    }
                    // 請求書編集する
                    $.ajax({
                        url: '/manager/invoice/update_invoice',
                        type: 'POST',
                        data: JSON.stringify(req),
                        contentType: "application/json",
                        success: function(data){
                            // 成功の場合、メッセージを表示してホームページにリダイレクト
                            if(data.code == 1008){
                                swal({
                                    title: "ナイス！",
                                    text: "請求書編集しました！",
                                    icon: "success"
                                })
                                .then((res) => {
                                    if(res == true){
                                        window.location.href = '/manager/invoice/invoice_list';
                                    }
                                })
                            } else {
                                // エラーの場合はメッセージを表示する
                                swal({
                                    title: "残念です！",
                                    text: `${data.message}!`,
                                    icon: "error"
                                })
                            }
                        },
                        error: function(){
                            // エラーの場合はメッセージを表示する
                            swal({
                                title: "残念です！",
                                text: "エラーが発生しました...\nホームページに戻ってやり直してください。",
                                icon: "error"
                            })
                        }
                    })
                }
            })
        }
    })

    // 取消ボタンのクリックイベント処理
    $(".cancelBtn").click(function(){
        // アラートで再確認する
        swal({
            title: "よろしいですか？",
            text: "編集した内容は保存されませんが、よろしいでしょうか？",
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
                // 請求書リストページへ戻る
                window.location.href = "/manager/invoice/invoice_list";
            }
        })
    })
})