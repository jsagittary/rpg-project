package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillUpgradeListRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1409;
	//技能id
	private int skillId;
	private List<com.dykj.rpg.protocol.skill.SkillUpgradeRq> skillUpgrades;
	public SkillUpgradeListRq(){
		this.skillUpgrades = new ArrayList<>();
	}
	public SkillUpgradeListRq(int skillId,List<com.dykj.rpg.protocol.skill.SkillUpgradeRq> skillUpgrades){
		this.skillId = skillId;
		this.skillUpgrades = skillUpgrades;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		for(com.dykj.rpg.protocol.skill.SkillUpgradeRq value : skillUpgrades){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillUpgrades.clear();
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
	public List<com.dykj.rpg.protocol.skill.SkillUpgradeRq> getSkillUpgrades(){
		return this.skillUpgrades;
	}
	public void setSkillUpgrades(List<com.dykj.rpg.protocol.skill.SkillUpgradeRq> _skillUpgrades){
		this.skillUpgrades = _skillUpgrades;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		skillUpgrades = (List<com.dykj.rpg.protocol.skill.SkillUpgradeRq>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.SkillUpgradeRq>");
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
		if(!ech.paramToBytes(skillUpgrades)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillUpgradeListRq [code=" + code +",skillId=" + skillId +",skillUpgrades=" + skillUpgrades +"]";
	}
}
