package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class LoginBattleServerRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9001;
	private int userId;
	public LoginBattleServerRq(){
	}
	public LoginBattleServerRq(int userId){
		this.userId = userId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.userId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getUserId(){
		return this.userId;
	}
	public void setUserId(int _userId){
		this.userId = _userId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		userId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(userId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "LoginBattleServerRq [code=" + code +",userId=" + userId +"]";
	}
}
