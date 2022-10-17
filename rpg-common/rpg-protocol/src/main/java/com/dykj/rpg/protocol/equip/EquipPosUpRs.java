package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipPosUpRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1309;
	//装备栏位置
	private int pos;
	//装备栏等级
	private int posLv;
	public EquipPosUpRs(){
	}
	public EquipPosUpRs(int pos,int posLv){
		this.pos = pos;
		this.posLv = posLv;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.pos = 0;
		this.posLv = 0;
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
	public int getPosLv(){
		return this.posLv;
	}
	public void setPosLv(int _posLv){
		this.posLv = _posLv;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		pos = (int)dch.bytesToParam("Integer");
		posLv = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(posLv)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipPosUpRs [code=" + code +",pos=" + pos +",posLv=" + posLv +"]";
	}
}
