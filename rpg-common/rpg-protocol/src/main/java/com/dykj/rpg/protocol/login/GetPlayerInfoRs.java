package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GetPlayerInfoRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1102;
	private List<com.dykj.rpg.protocol.player.PlayerRs> players;
	// 选中角色的装备
	private List<com.dykj.rpg.protocol.item.ItemRs> equips;
	public GetPlayerInfoRs(){
		this.players = new ArrayList<>();
		this.equips = new ArrayList<>();
	}
	public GetPlayerInfoRs(List<com.dykj.rpg.protocol.player.PlayerRs> players,List<com.dykj.rpg.protocol.item.ItemRs> equips){
		this.players = players;
		this.equips = equips;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.player.PlayerRs value : players){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.players.clear();
		for(com.dykj.rpg.protocol.item.ItemRs value : equips){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.equips.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.player.PlayerRs> getPlayers(){
		return this.players;
	}
	public void setPlayers(List<com.dykj.rpg.protocol.player.PlayerRs> _players){
		this.players = _players;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getEquips(){
		return this.equips;
	}
	public void setEquips(List<com.dykj.rpg.protocol.item.ItemRs> _equips){
		this.equips = _equips;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		players = (List<com.dykj.rpg.protocol.player.PlayerRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.player.PlayerRs>");
		equips = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(players)){
			return false;
		}
		if(!ech.paramToBytes(equips)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "GetPlayerInfoRs [code=" + code +",players=" + players +",equips=" + equips +"]";
	}
}
