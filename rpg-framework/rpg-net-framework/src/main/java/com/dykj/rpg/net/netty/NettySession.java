package com.dykj.rpg.net.netty;

import com.dykj.rpg.net.core.AbstractSession;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2018/12/22 14:44
 * @Description:
 */
public class NettySession extends AbstractSession {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public NettySession(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    @Override
    public void sessionClosed() {
        super.sessionClosed();
    }

    @Override
    public boolean write(Object message) {
        if (message instanceof Protocol) {
            Protocol msg = (Protocol) message;
            BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
            msg.encode(bitArray);
            channel.writeAndFlush(bitArray.getWriteByteArray());
            bitArray.release();
        }
        return true;
    }

    @Override
    public boolean write(byte[] message) {
        if (message != null && message.length > 0) {
            channel.writeAndFlush(message);
        } else {
            logger.error("NettySession error : message is null");
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (sessionHolder != null) {
            buffer.append("holderId[").append(sessionHolder.getHolderId()).append("]");
        }
        buffer.append(channel.toString());
        return buffer.toString();
    }
}
