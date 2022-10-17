package com.dykj.rpg.net.netty;

import com.dykj.rpg.net.netty.codex.MessageDecoder;
import com.dykj.rpg.net.netty.codex.MessageEncoder;
import com.dykj.rpg.net.netty.codex.MessageNewDecoder;
import com.dykj.rpg.net.netty.codex.MessageNewEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: jyb
 * @Date: 2018/12/22 17:26
 * @Description:
 */
public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 读操作空闲30秒
     */
    private final int READ_FREE_TIME = 35;
    /**
     * 写操作空闲30秒
     */
    private final int WRITE_FREE_TIME = 35;
    /**
     * 读操作空闲30秒
     */
    private final int READ_WRITE_FREE_TIME = 35;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("timeout", new IdleStateHandler(READ_FREE_TIME, WRITE_FREE_TIME, READ_WRITE_FREE_TIME, TimeUnit.SECONDS));
//        pipeline.addLast("decoder", new MessageDecoder());
//        pipeline.addLast("encoder", new MessageEncoder());


        pipeline.addLast("decoder", new MessageNewDecoder());
        pipeline.addLast("encoder", new MessageNewEncoder());
        pipeline.addLast("handler", new GameServerHandler());

    }
}
