package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerEnterBattleRequest extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8905;
	//玩家ID
	private int playerId;
	//玩家sessionId
	private int sessionId;
	//请求进入的战斗类型
	private byte battleType;
	//关卡
	private int missionId;
	//进入战斗所选的角色信息
	private com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo roleInfo;
	//角色携带的技能信息
	private List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo> skillInfos;
	//战斗药品
	private List<com.dykj.rpg.protocol.item.ItemRs> items;
	//灵魂之影id
	private int soulId;
	//技巧列表
	private List<com.dykj.rpg.protocol.game2battle.AiBattleRs> aiBattles;
	public PlayerEnterBattleRequest(){
		this.skillInfos = new ArrayList<>();
		this.items = new ArrayList<>();
		this.aiBattles = new ArrayList<>();
	}
	public PlayerEnterBattleRequest(int playerId,int sessionId,byte battleType,int missionId,com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo roleInfo,List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo> skillInfos,List<com.dykj.rpg.protocol.item.ItemRs> items,int soulId,List<com.dykj.rpg.protocol.game2battle.AiBattleRs> aiBattles){
		this.playerId = playerId;
		this.sessionId = sessionId;
		this.battleType = battleType;
		this.missionId = missionId;
		this.roleInfo = roleInfo;
		this.skillInfos = skillInfos;
		this.items = items;
		this.soulId = soulId;
		this.aiBattles = aiBattles;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.playerId = 0;
		this.sessionId = 0;
		this.battleType = 0;
		this.missionId = 0;
		ProtocolPool.getInstance().restoreProtocol(this.roleInfo);
		this.roleInfo = null;
		for(com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo value : skillInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillInfos.clear();
		for(com.dykj.rpg.protocol.item.ItemRs value : items){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.items.clear();
		this.soulId = 0;
		for(com.dykj.rpg.protocol.game2battle.AiBattleRs value : aiBattles){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.aiBattles.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getPlayerId(){
		return this.playerId;
	}
	public void setPlayerId(int _playerId){
		this.playerId = _playerId;
	}
	public int getSessionId(){
		return this.sessionId;
	}
	public void setSessionId(int _sessionId){
		this.sessionId = _sessionId;
	}
	public byte getBattleType(){
		return this.battleType;
	}
	public void setBattleType(byte _battleType){
		this.battleType = _battleType;
	}
	public int getMissionId(){
		return this.missionId;
	}
	public void setMissionId(int _missionId){
		this.missionId = _missionId;
	}
	public com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo getRoleInfo(){
		return this.roleInfo;
	}
	public void setRoleInfo(com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo _roleInfo){
		this.roleInfo = _roleInfo;
	}
	public List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo> getSkillInfos(){
		return this.skillInfos;
	}
	public void setSkillInfos(List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo> _skillInfos){
		this.skillInfos = _skillInfos;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getItems(){
		return this.items;
	}
	public void setItems(List<com.dykj.rpg.protocol.item.ItemRs> _items){
		this.items = _items;
	}
	public int getSoulId(){
		return this.soulId;
	}
	public void setSoulId(int _soulId){
		this.soulId = _soulId;
	}
	public List<com.dykj.rpg.protocol.game2battle.AiBattleRs> getAiBattles(){
		return this.aiBattles;
	}
	public void setAiBattles(List<com.dykj.rpg.protocol.game2battle.AiBattleRs> _aiBattles){
		this.aiBattles = _aiBattles;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		playerId = (int)dch.bytesToParam("Integer");
		sessionId = (int)dch.bytesToParam("Integer");
		battleType = (byte)dch.bytesToParam("Byte");
		missionId = (int)dch.bytesToParam("Integer");
		roleInfo = (com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo)dch.bytesToParam("com.dykj.rpg.protocol.game2battle.EnterBattleRoleInfo");
		skillInfos = (List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo>");
		items = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		soulId = (int)dch.bytesToParam("Integer");
		aiBattles = (List<com.dykj.rpg.protocol.game2battle.AiBattleRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.AiBattleRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(playerId)){
			return false;
		}
		if(!ech.paramToBytes(sessionId)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		if(!ech.paramToBytes(missionId)){
			return false;
		}
		if(!ech.paramToBytes(roleInfo)){
			return false;
		}
		if(!ech.paramToBytes(skillInfos)){
			return false;
		}
		if(!ech.paramToBytes(items)){
			return false;
		}
		if(!ech.paramToBytes(soulId)){
			return false;
		}
		if(!ech.paramToBytes(aiBattles)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerEnterBattleRequest [code=" + code +",playerId=" + playerId +",sessionId=" + sessionId +",battleType=" + battleType +",missionId=" + missionId +",roleInfo=" + roleInfo +",skillInfos=" + skillInfos +",items=" + items +",soulId=" + soulId +",aiBattles=" + aiBattles +"]";
	}
}
