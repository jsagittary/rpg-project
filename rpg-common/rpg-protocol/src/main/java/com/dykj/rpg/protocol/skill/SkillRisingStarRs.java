package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillRisingStarRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1425;
	//技能id
	private int skillId;
	//技能星级
	private int skillStarLevel;
	//true-成功, false-失败
	private boolean status;
	public SkillRisingStarRs(){
	}
	public SkillRisingStarRs(int skillId,int skillStarLevel,boolean status){
		this.skillId = skillId;
		this.skillStarLevel = skillStarLevel;
		this.status = status;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.skillStarLevel = 0;
		this.status = false;
	}
	public short getCode(){
		return this.code;
	}
	public int getSkillId(){
		return this.skillId;
	}
	public void setSkillId(int _skillId){
		this.skillId = _skillId;
	}
	public int getSkillStarLevel(){
		return this.skillStarLevel;
	}
	public void setSkillStarLevel(int _skillStarLevel){
		this.skillStarLevel = _skillStarLevel;
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
		skillId = (int)dch.bytesToParam("Integer");
		skillStarLevel = (int)dch.bytesToParam("Integer");
		status = (boolean)dch.bytesToParam("Boolean");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(skillStarLevel)){
			return false;
		}
		if(!ech.paramToBytes(status)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillRisingStarRs [code=" + code +",skillId=" + skillId +",skillStarLevel=" + skillStarLevel +",status=" + status +"]";
	}
}
