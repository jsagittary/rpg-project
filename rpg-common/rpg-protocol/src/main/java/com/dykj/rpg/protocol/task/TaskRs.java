package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class TaskRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1701;
	//任务id
	private int taskId;
	//任务类型(1=主线 2=日常 3=周常)
	private int taskType;
	//任务状态(1-未完成 2-已完成)
	private int taskStatus;
	//任务进度
	private int taskSchedule;
	//任务奖励领取状态(0-未领取, 1-已领取)
	private int taskRewardStatus;
	//守护者奖励领取状态(0-未领取, 1-已领取)
	private int protectorRewardStatus;
	public TaskRs(){
	}
	public TaskRs(int taskId,int taskType,int taskStatus,int taskSchedule,int taskRewardStatus,int protectorRewardStatus){
		this.taskId = taskId;
		this.taskType = taskType;
		this.taskStatus = taskStatus;
		this.taskSchedule = taskSchedule;
		this.taskRewardStatus = taskRewardStatus;
		this.protectorRewardStatus = protectorRewardStatus;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.taskId = 0;
		this.taskType = 0;
		this.taskStatus = 0;
		this.taskSchedule = 0;
		this.taskRewardStatus = 0;
		this.protectorRewardStatus = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getTaskId(){
		return this.taskId;
	}
	public void setTaskId(int _taskId){
		this.taskId = _taskId;
	}
	public int getTaskType(){
		return this.taskType;
	}
	public void setTaskType(int _taskType){
		this.taskType = _taskType;
	}
	public int getTaskStatus(){
		return this.taskStatus;
	}
	public void setTaskStatus(int _taskStatus){
		this.taskStatus = _taskStatus;
	}
	public int getTaskSchedule(){
		return this.taskSchedule;
	}
	public void setTaskSchedule(int _taskSchedule){
		this.taskSchedule = _taskSchedule;
	}
	public int getTaskRewardStatus(){
		return this.taskRewardStatus;
	}
	public void setTaskRewardStatus(int _taskRewardStatus){
		this.taskRewardStatus = _taskRewardStatus;
	}
	public int getProtectorRewardStatus(){
		return this.protectorRewardStatus;
	}
	public void setProtectorRewardStatus(int _protectorRewardStatus){
		this.protectorRewardStatus = _protectorRewardStatus;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		taskId = (int)dch.bytesToParam("Integer");
		taskType = (int)dch.bytesToParam("Integer");
		taskStatus = (int)dch.bytesToParam("Integer");
		taskSchedule = (int)dch.bytesToParam("Integer");
		taskRewardStatus = (int)dch.bytesToParam("Integer");
		protectorRewardStatus = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(taskId)){
			return false;
		}
		if(!ech.paramToBytes(taskType)){
			return false;
		}
		if(!ech.paramToBytes(taskStatus)){
			return false;
		}
		if(!ech.paramToBytes(taskSchedule)){
			return false;
		}
		if(!ech.paramToBytes(taskRewardStatus)){
			return false;
		}
		if(!ech.paramToBytes(protectorRewardStatus)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "TaskRs [code=" + code +",taskId=" + taskId +",taskType=" + taskType +",taskStatus=" + taskStatus +",taskSchedule=" + taskSchedule +",taskRewardStatus=" + taskRewardStatus +",protectorRewardStatus=" + protectorRewardStatus +"]";
	}
}
