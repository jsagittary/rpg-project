package com.dykj.rpg.protocol.mail;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RemoveMailRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2007;
	//服务器唯一id
	private long instId;
	public RemoveMailRq(){
	}
	public RemoveMailRq(long instId){
		this.instId = instId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
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
		return true;
	}
	public String toString() {
		return "RemoveMailRq [code=" + code +",instId=" + instId +"]";
	}
}
