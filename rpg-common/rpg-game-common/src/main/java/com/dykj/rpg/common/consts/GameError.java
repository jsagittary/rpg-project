package com.dykj.rpg.common.consts;

public enum GameError {
    OK(200, "OK") {
    },
    ;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private GameError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public String toString() {
        return msg + ", code = " + code;
    }
}
