package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class DeletePlayerRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1007;
	private int playerId;
	public DeletePlayerRq(){
	}
	public DeletePlayerRq(int playerId){
		this.playerId = playerId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.playerId = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		playerId = (int)dch.bytesToParam("Integer");
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
		return true;
	}
	public String toString() {
		return "DeletePlayerRq [code=" + code +",playerId=" + playerId +"]";
	}
}
