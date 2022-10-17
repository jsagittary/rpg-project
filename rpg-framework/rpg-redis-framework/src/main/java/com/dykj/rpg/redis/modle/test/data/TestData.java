package com.dykj.rpg.redis.modle.test.data;

public class TestData {
    private int id ;


    private  String name ;


    public TestData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
