package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillRisingStarListRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1424;
	//技能id
	private int skillId;
	private List<com.dykj.rpg.protocol.skill.SkillRisingStar> skillRisingStar;
	public SkillRisingStarListRq(){
		this.skillRisingStar = new ArrayList<>();
	}
	public SkillRisingStarListRq(int skillId,List<com.dykj.rpg.protocol.skill.SkillRisingStar> skillRisingStar){
		this.skillId = skillId;
		this.skillRisingStar = skillRisingStar;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		for(com.dykj.rpg.protocol.skill.SkillRisingStar value : skillRisingStar){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillRisingStar.clear();
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
	public List<com.dykj.rpg.protocol.skill.SkillRisingStar> getSkillRisingStar(){
		return this.skillRisingStar;
	}
	public void setSkillRisingStar(List<com.dykj.rpg.protocol.skill.SkillRisingStar> _skillRisingStar){
		this.skillRisingStar = _skillRisingStar;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		skillRisingStar = (List<com.dykj.rpg.protocol.skill.SkillRisingStar>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.SkillRisingStar>");
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
		if(!ech.paramToBytes(skillRisingStar)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillRisingStarListRq [code=" + code +",skillId=" + skillId +",skillRisingStar=" + skillRisingStar +"]";
	}
}
