package com.dykj.rpg.net.kcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.Recycler;

/**
 * SEGMENT
 */
public class Segment
{

    public int conv = 0;
    public byte cmd = 0;
    public int frg = 0;
    public int wnd = 0;
    public int ts = 0;
    public int sn = 0;
    public int una = 0;
    public int resendts = 0;
    public int rto = 0;
    public int fastack = 0;
    public int xmit = 0;
    public ByteBuf data;

    private Recycler.Handle<Segment> recyclerHandle;

    private static final Recycler<Segment> RECYCLER = new Recycler<Segment>() {
        @Override
        protected Segment newObject(Handle<Segment> handle) {
            return new Segment(handle);
        }
    };

    private Segment(Recycler.Handle<Segment> handle){
        this.recyclerHandle = handle;
    }

    //获取新的对象
    public static Segment createSegment(ByteBuf byteBuf){
        Segment seg = RECYCLER.get();
        seg.data = byteBuf;
        return seg;
    }

    public static Segment createSegment(){
        Segment seg = RECYCLER.get();
        seg.data = null;
        return seg;
    }

    //释放旧的对象
    public void recycle(){
        conv = 0;
        cmd = 0;
        frg = 0;
        wnd = 0;
        ts = 0;
        sn = 0;
        una = 0;
        resendts = 0;
        rto = 0;
        fastack = 0;
        xmit = 0;
        if(data != null){
            data.release();
            data = null;
        }
        recyclerHandle.recycle(this);
    }

    private Segment(int size)
    {
        if (size > 0)
        {
            this.data = PooledByteBufAllocator.DEFAULT.buffer(size);
        }
    }

    /**
     * encode a segment into buffer
     *
     * @param buf
     * @return
     */
    public int encode(ByteBuf buf)
    {
        int off = buf.writerIndex();
        buf.writeInt(conv);
        buf.writeByte(cmd);
        buf.writeByte(frg);
        buf.writeShort(wnd);
        buf.writeInt(ts);
        buf.writeInt(sn);
        buf.writeInt(una);
        buf.writeInt(data == null ? 0 : data.readableBytes());
        return buf.writerIndex() - off;
    }

    public void decode(ByteBuf buf)
    {
        buf.readerIndex(0);
        conv = buf.readInt();
        cmd = buf.readByte();
        frg = buf.readByte();
        wnd = buf.readShort();
        ts = buf.readInt();
        sn = buf.readInt();
        una = buf.readInt();
        int dataLen = buf.readInt();
        data = Unpooled.buffer(dataLen);
    }

    /**
     * 释放内存
     */
    private void release()
    {
        if (this.data != null && data.refCnt() > 0)
        {
            this.data.release(data.refCnt());
        }
    }

    public String toString(){
        return "Segment [conv="+conv+",cmd="+cmd+",frg="+frg+",wnd="+wnd+",ts="+ts+",sn="+sn+",una="+una+",dataLen="+(data == null?0:data.readableBytes())+",resendts="+resendts+",rto="+rto+",fastack="+fastack+",xmit="+xmit+"]";
    }
}
