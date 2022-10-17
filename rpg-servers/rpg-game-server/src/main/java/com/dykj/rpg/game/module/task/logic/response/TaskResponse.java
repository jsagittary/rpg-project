package com.dykj.rpg.game.module.task.logic.response;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.net.protocol.Protocol;

/**
 * @Description 任务处理响应
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/8
 */
public class TaskResponse
{
    private ErrorCodeEnum codeEnum;//响应码
    private Protocol protocol;//协议
    private Boolean taskRefresh;//任务是否刷新

    public TaskResponse()
    {
    }

    public TaskResponse(ErrorCodeEnum codeEnum, Protocol protocol)
    {
        this.codeEnum = codeEnum;
        this.protocol = protocol;
    }

    public ErrorCodeEnum getCodeEnum()
    {
        return codeEnum;
    }

    public void setCodeEnum(ErrorCodeEnum codeEnum)
    {
        this.codeEnum = codeEnum;
    }

    public Protocol getProtocol()
    {
        return protocol;
    }

    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }

    public Boolean getTaskRefresh()
    {
        return taskRefresh;
    }

    public void setTaskRefresh(Boolean taskRefresh)
    {
        this.taskRefresh = taskRefresh;
    }

    @Override
    public String toString()
    {
        return "TaskResponse{" + "codeEnum=" + codeEnum + ", protocol=" + protocol + '}';
    }
}