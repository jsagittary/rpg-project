package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class TrainSoulSkillRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1413;
	//技能id
	private int skillId;
	//灵魂之影技能培养的成功时间
	private long soulChangeTime;
	//位置
	private int trainSoulPos;
	public TrainSoulSkillRs(){
	}
	public TrainSoulSkillRs(int skillId,long soulChangeTime,int trainSoulPos){
		this.skillId = skillId;
		this.soulChangeTime = soulChangeTime;
		this.trainSoulPos = trainSoulPos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.soulChangeTime = 0;
		this.trainSoulPos = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		soulChangeTime = (long)dch.bytesToParam("Long");
		trainSoulPos = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(soulChangeTime)){
			return false;
		}
		if(!ech.paramToBytes(trainSoulPos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "TrainSoulSkillRs [code=" + code +",skillId=" + skillId +",soulChangeTime=" + soulChangeTime +",trainSoulPos=" + trainSoulPos +"]";
	}
}
