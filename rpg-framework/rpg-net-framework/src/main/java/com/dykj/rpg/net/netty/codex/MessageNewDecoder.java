package com.dykj.rpg.net.netty.codex;

import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ProtocolConstants;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.net.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
public class MessageNewDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 10) {
            return;
        }
        in.markReaderIndex();
        int msg_key = in.readInt();
        short encryption = in.readShort();
        int length = in.readInt();
        if (length < 0) {
            in.resetReaderIndex();
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = insocket.getAddress().getHostAddress();
            //logger.error("MessageDecoder msg length is error: length=" + length + " ,cmd = " + cmd + " ip = " + clientIP);
            return;
        }
        if (length > in.readableBytes()) {
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = insocket.getAddress().getHostAddress();
            //logger.error("MessageDecoder msg length is error: length< maxLength ip =" + clientIP + "[" + cmd + " ;" + length + ";" + in.readableBytes() + "]");
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
        bitArray.initBytes(bytes,bytes.length);
        short cmd = bitArray.readShort();
        bitArray.release();

//        System.out.println("cmd = " + cmd);
//        if (cmd == 1103) {
//            LoginMsgRq   loginMsgRq =(LoginMsgRq) Serializer.deserialize(bytes,LoginMsgRq.class);
//            System.out.println(loginMsgRq.toString());
//        }
        out.add(new CmdMsg(cmd, bytes));

    }

}
