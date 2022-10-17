package com.dykj.rpg.client.netty.codex;

import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import com.dykj.rpg.net.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: jyb
 * @Date: 2018/12/22 16:54
 * @Description:
 */
public class MessageNewEncoder extends MessageToByteEncoder<Protocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, ByteBuf out) throws Exception {
        out.writeInt(ProtocolConstants.MSG_KEY);
        out.writeShort(ProtocolConstants.NO_ENCRYPTION);
        byte[] body = msg.encode();
        out.writeInt(body.length);
        out.writeBytes(body);
        //logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCommandId()) + ";length =" + 0);
    }
}
