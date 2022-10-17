package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerSkillInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9017;
	//技能ID
	private int id;
	//技能位置 1=核心槽 2以后为技能相应位置
	private byte seat;
	//技能冷却时间 单位为毫秒
	private int cdTime;
	public PlayerSkillInfo(){
	}
	public PlayerSkillInfo(int id,byte seat,int cdTime){
		this.id = id;
		this.seat = seat;
		this.cdTime = cdTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.seat = 0;
		this.cdTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public byte getSeat(){
		return this.seat;
	}
	public void setSeat(byte _seat){
		this.seat = _seat;
	}
	public int getCdTime(){
		return this.cdTime;
	}
	public void setCdTime(int _cdTime){
		this.cdTime = _cdTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (int)dch.bytesToParam("Integer");
		seat = (byte)dch.bytesToParam("Byte");
		cdTime = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(seat)){
			return false;
		}
		if(!ech.paramToBytes(cdTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerSkillInfo [code=" + code +",id=" + id +",seat=" + seat +",cdTime=" + cdTime +"]";
	}
}
