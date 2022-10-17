package com.dykj.rpg.net.netty.codex;

import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import com.dykj.rpg.net.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2018/12/22 16:54
 * @Description:
 */
public class MessageNewEncoder extends MessageToByteEncoder<byte[]> {

    private Logger logger = LoggerFactory.getLogger("game");
    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] body, ByteBuf out) throws Exception {
        out.writeInt(ProtocolConstants.MSG_KEY);
        out.writeShort(ProtocolConstants.NO_ENCRYPTION);
       // System.out.println(msg.toString());
        try {
            //byte[] body = msg.encode();
            out.writeInt(body.length);
            out.writeBytes(body);
          //  logger.info("s--------->c:cmd:" + msg.getCode() + ";length =" + body.length);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
