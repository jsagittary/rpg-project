package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerAttachedRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1002;
	//日活跃度
	private int dailyActivity;
	//周活跃度
	private int weekActivity;
	//是否已开通守护者(0-否 1-是)
	private int isProtector;
	//守护者奖励剩余时长(单位:分)
	private long protectorRemainingTime;
	//守护者奖励截止时间
	private long protectorLastTime;
	//活跃度奖励列表
	private String activityRewardList;
	public PlayerAttachedRs(){
	}
	public PlayerAttachedRs(int dailyActivity,int weekActivity,int isProtector,long protectorRemainingTime,long protectorLastTime,String activityRewardList){
		this.dailyActivity = dailyActivity;
		this.weekActivity = weekActivity;
		this.isProtector = isProtector;
		this.protectorRemainingTime = protectorRemainingTime;
		this.protectorLastTime = protectorLastTime;
		this.activityRewardList = activityRewardList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.dailyActivity = 0;
		this.weekActivity = 0;
		this.isProtector = 0;
		this.protectorRemainingTime = 0;
		this.protectorLastTime = 0;
		this.activityRewardList = null;
	}
	public short getCode(){
		return this.code;
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
	public int getIsProtector(){
		return this.isProtector;
	}
	public void setIsProtector(int _isProtector){
		this.isProtector = _isProtector;
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
	public String getActivityRewardList(){
		return this.activityRewardList;
	}
	public void setActivityRewardList(String _activityRewardList){
		this.activityRewardList = _activityRewardList;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		dailyActivity = (int)dch.bytesToParam("Integer");
		weekActivity = (int)dch.bytesToParam("Integer");
		isProtector = (int)dch.bytesToParam("Integer");
		protectorRemainingTime = (long)dch.bytesToParam("Long");
		protectorLastTime = (long)dch.bytesToParam("Long");
		activityRewardList = (String)dch.bytesToParam("String");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(dailyActivity)){
			return false;
		}
		if(!ech.paramToBytes(weekActivity)){
			return false;
		}
		if(!ech.paramToBytes(isProtector)){
			return false;
		}
		if(!ech.paramToBytes(protectorRemainingTime)){
			return false;
		}
		if(!ech.paramToBytes(protectorLastTime)){
			return false;
		}
		if(!ech.paramToBytes(activityRewardList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerAttachedRs [code=" + code +",dailyActivity=" + dailyActivity +",weekActivity=" + weekActivity +",isProtector=" + isProtector +",protectorRemainingTime=" + protectorRemainingTime +",protectorLastTime=" + protectorLastTime +",activityRewardList=" + activityRewardList +"]";
	}
}
