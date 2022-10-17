//package com.dykj.rpg.client;
//
//import com.dykj.rpg.net.kcp.Kcp;
//import com.dykj.rpg.net.kcp.KcpClient;
//import io.netty.buffer.ByteBuf;
//
//public class UdpClient extends KcpClient {
//
//    private static UdpClient instance;
//
//    private static final int PORT = 12345;
//
//    private UdpClient(int port) {
//        super(port);
//    }
//
//    public static UdpClient getInstance() {
//        if(instance == null){
//            instance = new UdpClient(PORT);
//        }
//        return instance;
//    }
//
//    @Override
//    public void handleReceive(ByteBuf bb, Kcp kcp) {
//
//    }
//
//    @Override
//    public void handleException(Throwable ex, Kcp kcp) {
//
//    }
//}
