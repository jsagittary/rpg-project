package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleHitInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9030;
	private int modelId;
	//被击中时的效果ID
	private int beHidId;
	//被击伤害类型 1= 普通，2 = 闪避，3 = 暴击，4 = 格挡，5 = 治疗，6 = 精通伤害
	private byte hurtType;
	//血量改变 正数加血，负数减血
	private int changeBlood;
	//0=存活，1=死亡
	private byte state;
	//死亡时受击的载体ID
	private int carrierId;
	//死亡方向
	private short direction;
	public BattleRoleHitInfo(){
	}
	public BattleRoleHitInfo(int modelId,int beHidId,byte hurtType,int changeBlood,byte state,int carrierId,short direction){
		this.modelId = modelId;
		this.beHidId = beHidId;
		this.hurtType = hurtType;
		this.changeBlood = changeBlood;
		this.state = state;
		this.carrierId = carrierId;
		this.direction = direction;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.modelId = 0;
		this.beHidId = 0;
		this.hurtType = 0;
		this.changeBlood = 0;
		this.state = 0;
		this.carrierId = 0;
		this.direction = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getModelId(){
		return this.modelId;
	}
	public void setModelId(int _modelId){
		this.modelId = _modelId;
	}
	public int getBeHidId(){
		return this.beHidId;
	}
	public void setBeHidId(int _beHidId){
		this.beHidId = _beHidId;
	}
	public byte getHurtType(){
		return this.hurtType;
	}
	public void setHurtType(byte _hurtType){
		this.hurtType = _hurtType;
	}
	public int getChangeBlood(){
		return this.changeBlood;
	}
	public void setChangeBlood(int _changeBlood){
		this.changeBlood = _changeBlood;
	}
	public byte getState(){
		return this.state;
	}
	public void setState(byte _state){
		this.state = _state;
	}
	public int getCarrierId(){
		return this.carrierId;
	}
	public void setCarrierId(int _carrierId){
		this.carrierId = _carrierId;
	}
	public short getDirection(){
		return this.direction;
	}
	public void setDirection(short _direction){
		this.direction = _direction;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		modelId = (int)dch.bytesToParam("Integer");
		beHidId = (int)dch.bytesToParam("Integer");
		hurtType = (byte)dch.bytesToParam("Byte");
		changeBlood = (int)dch.bytesToParam("Integer");
		state = (byte)dch.bytesToParam("Byte");
		carrierId = (int)dch.bytesToParam("Integer");
		direction = (short)dch.bytesToParam("Short");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(modelId)){
			return false;
		}
		if(!ech.paramToBytes(beHidId)){
			return false;
		}
		if(!ech.paramToBytes(hurtType)){
			return false;
		}
		if(!ech.paramToBytes(changeBlood)){
			return false;
		}
		if(!ech.paramToBytes(state)){
			return false;
		}
		if(!ech.paramToBytes(carrierId)){
			return false;
		}
		if(!ech.paramToBytes(direction)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleHitInfo [code=" + code +",modelId=" + modelId +",beHidId=" + beHidId +",hurtType=" + hurtType +",changeBlood=" + changeBlood +",state=" + state +",carrierId=" + carrierId +",direction=" + direction +"]";
	}
}
