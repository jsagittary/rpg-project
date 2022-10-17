package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillUpRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1403;
	//技能id
	private int skillId;
	//穿戴的位置
	private int position;
	public SkillUpRq(){
	}
	public SkillUpRq(int skillId,int position){
		this.skillId = skillId;
		this.position = position;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.position = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getSkillId(){
		return this.skillId;
	}
	public void setSkillId(int _skillId){
		this.skillId = _skillId;
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
		skillId = (int)dch.bytesToParam("Integer");
		position = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillUpRq [code=" + code +",skillId=" + skillId +",position=" + position +"]";
	}
}
