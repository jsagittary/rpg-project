package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SoulRoleInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9040;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//主角id
	private int modelId;
	//灵魂之影出现的位置
	private com.dykj.rpg.protocol.battle.BattlePosition soulPos;
	//释放的技能ID
	private int skillId;
	//施法时间(单位ms)
	private int skillTime;
	public SoulRoleInfo(){
	}
	public SoulRoleInfo(int frameNum,int modelId,com.dykj.rpg.protocol.battle.BattlePosition soulPos,int skillId,int skillTime){
		this.frameNum = frameNum;
		this.modelId = modelId;
		this.soulPos = soulPos;
		this.skillId = skillId;
		this.skillTime = skillTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		this.modelId = 0;
		ProtocolPool.getInstance().restoreProtocol(this.soulPos);
		this.soulPos = null;
		this.skillId = 0;
		this.skillTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getFrameNum(){
		return this.frameNum;
	}
	public void setFrameNum(int _frameNum){
		this.frameNum = _frameNum;
	}
	public int getModelId(){
		return this.modelId;
	}
	public void setModelId(int _modelId){
		this.modelId = _modelId;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getSoulPos(){
		return this.soulPos;
	}
	public void setSoulPos(com.dykj.rpg.protocol.battle.BattlePosition _soulPos){
		this.soulPos = _soulPos;
	}
	public int getSkillId(){
		return this.skillId;
	}
	public void setSkillId(int _skillId){
		this.skillId = _skillId;
	}
	public int getSkillTime(){
		return this.skillTime;
	}
	public void setSkillTime(int _skillTime){
		this.skillTime = _skillTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		modelId = (int)dch.bytesToParam("Integer");
		soulPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		skillId = (int)dch.bytesToParam("Integer");
		skillTime = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(frameNum)){
			return false;
		}
		if(!ech.paramToBytes(modelId)){
			return false;
		}
		if(!ech.paramToBytes(soulPos)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(skillTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SoulRoleInfo [code=" + code +",frameNum=" + frameNum +",modelId=" + modelId +",soulPos=" + soulPos +",skillId=" + skillId +",skillTime=" + skillTime +"]";
	}
}
