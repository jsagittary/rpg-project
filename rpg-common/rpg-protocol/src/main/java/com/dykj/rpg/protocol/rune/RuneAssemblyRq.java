package com.dykj.rpg.protocol.rune;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RuneAssemblyRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2302;
	//技能id
	private int skillId;
	//符文id
	private int runeId;
	//符文装配栏位
	private int runePos;
	public RuneAssemblyRq(){
	}
	public RuneAssemblyRq(int skillId,int runeId,int runePos){
		this.skillId = skillId;
		this.runeId = runeId;
		this.runePos = runePos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.runeId = 0;
		this.runePos = 0;
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
	public int getRuneId(){
		return this.runeId;
	}
	public void setRuneId(int _runeId){
		this.runeId = _runeId;
	}
	public int getRunePos(){
		return this.runePos;
	}
	public void setRunePos(int _runePos){
		this.runePos = _runePos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		runeId = (int)dch.bytesToParam("Integer");
		runePos = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(runeId)){
			return false;
		}
		if(!ech.paramToBytes(runePos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RuneAssemblyRq [code=" + code +",skillId=" + skillId +",runeId=" + runeId +",runePos=" + runePos +"]";
	}
}
