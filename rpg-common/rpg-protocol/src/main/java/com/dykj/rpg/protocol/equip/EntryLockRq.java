package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EntryLockRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1311;
	//服务器唯一id
	private long instId;
	//词条的唯一表示,依据position在装备显示面板从上到下依次排序
	private int position;
	public EntryLockRq(){
	}
	public EntryLockRq(long instId,int position){
		this.instId = instId;
		this.position = position;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
		this.position = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getInstId(){
		return this.instId;
	}
	public void setInstId(long _instId){
		this.instId = _instId;
	}
	public int getPosition(){
		return this.position;
	}
	public void setPosition(int _position){
		this.position = _position;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
		position = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(instId)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EntryLockRq [code=" + code +",instId=" + instId +",position=" + position +"]";
	}
}
