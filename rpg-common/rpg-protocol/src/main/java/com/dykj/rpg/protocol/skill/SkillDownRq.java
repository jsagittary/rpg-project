package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillDownRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1405;
	//卸载哪个位置的技能1~9
	private int position;
	public SkillDownRq(){
	}
	public SkillDownRq(int position){
		this.position = position;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.position = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPosition(){
		return this.position;
	}
	public void setPosition(int _position){
		this.position = _position;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		position = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillDownRq [code=" + code +",position=" + position +"]";
	}
}
