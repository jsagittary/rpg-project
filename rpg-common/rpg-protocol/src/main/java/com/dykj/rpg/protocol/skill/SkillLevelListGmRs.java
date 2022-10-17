package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillLevelListGmRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1411;
	private List<com.dykj.rpg.protocol.skill.SkillRs> list;
	//true-成功, false-失败
	private boolean status;
	public SkillLevelListGmRs(){
		this.list = new ArrayList<>();
	}
	public SkillLevelListGmRs(List<com.dykj.rpg.protocol.skill.SkillRs> list,boolean status){
		this.list = list;
		this.status = status;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.skill.SkillRs value : list){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.list.clear();
		this.status = false;
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.skill.SkillRs> getList(){
		return this.list;
	}
	public void setList(List<com.dykj.rpg.protocol.skill.SkillRs> _list){
		this.list = _list;
	}
	public boolean getStatus(){
		return this.status;
	}
	public void setStatus(boolean _status){
		this.status = _status;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		list = (List<com.dykj.rpg.protocol.skill.SkillRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.skill.SkillRs>");
		status = (boolean)dch.bytesToParam("Boolean");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(list)){
			return false;
		}
		if(!ech.paramToBytes(status)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillLevelListGmRs [code=" + code +",list=" + list +",status=" + status +"]";
	}
}
