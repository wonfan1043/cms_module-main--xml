package com.inext.manage_system.enums;

public enum DateType {

    CREATE_DATE(1, "作成日"),
    CHARGE_DATE(2, "請求日"),
    DUE_DATE(3, "決済期限"),
    PAY_DATE(4, "決済日");

    private final int code;

    private final String type;

    private DateType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

}
