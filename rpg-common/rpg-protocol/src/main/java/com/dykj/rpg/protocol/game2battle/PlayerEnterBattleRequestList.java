package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerEnterBattleRequestList extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8906;
	private List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest> requests;
	public PlayerEnterBattleRequestList(){
		this.requests = new ArrayList<>();
	}
	public PlayerEnterBattleRequestList(List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest> requests){
		this.requests = requests;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest value : requests){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.requests.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest> getRequests(){
		return this.requests;
	}
	public void setRequests(List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest> _requests){
		this.requests = _requests;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		requests = (List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(requests)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerEnterBattleRequestList [code=" + code +",requests=" + requests +"]";
	}
}
