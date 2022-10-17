package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ReceiveTaskRewardsRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1706;
	//领取类型(1-普通奖励, 2-守护者奖励, 3-一键领取所有奖励)
	private int type;
	//任务id列表
	private List<Integer> taskIdList;
	public ReceiveTaskRewardsRq(){
		this.taskIdList = new ArrayList<>();
	}
	public ReceiveTaskRewardsRq(int type,List<Integer> taskIdList){
		this.type = type;
		this.taskIdList = taskIdList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.type = 0;
		this.taskIdList.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int _type){
		this.type = _type;
	}
	public List<Integer> getTaskIdList(){
		return this.taskIdList;
	}
	public void setTaskIdList(List<Integer> _taskIdList){
		this.taskIdList = _taskIdList;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		type = (int)dch.bytesToParam("Integer");
		taskIdList = (List<Integer>)dch.bytesToParam("List<Integer>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(taskIdList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ReceiveTaskRewardsRq [code=" + code +",type=" + type +",taskIdList=" + taskIdList +"]";
	}
}
