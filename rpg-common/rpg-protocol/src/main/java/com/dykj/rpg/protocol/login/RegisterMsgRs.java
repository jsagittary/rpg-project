package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RegisterMsgRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1107;
	//玩家信息
	private com.dykj.rpg.protocol.player.PlayerRs player;
	public RegisterMsgRs(){
	}
	public RegisterMsgRs(com.dykj.rpg.protocol.player.PlayerRs player){
		this.player = player;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.player);
		this.player = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.player.PlayerRs getPlayer(){
		return this.player;
	}
	public void setPlayer(com.dykj.rpg.protocol.player.PlayerRs _player){
		this.player = _player;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		player = (com.dykj.rpg.protocol.player.PlayerRs)dch.bytesToParam("com.dykj.rpg.protocol.player.PlayerRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(player)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RegisterMsgRs [code=" + code +",player=" + player +"]";
	}
}
