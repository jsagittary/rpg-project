package com.dykj.rpg.net.netty.codex;

import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: jyb
 * @Date: 2018/12/22 16:54
 * @Description:
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 8) {
            return;
        }
        in.markReaderIndex();
        short encryption = in.readShort();
        short cmd = in.readShort();
        int length = in.readInt();
        if (length < 0) {
            in.resetReaderIndex();
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = insocket.getAddress().getHostAddress();
            logger.error("MessageDecoder msg length is error: length=" + length + " ,cmd = " + cmd + " ip = " + clientIP);
            return;
        }
        if (length > in.readableBytes()) {
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = insocket.getAddress().getHostAddress();
            logger.error("MessageDecoder msg length is error: length< maxLength ip =" + clientIP + "[" + cmd + " ;" + length + ";" + in.readableBytes() + "]");
            in.resetReaderIndex();
            return;
        }
        // 不加密
        if (encryption == ProtocolConstants.NO_ENCRYPTION) {
            if (length == 0) {
                out.add(new CmdMsg(cmd));
            } else {
                byte[] bytes = new byte[length];
                in.readBytes(bytes); // 读取内容
                out.add(new CmdMsg(cmd, bytes));
            }
        }
    }
}
