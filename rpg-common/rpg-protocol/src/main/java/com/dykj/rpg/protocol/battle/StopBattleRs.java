package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class StopBattleRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9006;
	//true=退出成功
	private boolean result;
	public StopBattleRs(){
	}
	public StopBattleRs(boolean result){
		this.result = result;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.result = false;
	}
	public short getCode(){
		return this.code;
	}
	public boolean getResult(){
		return this.result;
	}
	public void setResult(boolean _result){
		this.result = _result;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		result = (boolean)dch.bytesToParam("Boolean");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(result)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "StopBattleRs [code=" + code +",result=" + result +"]";
	}
}
