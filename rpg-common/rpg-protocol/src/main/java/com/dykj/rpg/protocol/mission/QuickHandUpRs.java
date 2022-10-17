package com.dykj.rpg.protocol.mission;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class QuickHandUpRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1508;
	//快速挂机的次数，为0的时候表示一次快速挂没有使用。客户端显示免费（第一次免费）
	private int quickHangUpNum;
	//快速挂机重置的时间搓，客户端做倒计时用
	private long quickHangUpTime;
	public QuickHandUpRs(){
	}
	public QuickHandUpRs(int quickHangUpNum,long quickHangUpTime){
		this.quickHangUpNum = quickHangUpNum;
		this.quickHangUpTime = quickHangUpTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.quickHangUpNum = 0;
		this.quickHangUpTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getQuickHangUpNum(){
		return this.quickHangUpNum;
	}
	public void setQuickHangUpNum(int _quickHangUpNum){
		this.quickHangUpNum = _quickHangUpNum;
	}
	public long getQuickHangUpTime(){
		return this.quickHangUpTime;
	}
	public void setQuickHangUpTime(long _quickHangUpTime){
		this.quickHangUpTime = _quickHangUpTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		quickHangUpNum = (int)dch.bytesToParam("Integer");
		quickHangUpTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(quickHangUpNum)){
			return false;
		}
		if(!ech.paramToBytes(quickHangUpTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "QuickHandUpRs [code=" + code +",quickHangUpNum=" + quickHangUpNum +",quickHangUpTime=" + quickHangUpTime +"]";
	}
}
