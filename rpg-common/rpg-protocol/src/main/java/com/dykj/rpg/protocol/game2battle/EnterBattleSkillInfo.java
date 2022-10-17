package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EnterBattleSkillInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8903;
	//技能ID
	private int skillId;
	//技能等级
	private int level;
	//技能修改的属性信息
	private List<com.dykj.rpg.protocol.game2battle.SkillAttribute> attributeInfos;
	//-1为被动  ,0~8 为主动技能的位置
	private int skillPosition;
	public EnterBattleSkillInfo(){
		this.attributeInfos = new ArrayList<>();
	}
	public EnterBattleSkillInfo(int skillId,int level,List<com.dykj.rpg.protocol.game2battle.SkillAttribute> attributeInfos,int skillPosition){
		this.skillId = skillId;
		this.level = level;
		this.attributeInfos = attributeInfos;
		this.skillPosition = skillPosition;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.level = 0;
		for(com.dykj.rpg.protocol.game2battle.SkillAttribute value : attributeInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.attributeInfos.clear();
		this.skillPosition = 0;
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
	public int getLevel(){
		return this.level;
	}
	public void setLevel(int _level){
		this.level = _level;
	}
	public List<com.dykj.rpg.protocol.game2battle.SkillAttribute> getAttributeInfos(){
		return this.attributeInfos;
	}
	public void setAttributeInfos(List<com.dykj.rpg.protocol.game2battle.SkillAttribute> _attributeInfos){
		this.attributeInfos = _attributeInfos;
	}
	public int getSkillPosition(){
		return this.skillPosition;
	}
	public void setSkillPosition(int _skillPosition){
		this.skillPosition = _skillPosition;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		level = (int)dch.bytesToParam("Integer");
		attributeInfos = (List<com.dykj.rpg.protocol.game2battle.SkillAttribute>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.SkillAttribute>");
		skillPosition = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(level)){
			return false;
		}
		if(!ech.paramToBytes(attributeInfos)){
			return false;
		}
		if(!ech.paramToBytes(skillPosition)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EnterBattleSkillInfo [code=" + code +",skillId=" + skillId +",level=" + level +",attributeInfos=" + attributeInfos +",skillPosition=" + skillPosition +"]";
	}
}
