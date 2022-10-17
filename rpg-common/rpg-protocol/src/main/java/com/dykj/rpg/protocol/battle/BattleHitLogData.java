package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleHitLogData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9035;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//施法者modelId
	private int releaseModelId;
	//施法者属性列表
	private List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> releaseAtrributes;
	//目标modelId
	private int targetModelId;
	//目标属性列表
	private List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> targetAtrributes;
	//技能ID
	private int skillId;
	//技能元素类型
	private byte elementType;
	//true=闪避成功
	private boolean shanbi;
	//true=暴击成功
	private boolean baoji;
	//true=格挡成功
	private boolean gedang;
	//true=治疗成功
	private boolean zhiliao;
	//伤害(治疗)数值
	private int hurtNum;
	//计算过程
	private String calculationProgress;
	//受到伤害后的目标剩余血量
	private int targetCurBlood;
	//施法者消耗的技能资源类型
	private byte skillSourceType;
	//施法者消耗的技能资源数量
	private int skillSourceNum;
	//施法者剩余的技能资源数量
	private int releaseCurskillSourceNum;
	public BattleHitLogData(){
		this.releaseAtrributes = new ArrayList<>();
		this.targetAtrributes = new ArrayList<>();
	}
	public BattleHitLogData(int frameNum,int releaseModelId,List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> releaseAtrributes,int targetModelId,List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> targetAtrributes,int skillId,byte elementType,boolean shanbi,boolean baoji,boolean gedang,boolean zhiliao,int hurtNum,String calculationProgress,int targetCurBlood,byte skillSourceType,int skillSourceNum,int releaseCurskillSourceNum){
		this.frameNum = frameNum;
		this.releaseModelId = releaseModelId;
		this.releaseAtrributes = releaseAtrributes;
		this.targetModelId = targetModelId;
		this.targetAtrributes = targetAtrributes;
		this.skillId = skillId;
		this.elementType = elementType;
		this.shanbi = shanbi;
		this.baoji = baoji;
		this.gedang = gedang;
		this.zhiliao = zhiliao;
		this.hurtNum = hurtNum;
		this.calculationProgress = calculationProgress;
		this.targetCurBlood = targetCurBlood;
		this.skillSourceType = skillSourceType;
		this.skillSourceNum = skillSourceNum;
		this.releaseCurskillSourceNum = releaseCurskillSourceNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		this.releaseModelId = 0;
		for(com.dykj.rpg.protocol.battle.RoleBasicAttributeLog value : releaseAtrributes){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.releaseAtrributes.clear();
		this.targetModelId = 0;
		for(com.dykj.rpg.protocol.battle.RoleBasicAttributeLog value : targetAtrributes){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.targetAtrributes.clear();
		this.skillId = 0;
		this.elementType = 0;
		this.shanbi = false;
		this.baoji = false;
		this.gedang = false;
		this.zhiliao = false;
		this.hurtNum = 0;
		this.calculationProgress = null;
		this.targetCurBlood = 0;
		this.skillSourceType = 0;
		this.skillSourceNum = 0;
		this.releaseCurskillSourceNum = 0;
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
	public int getReleaseModelId(){
		return this.releaseModelId;
	}
	public void setReleaseModelId(int _releaseModelId){
		this.releaseModelId = _releaseModelId;
	}
	public List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> getReleaseAtrributes(){
		return this.releaseAtrributes;
	}
	public void setReleaseAtrributes(List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> _releaseAtrributes){
		this.releaseAtrributes = _releaseAtrributes;
	}
	public int getTargetModelId(){
		return this.targetModelId;
	}
	public void setTargetModelId(int _targetModelId){
		this.targetModelId = _targetModelId;
	}
	public List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> getTargetAtrributes(){
		return this.targetAtrributes;
	}
	public void setTargetAtrributes(List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog> _targetAtrributes){
		this.targetAtrributes = _targetAtrributes;
	}
	public int getSkillId(){
		return this.skillId;
	}
	public void setSkillId(int _skillId){
		this.skillId = _skillId;
	}
	public byte getElementType(){
		return this.elementType;
	}
	public void setElementType(byte _elementType){
		this.elementType = _elementType;
	}
	public boolean getShanbi(){
		return this.shanbi;
	}
	public void setShanbi(boolean _shanbi){
		this.shanbi = _shanbi;
	}
	public boolean getBaoji(){
		return this.baoji;
	}
	public void setBaoji(boolean _baoji){
		this.baoji = _baoji;
	}
	public boolean getGedang(){
		return this.gedang;
	}
	public void setGedang(boolean _gedang){
		this.gedang = _gedang;
	}
	public boolean getZhiliao(){
		return this.zhiliao;
	}
	public void setZhiliao(boolean _zhiliao){
		this.zhiliao = _zhiliao;
	}
	public int getHurtNum(){
		return this.hurtNum;
	}
	public void setHurtNum(int _hurtNum){
		this.hurtNum = _hurtNum;
	}
	public String getCalculationProgress(){
		return this.calculationProgress;
	}
	public void setCalculationProgress(String _calculationProgress){
		this.calculationProgress = _calculationProgress;
	}
	public int getTargetCurBlood(){
		return this.targetCurBlood;
	}
	public void setTargetCurBlood(int _targetCurBlood){
		this.targetCurBlood = _targetCurBlood;
	}
	public byte getSkillSourceType(){
		return this.skillSourceType;
	}
	public void setSkillSourceType(byte _skillSourceType){
		this.skillSourceType = _skillSourceType;
	}
	public int getSkillSourceNum(){
		return this.skillSourceNum;
	}
	public void setSkillSourceNum(int _skillSourceNum){
		this.skillSourceNum = _skillSourceNum;
	}
	public int getReleaseCurskillSourceNum(){
		return this.releaseCurskillSourceNum;
	}
	public void setReleaseCurskillSourceNum(int _releaseCurskillSourceNum){
		this.releaseCurskillSourceNum = _releaseCurskillSourceNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		releaseModelId = (int)dch.bytesToParam("Integer");
		releaseAtrributes = (List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog>");
		targetModelId = (int)dch.bytesToParam("Integer");
		targetAtrributes = (List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.RoleBasicAttributeLog>");
		skillId = (int)dch.bytesToParam("Integer");
		elementType = (byte)dch.bytesToParam("Byte");
		shanbi = (boolean)dch.bytesToParam("Boolean");
		baoji = (boolean)dch.bytesToParam("Boolean");
		gedang = (boolean)dch.bytesToParam("Boolean");
		zhiliao = (boolean)dch.bytesToParam("Boolean");
		hurtNum = (int)dch.bytesToParam("Integer");
		calculationProgress = (String)dch.bytesToParam("String");
		targetCurBlood = (int)dch.bytesToParam("Integer");
		skillSourceType = (byte)dch.bytesToParam("Byte");
		skillSourceNum = (int)dch.bytesToParam("Integer");
		releaseCurskillSourceNum = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(releaseModelId)){
			return false;
		}
		if(!ech.paramToBytes(releaseAtrributes)){
			return false;
		}
		if(!ech.paramToBytes(targetModelId)){
			return false;
		}
		if(!ech.paramToBytes(targetAtrributes)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(elementType)){
			return false;
		}
		if(!ech.paramToBytes(shanbi)){
			return false;
		}
		if(!ech.paramToBytes(baoji)){
			return false;
		}
		if(!ech.paramToBytes(gedang)){
			return false;
		}
		if(!ech.paramToBytes(zhiliao)){
			return false;
		}
		if(!ech.paramToBytes(hurtNum)){
			return false;
		}
		if(!ech.paramToBytes(calculationProgress)){
			return false;
		}
		if(!ech.paramToBytes(targetCurBlood)){
			return false;
		}
		if(!ech.paramToBytes(skillSourceType)){
			return false;
		}
		if(!ech.paramToBytes(skillSourceNum)){
			return false;
		}
		if(!ech.paramToBytes(releaseCurskillSourceNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleHitLogData [code=" + code +",frameNum=" + frameNum +",releaseModelId=" + releaseModelId +",releaseAtrributes=" + releaseAtrributes +",targetModelId=" + targetModelId +",targetAtrributes=" + targetAtrributes +",skillId=" + skillId +",elementType=" + elementType +",shanbi=" + shanbi +",baoji=" + baoji +",gedang=" + gedang +",zhiliao=" + zhiliao +",hurtNum=" + hurtNum +",calculationProgress=" + calculationProgress +",targetCurBlood=" + targetCurBlood +",skillSourceType=" + skillSourceType +",skillSourceNum=" + skillSourceNum +",releaseCurskillSourceNum=" + releaseCurskillSourceNum +"]";
	}
}
