package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class DeletePlayerRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1008;
	//当前选择的角色 0表示没有
	private int chosePlayerId;
	public DeletePlayerRs(){
	}
	public DeletePlayerRs(int chosePlayerId){
		this.chosePlayerId = chosePlayerId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.chosePlayerId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getChosePlayerId(){
		return this.chosePlayerId;
	}
	public void setChosePlayerId(int _chosePlayerId){
		this.chosePlayerId = _chosePlayerId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		chosePlayerId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(chosePlayerId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "DeletePlayerRs [code=" + code +",chosePlayerId=" + chosePlayerId +"]";
	}
}
