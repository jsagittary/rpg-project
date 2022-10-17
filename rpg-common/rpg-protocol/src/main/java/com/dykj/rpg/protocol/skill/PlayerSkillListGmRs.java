package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerSkillListGmRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1412;
	private List<com.dykj.rpg.protocol.skill.SkillRs> skills;
	public PlayerSkillListGmRs(){
		this.skills = new ArrayList<>();
	}
	public PlayerSkillListGmRs(List<com.dykj.rpg.protocol.skill.SkillRs> skills){
		this.skills = skills;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.skill.SkillRs value : skills){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skills.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.skill.SkillRs> getSkills(){
		return this.skills;
	}
	public void setSkills(List<com.dykj.rpg.protocol.skill.SkillRs> _skills){
		this.skills = _skills;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skills = (List<com.dykj.rpg.protocol.skill.SkillRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.SkillRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(skills)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerSkillListGmRs [code=" + code +",skills=" + skills +"]";
	}
}
