package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class StartTrainSoulSkillRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1416;
	//灵魂之影技能培养的成功时间
	private List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs> trainSoulSkills;
	public StartTrainSoulSkillRs(){
		this.trainSoulSkills = new ArrayList<>();
	}
	public StartTrainSoulSkillRs(List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs> trainSoulSkills){
		this.trainSoulSkills = trainSoulSkills;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.skill.TrainSoulSkillRs value : trainSoulSkills){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.trainSoulSkills.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs> getTrainSoulSkills(){
		return this.trainSoulSkills;
	}
	public void setTrainSoulSkills(List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs> _trainSoulSkills){
		this.trainSoulSkills = _trainSoulSkills;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		trainSoulSkills = (List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.TrainSoulSkillRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(trainSoulSkills)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "StartTrainSoulSkillRs [code=" + code +",trainSoulSkills=" + trainSoulSkills +"]";
	}
}
