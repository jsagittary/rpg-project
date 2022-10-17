package com.dykj.rpg.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:32
 * @Description:
 */
@Component
public class NettyClientFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private EventLoopGroup group;
    private Bootstrap b;

    public NettyClientFactory() {
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.handler(new GameServerInitializer());
    }


    public Channel createNetty(String host, int port) {
        Channel channel = null;
        try {
            channel = b.connect(host, port).channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
}
