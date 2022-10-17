package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerSkillNewRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1407;
	private com.dykj.rpg.protocol.skill.SkillRs skill;
	public PlayerSkillNewRs(){
	}
	public PlayerSkillNewRs(com.dykj.rpg.protocol.skill.SkillRs skill){
		this.skill = skill;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.skill);
		this.skill = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.skill.SkillRs getSkill(){
		return this.skill;
	}
	public void setSkill(com.dykj.rpg.protocol.skill.SkillRs _skill){
		this.skill = _skill;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skill = (com.dykj.rpg.protocol.skill.SkillRs)dch.bytesToParam("com.dykj.rpg.protocol.skill.SkillRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(skill)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerSkillNewRs [code=" + code +",skill=" + skill +"]";
	}
}
