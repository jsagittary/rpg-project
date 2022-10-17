package com.dykj.rpg.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @Author: jyb
 * @Date: 2018/12/22 17:52
 * @Description:
 */
public class NettyServer {

    private Logger logger = LoggerFactory.getLogger("game");
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private String address = "0.0.0.0";
    private int port = 8888;
    private String type = "tcp";

    private AtomicBoolean start  = new AtomicBoolean(false);


    public NettyServer() {
    }

    public void start() throws Exception {
        new Thread("NettyServer-Thread") {
            @Override
            public void run() {
                if(type.equals("tcp")){
                    createTcpConnection();
                }
                if(type.equals("udp")){
                    createUdpConnection();
                }
            }
        }.start();
    }

    private void createTcpConnection(){
        //System.out.println();
        try {

            // Configure the server.
            if (bossGroup == null)
                bossGroup = new NioEventLoopGroup(1);

            if (workerGroup == null)
                workerGroup = new NioEventLoopGroup();

            try {
                if (bootstrap == null)
                    bootstrap = new ServerBootstrap();

                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 100)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(ChannelOption.AUTO_READ, true)
                        .childOption(ChannelOption.SO_LINGER, 10)
                        .childHandler(new GameServerInitializer());
                ChannelFuture future = null;
                if (address != null && address.length() > 0
                        && !address.equals("127.0.0.1")
                        && !address.equals("0:0:0:0:0:0:0:1")) {
                    future = bootstrap.bind(address, port).sync();
                } else {
                    future = bootstrap.bind(port).sync();
                }
                logger.info("############################################################");
                logger.info("#  nettyServer start --> type[{}] , address [{}] , port [{}] #",type,address,port);
                logger.info("############################################################");
                start.set(true);
                future.channel().closeFuture().sync();
            } finally {
                // Shut down all event loops to terminate all threads.
                if (bossGroup != null)
                    bossGroup.shutdownGracefully();

                if (workerGroup != null)
                    workerGroup.shutdownGracefully();
            }
        } catch (Exception ex) {
            logger.error("server start fail.[{}]:[{}]", address,
                    port, ex);
        }
    }

    private void createUdpConnection(){
        try {
            Bootstrap bootstrap = new Bootstrap();

            boolean epoll = Epoll.isAvailable();

            if (epoll) {

                bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
            }else {
                bootstrap.option(ChannelOption.SO_REUSEADDR, true);
            }

            bootstrap.group(epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup());

            Class<? extends Channel> channelClass = epoll ? EpollDatagramChannel.class : NioDatagramChannel.class;
            bootstrap.channel(channelClass);

            bootstrap.handler(new UdpGameServerHandler(this));

            ChannelFuture future ;
            if (address != null && address.length() > 0
                    && !address.equals("127.0.0.1")
                    && !address.equals("0:0:0:0:0:0:0:1")) {
                future = bootstrap.bind(address, port).sync();
            } else {
                future = bootstrap.bind(port).sync();
                address = InetAddress.getLocalHost().getHostAddress();
            }

//			开通多端口
//			for (int port : ports) {
//	            for (int i = 0; i < bindTimes; i++) {
//	                ChannelFuture channelFuture = bootstrap.bind(port);
//	                Channel channel = channelFuture.channel();
//	                localAddresss.add(channel);
//	            }
//	        }

            logger.info("############################################################");
            logger.info("#  nettyServer start --> type[{}] , address [{}] , port [{}] #",type,address,port);
            logger.info("############################################################");
            start.set(true);

            //NioDatagramChannel channel = (NioDatagramChannel) future.channel();
            //KcpInputPool.getInstance().startKcp(channel.localAddress());

            future.channel().closeFuture().sync();

           // new Timer().scheduleAtFixedRate(new OutputTimerTask(this), 0L,60000L);

        } catch (Exception ex) {
            logger.error("server start fail.[{}]:[{}]", address,
                    port, ex);
        }finally {
            // Shut down all event loops to terminate all threads.
            if (bossGroup != null)
                bossGroup.shutdownGracefully();

            if (workerGroup != null)
                workerGroup.shutdownGracefully();
        }
    }

    public AtomicBoolean getStart() {
        return start;
    }

    public void setStart(AtomicBoolean start) {
        this.start = start;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
