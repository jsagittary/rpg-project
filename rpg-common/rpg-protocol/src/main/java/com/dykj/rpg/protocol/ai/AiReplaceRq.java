package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiReplaceRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1906;
	//当前位置
	private int pos1;
	//目标位置
	private int pos2;
	public AiReplaceRq(){
	}
	public AiReplaceRq(int pos1,int pos2){
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.pos1 = 0;
		this.pos2 = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPos1(){
		return this.pos1;
	}
	public void setPos1(int _pos1){
		this.pos1 = _pos1;
	}
	public int getPos2(){
		return this.pos2;
	}
	public void setPos2(int _pos2){
		this.pos2 = _pos2;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		pos1 = (int)dch.bytesToParam("Integer");
		pos2 = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(pos1)){
			return false;
		}
		if(!ech.paramToBytes(pos2)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiReplaceRq [code=" + code +",pos1=" + pos1 +",pos2=" + pos2 +"]";
	}
}
