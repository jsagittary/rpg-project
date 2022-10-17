package com.dykj.rpg.common.module.uc.logic;

/**
 * @Author: jyb
 * @Date: 2020/10/10 17:59
 * @Description:
 */
public class LoginMsg {
    /**
     * 账号
     */
    private int accountKey;
    /**
     * ip:port
     */
    private  String address;

    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }



    public LoginMsg(int accountKey, String address) {
        this.accountKey = accountKey;
        this.address = address;
    }

    public LoginMsg() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
