package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class OfflinePlayerRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1105;
	// 1 您的账户在异地登录 ，2 服务器已维护
	private int type;
	public OfflinePlayerRs(){
	}
	public OfflinePlayerRs(int type){
		this.type = type;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.type = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int _type){
		this.type = _type;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		type = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "OfflinePlayerRs [code=" + code +",type=" + type +"]";
	}
}
