package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ChangePlayerRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1111;
	//角色id
	private int playerId;
	//账号id
	private int accountKey;
	public ChangePlayerRq(){
	}
	public ChangePlayerRq(int playerId,int accountKey){
		this.playerId = playerId;
		this.accountKey = accountKey;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.playerId = 0;
		this.accountKey = 0;
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
	public int getAccountKey(){
		return this.accountKey;
	}
	public void setAccountKey(int _accountKey){
		this.accountKey = _accountKey;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		playerId = (int)dch.bytesToParam("Integer");
		accountKey = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(accountKey)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ChangePlayerRq [code=" + code +",playerId=" + playerId +",accountKey=" + accountKey +"]";
	}
}
