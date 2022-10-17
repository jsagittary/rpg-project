package com.dykj.rpg.game.module.gm.response;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.net.protocol.Protocol;

/**
 * @Description GM指令处理响应
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/4
 */
public class GmResponse
{
    private ErrorCodeEnum codeEnum;//响应码
    private Protocol protocol;

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

    @Override
    public String toString()
    {
        return "GmResponse{" + "codeEnum=" + codeEnum + ", protocol=" + protocol + '}';
    }
}