package com.dykj.rpg.client;

import com.dykj.rpg.client.handler.GameClientHandler;
import com.dykj.rpg.client.netty.MessageDecoder;
import com.dykj.rpg.client.netty.MessageEncoder;

import com.dykj.rpg.client.netty.codex.MessageNewDecoder;
import com.dykj.rpg.client.netty.codex.MessageNewEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:56
 * @Description:
 */
public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 游戏指令解码器
//        pipeline.addLast("decoder", new MessageDecoder());
//        pipeline.addLast("encoder", new MessageEncoder());


        pipeline.addLast("decoder", new MessageNewDecoder());
        pipeline.addLast("encoder", new MessageNewEncoder());

        // 自己的逻辑Handler
        pipeline.addLast("handler", new GameClientHandler());

    }
}
