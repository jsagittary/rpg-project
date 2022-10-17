package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleSkillEffectInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9022;
	//效果ID,用于客户端寻找特效资源
	private int effectId;
	//效果挂载对象 （modelId与effectPos只会有一个进行赋值）
	private int modelId;
	//效果的位置 （modelId与effectPos只会有一个进行赋值）
	private com.dykj.rpg.protocol.battle.BattlePosition effectPos;
	public BattleSkillEffectInfo(){
	}
	public BattleSkillEffectInfo(int effectId,int modelId,com.dykj.rpg.protocol.battle.BattlePosition effectPos){
		this.effectId = effectId;
		this.modelId = modelId;
		this.effectPos = effectPos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.effectId = 0;
		this.modelId = 0;
		ProtocolPool.getInstance().restoreProtocol(this.effectPos);
		this.effectPos = null;
	}
	public short getCode(){
		return this.code;
	}
	public int getEffectId(){
		return this.effectId;
	}
	public void setEffectId(int _effectId){
		this.effectId = _effectId;
	}
	public int getModelId(){
		return this.modelId;
	}
	public void setModelId(int _modelId){
		this.modelId = _modelId;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getEffectPos(){
		return this.effectPos;
	}
	public void setEffectPos(com.dykj.rpg.protocol.battle.BattlePosition _effectPos){
		this.effectPos = _effectPos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		effectId = (int)dch.bytesToParam("Integer");
		modelId = (int)dch.bytesToParam("Integer");
		effectPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(effectId)){
			return false;
		}
		if(!ech.paramToBytes(modelId)){
			return false;
		}
		if(!ech.paramToBytes(effectPos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleSkillEffectInfo [code=" + code +",effectId=" + effectId +",modelId=" + modelId +",effectPos=" + effectPos +"]";
	}
}
