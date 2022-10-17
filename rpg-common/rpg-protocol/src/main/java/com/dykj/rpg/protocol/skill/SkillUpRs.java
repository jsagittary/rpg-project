package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillUpRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1404;
	//穿上的技能信息
	private com.dykj.rpg.protocol.skill.SkillRs upSkill;
	//卸下的技能信息
	private com.dykj.rpg.protocol.skill.SkillRs downSkill;
	public SkillUpRs(){
	}
	public SkillUpRs(com.dykj.rpg.protocol.skill.SkillRs upSkill,com.dykj.rpg.protocol.skill.SkillRs downSkill){
		this.upSkill = upSkill;
		this.downSkill = downSkill;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.upSkill);
		this.upSkill = null;
		ProtocolPool.getInstance().restoreProtocol(this.downSkill);
		this.downSkill = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.skill.SkillRs getUpSkill(){
		return this.upSkill;
	}
	public void setUpSkill(com.dykj.rpg.protocol.skill.SkillRs _upSkill){
		this.upSkill = _upSkill;
	}
	public com.dykj.rpg.protocol.skill.SkillRs getDownSkill(){
		return this.downSkill;
	}
	public void setDownSkill(com.dykj.rpg.protocol.skill.SkillRs _downSkill){
		this.downSkill = _downSkill;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		upSkill = (com.dykj.rpg.protocol.skill.SkillRs)dch.bytesToParam("com.dykj.rpg.protocol.skill.SkillRs");
		downSkill = (com.dykj.rpg.protocol.skill.SkillRs)dch.bytesToParam("com.dykj.rpg.protocol.skill.SkillRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(upSkill)){
			return false;
		}
		if(!ech.paramToBytes(downSkill)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillUpRs [code=" + code +",upSkill=" + upSkill +",downSkill=" + downSkill +"]";
	}
}
