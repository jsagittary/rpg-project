package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class UpdPlayerInfoRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1009;
	//玩家等级
	private int level;
	//经验
	private int exp;
	//vip等级
	private int vipLv;
	//日活跃度
	private int dailyActivity;
	//周活跃度
	private int weekActivity;
	//守护者奖励剩余时长(单位:分)
	private long protectorRemainingTime;
	//守护者奖励截止时间
	private long protectorLastTime;
	public UpdPlayerInfoRs(){
	}
	public UpdPlayerInfoRs(int level,int exp,int vipLv,int dailyActivity,int weekActivity,long protectorRemainingTime,long protectorLastTime){
		this.level = level;
		this.exp = exp;
		this.vipLv = vipLv;
		this.dailyActivity = dailyActivity;
		this.weekActivity = weekActivity;
		this.protectorRemainingTime = protectorRemainingTime;
		this.protectorLastTime = protectorLastTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.level = 0;
		this.exp = 0;
		this.vipLv = 0;
		this.dailyActivity = 0;
		this.weekActivity = 0;
		this.protectorRemainingTime = 0;
		this.protectorLastTime = 0;
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
	public int getVipLv(){
		return this.vipLv;
	}
	public void setVipLv(int _vipLv){
		this.vipLv = _vipLv;
	}
	public int getDailyActivity(){
		return this.dailyActivity;
	}
	public void setDailyActivity(int _dailyActivity){
		this.dailyActivity = _dailyActivity;
	}
	public int getWeekActivity(){
		return this.weekActivity;
	}
	public void setWeekActivity(int _weekActivity){
		this.weekActivity = _weekActivity;
	}
	public long getProtectorRemainingTime(){
		return this.protectorRemainingTime;
	}
	public void setProtectorRemainingTime(long _protectorRemainingTime){
		this.protectorRemainingTime = _protectorRemainingTime;
	}
	public long getProtectorLastTime(){
		return this.protectorLastTime;
	}
	public void setProtectorLastTime(long _protectorLastTime){
		this.protectorLastTime = _protectorLastTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		level = (int)dch.bytesToParam("Integer");
		exp = (int)dch.bytesToParam("Integer");
		vipLv = (int)dch.bytesToParam("Integer");
		dailyActivity = (int)dch.bytesToParam("Integer");
		weekActivity = (int)dch.bytesToParam("Integer");
		protectorRemainingTime = (long)dch.bytesToParam("Long");
		protectorLastTime = (long)dch.bytesToParam("Long");
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
		if(!ech.paramToBytes(vipLv)){
			return false;
		}
		if(!ech.paramToBytes(dailyActivity)){
			return false;
		}
		if(!ech.paramToBytes(weekActivity)){
			return false;
		}
		if(!ech.paramToBytes(protectorRemainingTime)){
			return false;
		}
		if(!ech.paramToBytes(protectorLastTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "UpdPlayerInfoRs [code=" + code +",level=" + level +",exp=" + exp +",vipLv=" + vipLv +",dailyActivity=" + dailyActivity +",weekActivity=" + weekActivity +",protectorRemainingTime=" + protectorRemainingTime +",protectorLastTime=" + protectorLastTime +"]";
	}
}
