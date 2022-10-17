package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EndTrainSoulSkillRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1417;
	//技能id
	private int skillId;
	public EndTrainSoulSkillRq(){
	}
	public EndTrainSoulSkillRq(int skillId){
		this.skillId = skillId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
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
		return true;
	}
	public String toString() {
		return "EndTrainSoulSkillRq [code=" + code +",skillId=" + skillId +"]";
	}
}
