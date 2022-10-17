package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipPosUpRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1308;
	//装备栏位置
	private int pos;
	public EquipPosUpRq(){
	}
	public EquipPosUpRq(int pos){
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
		return "EquipPosUpRq [code=" + code +",pos=" + pos +"]";
	}
}
