package com.dykj.rpg.net.netty;

import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.core.UdpSessionManager;
import com.dykj.rpg.net.kcp.Kcp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.util.logging.Logger;

public class UdpGameServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(UdpGameServerHandler.class.getName());

    private static byte[] heartHeadResponseBytes;

    private NettyServer nettyServer;

    private boolean testMode = true;

    public UdpGameServerHandler(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("请求连接成功 " + ctx.channel().remoteAddress());

        super.channelActive(ctx);
    }

    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DatagramPacket) {
            DatagramPacket data = (DatagramPacket) msg;

            ByteBuf byteBuf = data.content();

            try {

                if (byteBuf.readableBytes() < Kcp.IKCP_OVERHEAD) {
                    logger.info("接收的数据长度 [ " + byteBuf.readableBytes() + " ] 小于kcp协议头长度 [ " + Kcp.IKCP_OVERHEAD + " ]");
                    return;
                }

                clientRequestHandler(ctx,data,byteBuf);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void clientRequestHandler(ChannelHandlerContext ctx,DatagramPacket data,ByteBuf byteBuf){
        try{
            int conv = byteBuf.readInt();

            UdpSession session = UdpSessionManager.getInstance().getSession(conv);
            if (session == null) {
                if(testMode){
                    UdpSessionManager.getInstance().register(conv,conv,null);
                    session = UdpSessionManager.getInstance().getSession(conv);
                }else{
                    logger.info("请先进行登录操作 !!!");

                    ByteBuf msg = Unpooled.buffer(1);
                    msg.writeByte(0);
                    DatagramPacket packet = new DatagramPacket(msg,data.sender(), data.recipient());
                    ctx.writeAndFlush(packet);
                    return;
                }

            }

            if(session.kcpHandler == null){
                session.createKcp(conv);
            }

            byte cmd = byteBuf.readByte();
            short frg = byteBuf.readByte();
            int wnd = byteBuf.readShort();
            long ts = byteBuf.readInt();
            long sn = byteBuf.readInt();
            long una = byteBuf.readInt();
            int len = byteBuf.readInt();

            session.remoteAddress = data.sender();
            session.localAddress = data.recipient();
            session.refreshTime = System.currentTimeMillis();
            session.ioSession = ctx.channel();

           // System.out.println("kcp[cmd="+cmd+",frg="+frg+",wnd="+wnd+",ts="+ts+",sn="+sn+",una="+una+",len="+len+"]");

            //kcp重新计数
            if (cmd == Kcp.IKCP_CMD_PUSH) {

                //System.out.println("sn = "+sn);

                if(sn == 0 && !session.isWait()){
                    session.resetKcp();
                }

                if(sn == 0 && session.isWait()){
                    session.startKcp();
                }

                if(sn > 0 && session.isWait()){
                    ByteBuf msg = Unpooled.buffer(1);
                    msg.writeByte(2);
                    DatagramPacket packet = new DatagramPacket(msg,session.remoteAddress, session.localAddress);
                    session.ioSession.writeAndFlush(packet);
                    return;
                }
            }

            byteBuf.readerIndex(0);
            byte[] bytes = new byte[len+Kcp.IKCP_OVERHEAD];
            byteBuf.readBytes(bytes);
            session.input(Unpooled.wrappedBuffer(bytes));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("断开连接成功 " + ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        ctx.channel().close();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        super.channelReadComplete(ctx);
    }

}