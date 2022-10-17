package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiOffRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1904;
	//卸载哪个位置的雕纹1~6
	private int pos;
	public AiOffRq(){
	}
	public AiOffRq(int pos){
		this.pos = pos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.pos = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPos(){
		return this.pos;
	}
	public void setPos(int _pos){
		this.pos = _pos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		pos = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(pos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiOffRq [code=" + code +",pos=" + pos +"]";
	}
}
