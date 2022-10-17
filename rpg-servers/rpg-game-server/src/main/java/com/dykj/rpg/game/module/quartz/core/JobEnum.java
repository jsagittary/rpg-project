package com.dykj.rpg.game.module.quartz.core;

public enum JobEnum {
    /**
     * 测试Job
     */
    TEST_CORN("test_corn", "gameServer"),


    /**
     * 测试Job
     */
    TEST_REPEAT("TEST_REPEAT", "gameServer"),

    /**
     * 日常刷新
     */
    DAILY_TASK_REFRESH("DAILY_TASK_REFRESH", "gameServer"),

    /**
     * 周常刷新
     */
    WEEK_TASK_REFRESH("WEEK_TASK_REFRESH", "gameServer");

    private String name;

    private String group;


    JobEnum(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
