package com.dykj.rpg.protocol.test;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class TestResponse1 extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2;
	//时间戳
	private long time;
	public TestResponse1(){
	}
	public TestResponse1(long time){
		this.time = time;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.time = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getTime(){
		return this.time;
	}
	public void setTime(long _time){
		this.time = _time;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		time = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(time)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "TestResponse1 [code=" + code +",time=" + time +"]";
	}
}
