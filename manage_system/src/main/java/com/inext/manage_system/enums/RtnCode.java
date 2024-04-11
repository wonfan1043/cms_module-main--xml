package com.inext.manage_system.enums;

public enum RtnCode {
    SUCCESS(200, "成功"),
	ERROR(400,"エラーが発生しました"),
	PARAM_ERROR(400, "入力内容を再確認してください"),
	DATA_TOO_BIG(400, "長さを制限に超えました"),
    INVOICE_ALREADY_EXISTS(400, "請求書がすでに存在しています"),
	INVOICE_NOT_FOUND(400,"請求書が見つかりませんでした"),
	SAMPLE_ALREADY_EXISTS(400, "サンプルがすでに存在しています"),
	CREATE_INVOICE_FIRST(400, "先に請求書を作成してください"),
	SAMPLE_NOT_FOUND(400,"サンプルが見つかりませんでした"),
	TOPIC_ALREADY_EXISTS(400, "主旨がすでに存在しています"),
	TOPIC_NOT_FOUND(400,"主旨が見つかりませんでした");

    private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
