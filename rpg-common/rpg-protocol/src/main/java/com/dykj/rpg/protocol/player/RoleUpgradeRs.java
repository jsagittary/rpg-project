package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RoleUpgradeRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1010;
	//玩家等级
	private int level;
	//经验
	private int exp;
	public RoleUpgradeRs(){
	}
	public RoleUpgradeRs(int level,int exp){
		this.level = level;
		this.exp = exp;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.level = 0;
		this.exp = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getLevel(){
		return this.level;
	}
	public void setLevel(int _level){
		this.level = _level;
	}
	public int getExp(){
		return this.exp;
	}
	public void setExp(int _exp){
		this.exp = _exp;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		level = (int)dch.bytesToParam("Integer");
		exp = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(level)){
			return false;
		}
		if(!ech.paramToBytes(exp)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RoleUpgradeRs [code=" + code +",level=" + level +",exp=" + exp +"]";
	}
}
