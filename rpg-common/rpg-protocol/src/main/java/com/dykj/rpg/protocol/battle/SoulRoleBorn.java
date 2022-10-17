package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SoulRoleBorn extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9041;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//主角id
	private int modelId;
	//灵魂之影出现的位置
	private com.dykj.rpg.protocol.battle.BattlePosition soulPos;
	public SoulRoleBorn(){
	}
	public SoulRoleBorn(int frameNum,int modelId,com.dykj.rpg.protocol.battle.BattlePosition soulPos){
		this.frameNum = frameNum;
		this.modelId = modelId;
		this.soulPos = soulPos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		this.modelId = 0;
		ProtocolPool.getInstance().restoreProtocol(this.soulPos);
		this.soulPos = null;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		modelId = (int)dch.bytesToParam("Integer");
		soulPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
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
		return true;
	}
	public String toString() {
		return "SoulRoleBorn [code=" + code +",frameNum=" + frameNum +",modelId=" + modelId +",soulPos=" + soulPos +"]";
	}
}
