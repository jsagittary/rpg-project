package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class TaskRefreshListRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1715;
	//任务类型(1=主线 2=日常 3=周常)
	private int taskType;
	//任务列表
	private List<com.dykj.rpg.protocol.task.TaskRs> taskList;
	public TaskRefreshListRs(){
		this.taskList = new ArrayList<>();
	}
	public TaskRefreshListRs(int taskType,List<com.dykj.rpg.protocol.task.TaskRs> taskList){
		this.taskType = taskType;
		this.taskList = taskList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.taskType = 0;
		for(com.dykj.rpg.protocol.task.TaskRs value : taskList){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.taskList.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getTaskType(){
		return this.taskType;
	}
	public void setTaskType(int _taskType){
		this.taskType = _taskType;
	}
	public List<com.dykj.rpg.protocol.task.TaskRs> getTaskList(){
		return this.taskList;
	}
	public void setTaskList(List<com.dykj.rpg.protocol.task.TaskRs> _taskList){
		this.taskList = _taskList;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		taskType = (int)dch.bytesToParam("Integer");
		taskList = (List<com.dykj.rpg.protocol.task.TaskRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.task.TaskRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(taskType)){
			return false;
		}
		if(!ech.paramToBytes(taskList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "TaskRefreshListRs [code=" + code +",taskType=" + taskType +",taskList=" + taskList +"]";
	}
}
