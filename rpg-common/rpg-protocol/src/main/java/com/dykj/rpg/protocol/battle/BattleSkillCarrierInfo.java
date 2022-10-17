package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleSkillCarrierInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9023;
	//载体ID,用于客户端寻找特效资源
	private int carrierId;
	//载体在地图中的编号，用于区分所有载体
	private int modelId;
	//载体类型 1=目标子弹(跟踪)，2=地点子弹，3=方向子弹，4=目标位移(跟踪)，5=地点位移，6=方向位移，7=地点魔法，8=地点瞬移，9=跟随
	private byte carrierType;
	//1=出生，2=消失
	private byte state;
	//false=不播放消失特效 true=播放消失特效 (state=2时有效)
	private boolean showDieEffect;
	//载体挂载对象 (carrierType=4,5,6时,挂载对象跟随载体的位置carrierType=9时,载体跟随挂载对象的位置)
	private int mountModelId;
	//载体的位置 (carrierType=9时，effectPos为其偏移量)
	private com.dykj.rpg.protocol.battle.BattlePosition effectPos;
	//载体的目标位置 carrierType=2,5时有值,用于做抛物线
	private com.dykj.rpg.protocol.battle.BattlePosition targetPos;
	//载体的目标对象 carrierType=1,4时有值,用于做跟踪
	private int targetModelId;
	//载体移动轨迹 1=直线平飞，2=抛物线
	private byte moveLocus;
	//载体速度(单位cm/s)
	private int speed;
	//载体最大飞行距离(单位cm)
	private int distance;
	//载体最大运行时间(单位ms)
	private int moveTime;
	public BattleSkillCarrierInfo(){
	}
	public BattleSkillCarrierInfo(int carrierId,int modelId,byte carrierType,byte state,boolean showDieEffect,int mountModelId,com.dykj.rpg.protocol.battle.BattlePosition effectPos,com.dykj.rpg.protocol.battle.BattlePosition targetPos,int targetModelId,byte moveLocus,int speed,int distance,int moveTime){
		this.carrierId = carrierId;
		this.modelId = modelId;
		this.carrierType = carrierType;
		this.state = state;
		this.showDieEffect = showDieEffect;
		this.mountModelId = mountModelId;
		this.effectPos = effectPos;
		this.targetPos = targetPos;
		this.targetModelId = targetModelId;
		this.moveLocus = moveLocus;
		this.speed = speed;
		this.distance = distance;
		this.moveTime = moveTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.carrierId = 0;
		this.modelId = 0;
		this.carrierType = 0;
		this.state = 0;
		this.showDieEffect = false;
		this.mountModelId = 0;
		ProtocolPool.getInstance().restoreProtocol(this.effectPos);
		this.effectPos = null;
		ProtocolPool.getInstance().restoreProtocol(this.targetPos);
		this.targetPos = null;
		this.targetModelId = 0;
		this.moveLocus = 0;
		this.speed = 0;
		this.distance = 0;
		this.moveTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getCarrierId(){
		return this.carrierId;
	}
	public void setCarrierId(int _carrierId){
		this.carrierId = _carrierId;
	}
	public int getModelId(){
		return this.modelId;
	}
	public void setModelId(int _modelId){
		this.modelId = _modelId;
	}
	public byte getCarrierType(){
		return this.carrierType;
	}
	public void setCarrierType(byte _carrierType){
		this.carrierType = _carrierType;
	}
	public byte getState(){
		return this.state;
	}
	public void setState(byte _state){
		this.state = _state;
	}
	public boolean getShowDieEffect(){
		return this.showDieEffect;
	}
	public void setShowDieEffect(boolean _showDieEffect){
		this.showDieEffect = _showDieEffect;
	}
	public int getMountModelId(){
		return this.mountModelId;
	}
	public void setMountModelId(int _mountModelId){
		this.mountModelId = _mountModelId;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getEffectPos(){
		return this.effectPos;
	}
	public void setEffectPos(com.dykj.rpg.protocol.battle.BattlePosition _effectPos){
		this.effectPos = _effectPos;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getTargetPos(){
		return this.targetPos;
	}
	public void setTargetPos(com.dykj.rpg.protocol.battle.BattlePosition _targetPos){
		this.targetPos = _targetPos;
	}
	public int getTargetModelId(){
		return this.targetModelId;
	}
	public void setTargetModelId(int _targetModelId){
		this.targetModelId = _targetModelId;
	}
	public byte getMoveLocus(){
		return this.moveLocus;
	}
	public void setMoveLocus(byte _moveLocus){
		this.moveLocus = _moveLocus;
	}
	public int getSpeed(){
		return this.speed;
	}
	public void setSpeed(int _speed){
		this.speed = _speed;
	}
	public int getDistance(){
		return this.distance;
	}
	public void setDistance(int _distance){
		this.distance = _distance;
	}
	public int getMoveTime(){
		return this.moveTime;
	}
	public void setMoveTime(int _moveTime){
		this.moveTime = _moveTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		carrierId = (int)dch.bytesToParam("Integer");
		modelId = (int)dch.bytesToParam("Integer");
		carrierType = (byte)dch.bytesToParam("Byte");
		state = (byte)dch.bytesToParam("Byte");
		showDieEffect = (boolean)dch.bytesToParam("Boolean");
		mountModelId = (int)dch.bytesToParam("Integer");
		effectPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		targetPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		targetModelId = (int)dch.bytesToParam("Integer");
		moveLocus = (byte)dch.bytesToParam("Byte");
		speed = (int)dch.bytesToParam("Integer");
		distance = (int)dch.bytesToParam("Integer");
		moveTime = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(carrierId)){
			return false;
		}
		if(!ech.paramToBytes(modelId)){
			return false;
		}
		if(!ech.paramToBytes(carrierType)){
			return false;
		}
		if(!ech.paramToBytes(state)){
			return false;
		}
		if(!ech.paramToBytes(showDieEffect)){
			return false;
		}
		if(!ech.paramToBytes(mountModelId)){
			return false;
		}
		if(!ech.paramToBytes(effectPos)){
			return false;
		}
		if(!ech.paramToBytes(targetPos)){
			return false;
		}
		if(!ech.paramToBytes(targetModelId)){
			return false;
		}
		if(!ech.paramToBytes(moveLocus)){
			return false;
		}
		if(!ech.paramToBytes(speed)){
			return false;
		}
		if(!ech.paramToBytes(distance)){
			return false;
		}
		if(!ech.paramToBytes(moveTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleSkillCarrierInfo [code=" + code +",carrierId=" + carrierId +",modelId=" + modelId +",carrierType=" + carrierType +",state=" + state +",showDieEffect=" + showDieEffect +",mountModelId=" + mountModelId +",effectPos=" + effectPos +",targetPos=" + targetPos +",targetModelId=" + targetModelId +",moveLocus=" + moveLocus +",speed=" + speed +",distance=" + distance +",moveTime=" + moveTime +"]";
	}
}
