package com.test.Enum;

public enum ResultEnum {
    UNKNOWN_ERROR(-1,"未知错误"),
    SUCCESS(200,"成功"),
    NOT_INT(1,"非正整数"),
    SEX_ERROR(2,"性别错误"),
    STUDENTID_ERROR1(3,"学号非整数"),
    STUDENTID_ERROR2(4,"学号非7位");
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
