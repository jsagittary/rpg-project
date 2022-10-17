package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleLootInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9028;
	//物品掉落地点
	private com.dykj.rpg.protocol.battle.BattlePosition lootPos;
	private List<com.dykj.rpg.protocol.battle.LootDetailInfo> lootDetails;
	public BattleRoleLootInfo(){
		this.lootDetails = new ArrayList<>();
	}
	public BattleRoleLootInfo(com.dykj.rpg.protocol.battle.BattlePosition lootPos,List<com.dykj.rpg.protocol.battle.LootDetailInfo> lootDetails){
		this.lootPos = lootPos;
		this.lootDetails = lootDetails;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.lootPos);
		this.lootPos = null;
		for(com.dykj.rpg.protocol.battle.LootDetailInfo value : lootDetails){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.lootDetails.clear();
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getLootPos(){
		return this.lootPos;
	}
	public void setLootPos(com.dykj.rpg.protocol.battle.BattlePosition _lootPos){
		this.lootPos = _lootPos;
	}
	public List<com.dykj.rpg.protocol.battle.LootDetailInfo> getLootDetails(){
		return this.lootDetails;
	}
	public void setLootDetails(List<com.dykj.rpg.protocol.battle.LootDetailInfo> _lootDetails){
		this.lootDetails = _lootDetails;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		lootPos = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		lootDetails = (List<com.dykj.rpg.protocol.battle.LootDetailInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.LootDetailInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(lootPos)){
			return false;
		}
		if(!ech.paramToBytes(lootDetails)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleLootInfo [code=" + code +",lootPos=" + lootPos +",lootDetails=" + lootDetails +"]";
	}
}
