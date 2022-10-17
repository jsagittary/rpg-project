package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ReceiveActivityRewardsRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1710;
	//活跃度奖励领取状态(0-未领取, 1-已领取)
	private int activityReceiveStatus;
	public ReceiveActivityRewardsRs(){
	}
	public ReceiveActivityRewardsRs(int activityReceiveStatus){
		this.activityReceiveStatus = activityReceiveStatus;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.activityReceiveStatus = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getActivityReceiveStatus(){
		return this.activityReceiveStatus;
	}
	public void setActivityReceiveStatus(int _activityReceiveStatus){
		this.activityReceiveStatus = _activityReceiveStatus;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		activityReceiveStatus = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(activityReceiveStatus)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ReceiveActivityRewardsRs [code=" + code +",activityReceiveStatus=" + activityReceiveStatus +"]";
	}
}
