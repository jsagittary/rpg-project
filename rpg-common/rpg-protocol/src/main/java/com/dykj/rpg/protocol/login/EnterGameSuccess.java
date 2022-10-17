package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EnterGameSuccess extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1110;
	//1 登录 2 重连 3 注册
	private int type;
	public EnterGameSuccess(){
	}
	public EnterGameSuccess(int type){
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
		return "EnterGameSuccess [code=" + code +",type=" + type +"]";
	}
}
