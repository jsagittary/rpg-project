package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipDownRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1306;
	//卸载哪个位置的装备0~9
	private int equipPos;
	public EquipDownRq(){
	}
	public EquipDownRq(int equipPos){
		this.equipPos = equipPos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.equipPos = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getEquipPos(){
		return this.equipPos;
	}
	public void setEquipPos(int _equipPos){
		this.equipPos = _equipPos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		equipPos = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(equipPos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipDownRq [code=" + code +",equipPos=" + equipPos +"]";
	}
}
