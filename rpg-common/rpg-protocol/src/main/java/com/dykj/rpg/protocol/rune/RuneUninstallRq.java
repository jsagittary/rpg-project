package com.dykj.rpg.protocol.rune;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RuneUninstallRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2304;
	//技能id
	private int skillId;
	//符文id
	private int runeId;
	public RuneUninstallRq(){
	}
	public RuneUninstallRq(int skillId,int runeId){
		this.skillId = skillId;
		this.runeId = runeId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.runeId = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		runeId = (int)dch.bytesToParam("Integer");
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
		return true;
	}
	public String toString() {
		return "RuneUninstallRq [code=" + code +",skillId=" + skillId +",runeId=" + runeId +"]";
	}
}
