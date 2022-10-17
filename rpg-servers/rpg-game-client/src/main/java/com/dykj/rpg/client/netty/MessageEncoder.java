package com.dykj.rpg.client.netty;


import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:51
 * @Description:
 */
public class MessageEncoder extends MessageToByteEncoder<CmdMsg> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void encode(ChannelHandlerContext ctx, CmdMsg msg, ByteBuf out) throws Exception {
        if (msg.getBytes() != null) {
            out.writeShort(ProtocolConstants.NO_ENCRYPTION);
            out.writeShort(msg.getCmd());
            out.writeInt(msg.getBytes().length);
            out.writeBytes(msg.getBytes());
           // logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCmd()) + ";length =" + msg.getBytes().length);
        } else {
            out.writeShort(ProtocolConstants.NO_ENCRYPTION);
            out.writeShort(msg.getCmd());
            out.writeInt(0);
           // logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCmd()) + ";length =" + 0);
        }
    }
}
