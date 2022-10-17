package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class UpdateTaskListRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1705;
	//任务列表
	private List<com.dykj.rpg.protocol.task.TaskRs> taskList;
	public UpdateTaskListRs(){
		this.taskList = new ArrayList<>();
	}
	public UpdateTaskListRs(List<com.dykj.rpg.protocol.task.TaskRs> taskList){
		this.taskList = taskList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.task.TaskRs value : taskList){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.taskList.clear();
	}
	public short getCode(){
		return this.code;
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
		taskList = (List<com.dykj.rpg.protocol.task.TaskRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.task.TaskRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(taskList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "UpdateTaskListRs [code=" + code +",taskList=" + taskList +"]";
	}
}
