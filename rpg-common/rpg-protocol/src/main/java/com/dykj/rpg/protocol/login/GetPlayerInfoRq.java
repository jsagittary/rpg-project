package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GetPlayerInfoRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1101;
	private int accountKey;
	public GetPlayerInfoRq(){
	}
	public GetPlayerInfoRq(int accountKey){
		this.accountKey = accountKey;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.accountKey = 0;
	}
	public short getCode(){
		return this.code;
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
		accountKey = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(accountKey)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "GetPlayerInfoRq [code=" + code +",accountKey=" + accountKey +"]";
	}
}
