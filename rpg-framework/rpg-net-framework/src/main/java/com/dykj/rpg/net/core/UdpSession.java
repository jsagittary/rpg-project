package com.dykj.rpg.net.core;

import com.dykj.rpg.net.kcp.GameServerPrx;
import com.dykj.rpg.net.kcp.Kcp;
import com.dykj.rpg.net.kcp.KcpHandler;
import com.dykj.rpg.net.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public class UdpSession extends AbstractSession{
    public int sessionId;
    public int playerId;
    public GameServerPrx gameServerPrx;
    /**
     * 客户端IP
     */
    public InetSocketAddress remoteAddress;
    /**
     * 本地IP
     */
    public InetSocketAddress localAddress;

    public Channel ioSession;

    public long refreshTime;

    public KcpHandler kcpHandler;


    public UdpSession(int sessionId,int playerId,GameServerPrx gameServerPrx){
        super(null);
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.gameServerPrx = gameServerPrx;
    }

    public void createKcp(int conv){
        kcpHandler = new KcpHandler(this,conv);
    }

    public void input(ByteBuf data){
        if(isActive()){
            kcpHandler.input(data);
        }
    }

    public void releaseKcp(){
        kcpHandler.release();
        kcpHandler = null;
    }

    public void resetKcp(){
        if(this.kcpHandler != null){
            kcpHandler.reset();
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isActive() {
        if(kcpHandler != null && kcpHandler.isActive()){
            return true;
        }
        return false;
    }

    public boolean isWait(){
        if(kcpHandler != null && kcpHandler.isWait()){
            return true;
        }
        return false;
    }

    public void startKcp(){
        if(kcpHandler != null && kcpHandler.isWait()){
            kcpHandler.startKcp();
        }
    }

    @Override
    public boolean write(Object message) {
        if(isActive() && message instanceof Protocol){
            try{
                //System.out.println("--------------write---------------");
                //System.out.println("write msg code = "+((Protocol) message).getCode()+"  size = "+bytes.length);
                kcpHandler.output((Protocol)message);

                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }
}
