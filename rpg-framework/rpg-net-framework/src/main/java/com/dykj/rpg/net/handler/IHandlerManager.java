package com.dykj.rpg.net.handler;

/**
 * @Author: jyb
 * @Date: 2018/12/24 18:33
 * @Description:
 */
public interface IHandlerManager<T> {
    /**
     * 设置扫描的包路径
     *
     * @param packeagePath
     */
    void setPackagePath(String packeagePath);

    /**
     * 初始化所以的handler
     */
    void initHandler();

    /**
     * 拿到指令集
     * @param cmd
     * @return
     */
    T  getHandler(short cmd);

}
