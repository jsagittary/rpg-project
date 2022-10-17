package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1401;
	//技能id
	private int skillId;
	//技能等级
	private int skillLevel;
	//技能经验
	private int skillExp;
	//技能星级
	private int skillStarLevel;
	//技能装备的位置 1~8 为穿戴的技能, -1 为没穿戴的技能
	private int position;
	//1 - 普通技能, 2 - 魂技
	private int skillType;
	//魂技培养转化起始时间, 0表示未开始培养  -1 表示已经培养完成
	private long soulChangeTime;
	//训练灵魂之影时在界面的位置
	private int trainSoulPos;
	//符文列表
	private List<com.dykj.rpg.protocol.rune.RuneRs> runeList;
	public SkillRs(){
		this.runeList = new ArrayList<>();
	}
	public SkillRs(int skillId,int skillLevel,int skillExp,int skillStarLevel,int position,int skillType,long soulChangeTime,int trainSoulPos,List<com.dykj.rpg.protocol.rune.RuneRs> runeList){
		this.skillId = skillId;
		this.skillLevel = skillLevel;
		this.skillExp = skillExp;
		this.skillStarLevel = skillStarLevel;
		this.position = position;
		this.skillType = skillType;
		this.soulChangeTime = soulChangeTime;
		this.trainSoulPos = trainSoulPos;
		this.runeList = runeList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.skillLevel = 0;
		this.skillExp = 0;
		this.skillStarLevel = 0;
		this.position = 0;
		this.skillType = 0;
		this.soulChangeTime = 0;
		this.trainSoulPos = 0;
		for(com.dykj.rpg.protocol.rune.RuneRs value : runeList){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.runeList.clear();
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
	public int getSkillLevel(){
		return this.skillLevel;
	}
	public void setSkillLevel(int _skillLevel){
		this.skillLevel = _skillLevel;
	}
	public int getSkillExp(){
		return this.skillExp;
	}
	public void setSkillExp(int _skillExp){
		this.skillExp = _skillExp;
	}
	public int getSkillStarLevel(){
		return this.skillStarLevel;
	}
	public void setSkillStarLevel(int _skillStarLevel){
		this.skillStarLevel = _skillStarLevel;
	}
	public int getPosition(){
		return this.position;
	}
	public void setPosition(int _position){
		this.position = _position;
	}
	public int getSkillType(){
		return this.skillType;
	}
	public void setSkillType(int _skillType){
		this.skillType = _skillType;
	}
	public long getSoulChangeTime(){
		return this.soulChangeTime;
	}
	public void setSoulChangeTime(long _soulChangeTime){
		this.soulChangeTime = _soulChangeTime;
	}
	public int getTrainSoulPos(){
		return this.trainSoulPos;
	}
	public void setTrainSoulPos(int _trainSoulPos){
		this.trainSoulPos = _trainSoulPos;
	}
	public List<com.dykj.rpg.protocol.rune.RuneRs> getRuneList(){
		return this.runeList;
	}
	public void setRuneList(List<com.dykj.rpg.protocol.rune.RuneRs> _runeList){
		this.runeList = _runeList;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		skillLevel = (int)dch.bytesToParam("Integer");
		skillExp = (int)dch.bytesToParam("Integer");
		skillStarLevel = (int)dch.bytesToParam("Integer");
		position = (int)dch.bytesToParam("Integer");
		skillType = (int)dch.bytesToParam("Integer");
		soulChangeTime = (long)dch.bytesToParam("Long");
		trainSoulPos = (int)dch.bytesToParam("Integer");
		runeList = (List<com.dykj.rpg.protocol.rune.RuneRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.rune.RuneRs>");
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
		if(!ech.paramToBytes(skillLevel)){
			return false;
		}
		if(!ech.paramToBytes(skillExp)){
			return false;
		}
		if(!ech.paramToBytes(skillStarLevel)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		if(!ech.paramToBytes(skillType)){
			return false;
		}
		if(!ech.paramToBytes(soulChangeTime)){
			return false;
		}
		if(!ech.paramToBytes(trainSoulPos)){
			return false;
		}
		if(!ech.paramToBytes(runeList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillRs [code=" + code +",skillId=" + skillId +",skillLevel=" + skillLevel +",skillExp=" + skillExp +",skillStarLevel=" + skillStarLevel +",position=" + position +",skillType=" + skillType +",soulChangeTime=" + soulChangeTime +",trainSoulPos=" + trainSoulPos +",runeList=" + runeList +"]";
	}
}
