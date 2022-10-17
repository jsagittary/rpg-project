package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class UpdateActivityRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1708;
	//活跃度
	private int activity;
	//活跃度类型(1-日常 2-周常)
	private int activityType;
	public UpdateActivityRs(){
	}
	public UpdateActivityRs(int activity,int activityType){
		this.activity = activity;
		this.activityType = activityType;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.activity = 0;
		this.activityType = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getActivity(){
		return this.activity;
	}
	public void setActivity(int _activity){
		this.activity = _activity;
	}
	public int getActivityType(){
		return this.activityType;
	}
	public void setActivityType(int _activityType){
		this.activityType = _activityType;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		activity = (int)dch.bytesToParam("Integer");
		activityType = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(activity)){
			return false;
		}
		if(!ech.paramToBytes(activityType)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "UpdateActivityRs [code=" + code +",activity=" + activity +",activityType=" + activityType +"]";
	}
}
