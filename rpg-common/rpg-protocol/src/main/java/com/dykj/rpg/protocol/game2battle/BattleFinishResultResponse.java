package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleFinishResultResponse extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8910;
	private int battleId;
	//所有玩家的结算信息
	private List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse> results;
	public BattleFinishResultResponse(){
		this.results = new ArrayList<>();
	}
	public BattleFinishResultResponse(int battleId,List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse> results){
		this.battleId = battleId;
		this.results = results;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.battleId = 0;
		for(com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse value : results){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.results.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getBattleId(){
		return this.battleId;
	}
	public void setBattleId(int _battleId){
		this.battleId = _battleId;
	}
	public List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse> getResults(){
		return this.results;
	}
	public void setResults(List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse> _results){
		this.results = _results;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		battleId = (int)dch.bytesToParam("Integer");
		results = (List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(battleId)){
			return false;
		}
		if(!ech.paramToBytes(results)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleFinishResultResponse [code=" + code +",battleId=" + battleId +",results=" + results +"]";
	}
}
