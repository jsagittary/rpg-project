package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerEnterBattleResponse extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8907;
	//0=失败，1=组队中，2=组队成功
	private byte state;
	public PlayerEnterBattleResponse(){
	}
	public PlayerEnterBattleResponse(byte state){
		this.state = state;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.state = 0;
	}
	public short getCode(){
		return this.code;
	}
	public byte getState(){
		return this.state;
	}
	public void setState(byte _state){
		this.state = _state;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		state = (byte)dch.bytesToParam("Byte");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(state)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerEnterBattleResponse [code=" + code +",state=" + state +"]";
	}
}
