package com.dykj.rpg.net.netty.codex;

import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: jyb
 * @Date: 2018/12/22 16:54
 * @Description:
 */
public class MessageEncoder extends MessageToByteEncoder<CmdMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CmdMsg msg, ByteBuf out) throws Exception {
        if (msg.getBody() != null) {
            out.writeShort(ProtocolConstants.NO_ENCRYPTION);
            out.writeShort(msg.getCmd());
            out.writeInt(msg.getBody().length);
            out.writeBytes(msg.getBody());
            //logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCommandId()) + ";length =" + msg.getBody().length);
        } else {
            out.writeShort(ProtocolConstants.NO_ENCRYPTION);
            out.writeShort(msg.getCmd());
            out.writeInt(0);
            //logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCommandId()) + ";length =" + 0);
        }
    }
}
