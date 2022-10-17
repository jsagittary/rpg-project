package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9021;
	//角色在地图中分配的唯一id
	private int id;
	//角色动作 0=等待，1=出生，2=移动（跑，冲，退），3=死亡，4=释放技能，5=受击，6=硬直
	private byte anim;
	//血量上限
	private int maxBlood;
	//血量
	private int blood;
	//anim=3(死亡)时，position里的direction为击飞方向，direction=-1为沿用上一帧怪物方向原地倒下
	private com.dykj.rpg.protocol.battle.BattlePosition position;
	//释放的技能ID anim=4时此值有效
	private int skillId;
	//施法时间，单位ms anim=4时此值有效
	private int skillTime;
	//false=隐藏，true=显示
	private boolean show;
	//角色锁定的攻击目标的id，对应此协议的id，0=没有目标
	private int targetId;
	//灵魂之影能量值
	private int soulEnergy;
	//技能资源值
	private int skillSource;
	public BattleRoleInfo(){
	}
	public BattleRoleInfo(int id,byte anim,int maxBlood,int blood,com.dykj.rpg.protocol.battle.BattlePosition position,int skillId,int skillTime,boolean show,int targetId,int soulEnergy,int skillSource){
		this.id = id;
		this.anim = anim;
		this.maxBlood = maxBlood;
		this.blood = blood;
		this.position = position;
		this.skillId = skillId;
		this.skillTime = skillTime;
		this.show = show;
		this.targetId = targetId;
		this.soulEnergy = soulEnergy;
		this.skillSource = skillSource;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.anim = 0;
		this.maxBlood = 0;
		this.blood = 0;
		ProtocolPool.getInstance().restoreProtocol(this.position);
		this.position = null;
		this.skillId = 0;
		this.skillTime = 0;
		this.show = false;
		this.targetId = 0;
		this.soulEnergy = 0;
		this.skillSource = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public byte getAnim(){
		return this.anim;
	}
	public void setAnim(byte _anim){
		this.anim = _anim;
	}
	public int getMaxBlood(){
		return this.maxBlood;
	}
	public void setMaxBlood(int _maxBlood){
		this.maxBlood = _maxBlood;
	}
	public int getBlood(){
		return this.blood;
	}
	public void setBlood(int _blood){
		this.blood = _blood;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getPosition(){
		return this.position;
	}
	public void setPosition(com.dykj.rpg.protocol.battle.BattlePosition _position){
		this.position = _position;
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
	public boolean getShow(){
		return this.show;
	}
	public void setShow(boolean _show){
		this.show = _show;
	}
	public int getTargetId(){
		return this.targetId;
	}
	public void setTargetId(int _targetId){
		this.targetId = _targetId;
	}
	public int getSoulEnergy(){
		return this.soulEnergy;
	}
	public void setSoulEnergy(int _soulEnergy){
		this.soulEnergy = _soulEnergy;
	}
	public int getSkillSource(){
		return this.skillSource;
	}
	public void setSkillSource(int _skillSource){
		this.skillSource = _skillSource;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (int)dch.bytesToParam("Integer");
		anim = (byte)dch.bytesToParam("Byte");
		maxBlood = (int)dch.bytesToParam("Integer");
		blood = (int)dch.bytesToParam("Integer");
		position = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		skillId = (int)dch.bytesToParam("Integer");
		skillTime = (int)dch.bytesToParam("Integer");
		show = (boolean)dch.bytesToParam("Boolean");
		targetId = (int)dch.bytesToParam("Integer");
		soulEnergy = (int)dch.bytesToParam("Integer");
		skillSource = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(anim)){
			return false;
		}
		if(!ech.paramToBytes(maxBlood)){
			return false;
		}
		if(!ech.paramToBytes(blood)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(skillTime)){
			return false;
		}
		if(!ech.paramToBytes(show)){
			return false;
		}
		if(!ech.paramToBytes(targetId)){
			return false;
		}
		if(!ech.paramToBytes(soulEnergy)){
			return false;
		}
		if(!ech.paramToBytes(skillSource)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleInfo [code=" + code +",id=" + id +",anim=" + anim +",maxBlood=" + maxBlood +",blood=" + blood +",position=" + position +",skillId=" + skillId +",skillTime=" + skillTime +",show=" + show +",targetId=" + targetId +",soulEnergy=" + soulEnergy +",skillSource=" + skillSource +"]";
	}
}
