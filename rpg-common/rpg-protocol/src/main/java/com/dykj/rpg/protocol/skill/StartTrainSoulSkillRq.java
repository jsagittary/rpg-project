package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class StartTrainSoulSkillRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1415;
	//训练灵魂之影的信息
	private List<com.dykj.rpg.protocol.skill.TrainSoulInfo> infos;
	public StartTrainSoulSkillRq(){
		this.infos = new ArrayList<>();
	}
	public StartTrainSoulSkillRq(List<com.dykj.rpg.protocol.skill.TrainSoulInfo> infos){
		this.infos = infos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.skill.TrainSoulInfo value : infos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.infos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.skill.TrainSoulInfo> getInfos(){
		return this.infos;
	}
	public void setInfos(List<com.dykj.rpg.protocol.skill.TrainSoulInfo> _infos){
		this.infos = _infos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		infos = (List<com.dykj.rpg.protocol.skill.TrainSoulInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.TrainSoulInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(infos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "StartTrainSoulSkillRq [code=" + code +",infos=" + infos +"]";
	}
}
