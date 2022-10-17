package com.dykj.rpg.db.dao;

/**
 * @Author: jyb
 * @Date: 2020/9/23 20:52
 * @Description:
 */
public class DBKafkaMsg {
    /***
     * queue的全路径
     */
    private String queueName;

    /**
     * modle的全路径
     */
    private String modelName;

    /**
     * 要入库的数据
     */
    private String data;

    /**
     * 操作
     */
    private int Operation;

    /**
     * 要同步的缓存或者数据model是几个主键
     */
    private int keyNum =1;


    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }





    public int getOperation() {
        return Operation;
    }

    public void setOperation(int operation) {
        Operation = operation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DBKafkaMsg() {
    }

    public int getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(int keyNum) {
        this.keyNum = keyNum;
    }

    @Override
    public String toString() {
        return "DBKafkaMsg{" +
                "queueName='" + queueName + '\'' +
                ", modelName='" + modelName + '\'' +
                ", data='" + data + '\'' +
                ", Operation=" + Operation +
                ", keyNum=" + keyNum +
                '}';
    }
}
