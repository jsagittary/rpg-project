package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillUpgradeRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1410;
	//技能id
	private int skillId;
	//技能等级
	private int skillLevel;
	//true-成功, false-失败
	private boolean status;
	public SkillUpgradeRs(){
	}
	public SkillUpgradeRs(int skillId,int skillLevel,boolean status){
		this.skillId = skillId;
		this.skillLevel = skillLevel;
		this.status = status;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.skillLevel = 0;
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
	public int getSkillLevel(){
		return this.skillLevel;
	}
	public void setSkillLevel(int _skillLevel){
		this.skillLevel = _skillLevel;
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
		skillLevel = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(skillLevel)){
			return false;
		}
		if(!ech.paramToBytes(status)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillUpgradeRs [code=" + code +",skillId=" + skillId +",skillLevel=" + skillLevel +",status=" + status +"]";
	}
}
