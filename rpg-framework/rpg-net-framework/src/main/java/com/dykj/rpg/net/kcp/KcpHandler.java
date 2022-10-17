/**
 * 维护kcp状态的线程
 */
package com.dykj.rpg.net.kcp;

import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.ClientHandler;
import com.dykj.rpg.net.handler.core.ClientHandlerManager;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.util.spring.BeanFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

/**
 *
 * @author beykery
 */
public class KcpHandler implements KcpListerner{

	private RingBuffer<ByteBuf> inputs;
	private RingBuffer<BitArray> outputs;
	private boolean needRelease;
	private boolean needReset;
	private final InetSocketAddress local;// 本地地址
	private Kcp kcp;

	public KcpHandler(UdpSession session,int conv) {
		kcp = new Kcp(this,session);
		kcp.setConv(conv);
		inputs = new RingBuffer<>(100);
		outputs = new RingBuffer<>(100);
		needRelease = false;
		this.local = session.localAddress;
	}

	public void handler() {
			long st = System.currentTimeMillis();
			//release
			if(needRelease){
				needRelease = false;
				kcp.release();
				while (this.outputs.size()>0) {
					this.outputs.poll().release();
				}
				while (this.inputs.size()>0) {
					this.inputs.poll().release();
				}

				return;
			}
			//reset
			if(needReset){
				needReset = false;
				kcp.initKcp();
				while (this.outputs.size()>0) {
					this.outputs.poll().release();
				}
				while (this.inputs.size()>0) {
					this.inputs.poll().release();
				}

				return;
			}
			//output
			while (this.outputs.size()>0) {
				kcp.send(this.outputs.poll());
			}
			// input
			while (this.inputs.size()>0) {
				kcp.input(this.inputs.poll());
			}

			kcp.update(System.currentTimeMillis());
	}

	/**
	 * 收到输入
	 */
	public void input(ByteBuf bb) {
		this.inputs.add(bb);
	}

	public void output(ByteBuf bb) {
		//System.out.println("kcp handler add output msg !!!");
		//this.outputs.add(bb);
	}

	public void output(Protocol protocol) {
		//System.out.println("kcp handler add output msg !!!");
		BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
		protocol.encode(bitArray);
		this.outputs.add(bitArray);
	}

	public void release(){
		this.needRelease = true;
		this.handler();
	}

	public void reset(){
		this.needReset = true;
		this.handler();
	}

	public boolean isActive(){
		return kcp.getState() == 1;
	}

	public boolean isWait(){
		return kcp.getState() == 0;
	}

	public void startKcp(){
		kcp.startKcp();
	}

	public int getState(){
		return kcp.getState();
	}

	@Override
	public void handleReceive(ByteBuf bb, Kcp kcp) {
		//short msgLength = bb.readShort();
		bb.readerIndex(0);
		//byte[] body = new byte[bb.readableBytes()];
		int size = bb.readableBytes();
		BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
		bb.readBytes(bitArray.getBytes(),0,size);
		bitArray.setWriteIndex(size);
		short cmd = bitArray.readShort();

		//int cmd = body[1] << 8 & 0xff;
		//cmd |= body[2];

		System.out.println("cmd = "+cmd);
		//cmd = 0 默认为心跳检测
		if(cmd != 0){
			ClientHandler clientHandler = BeanFactory.getBean(ClientHandlerManager.class).getHandler((short)cmd);
			try{
				if(clientHandler != null){
					clientHandler.handler(bitArray.getBytes(), (UdpSession) (kcp.getSession()));
				}else{
					//System.out.println("handler class not found , cmd = "+cmd);
				}
			}catch (Exception e){
				bitArray.release();
				e.printStackTrace();
			}
		}

		bitArray.release();

	}

	@Override
	public void handleException(Throwable ex, Kcp kcp) {

	}

	@Override
	public void handleClose(Kcp kcp) {

	}

	private long preFrameTime = 0;
	@Override
	public void out(ByteBuf msg, UdpSession session) {

//		long currentFrameTime = System.currentTimeMillis();
//		if(preFrameTime == 0){
//			preFrameTime = currentFrameTime;
//		}else{
//			System.out.println("send msg interval time "+(currentFrameTime-preFrameTime)+" size = "+msg.readableBytes());
//			preFrameTime = currentFrameTime;
//		}

		DatagramPacket packet = new DatagramPacket(msg,session.remoteAddress, session.localAddress);
		session.ioSession.writeAndFlush(packet);
	}
}
