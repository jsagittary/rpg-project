package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ModifyNameRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1003;
	//玩家名字
	private String name;
	public ModifyNameRq(){
	}
	public ModifyNameRq(String name){
		this.name = name;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.name = null;
	}
	public short getCode(){
		return this.code;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String _name){
		this.name = _name;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		name = (String)dch.bytesToParam("String");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(name)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ModifyNameRq [code=" + code +",name=" + name +"]";
	}
}
