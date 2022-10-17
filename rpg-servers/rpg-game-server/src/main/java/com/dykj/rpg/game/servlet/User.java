package com.dykj.rpg.game.servlet;

/**
 * @Author: jyb
 * @Date: 2018/12/27 16:33
 * @Description:
 */
public class User {
    private int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(int id) {
        this.id = id;
    }

    public User() {
    }
}
