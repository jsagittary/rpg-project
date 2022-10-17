package com.dykj.rpg.protocol.rune;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RuneReplaceRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2306;
	//技能id
	private int skillId;
	//源符文id
	private int sourceRuneId;
	//替换后的符文id
	private int newRuneId;
	//符文装配栏位
	private int runePos;
	public RuneReplaceRq(){
	}
	public RuneReplaceRq(int skillId,int sourceRuneId,int newRuneId,int runePos){
		this.skillId = skillId;
		this.sourceRuneId = sourceRuneId;
		this.newRuneId = newRuneId;
		this.runePos = runePos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.sourceRuneId = 0;
		this.newRuneId = 0;
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
	public int getSourceRuneId(){
		return this.sourceRuneId;
	}
	public void setSourceRuneId(int _sourceRuneId){
		this.sourceRuneId = _sourceRuneId;
	}
	public int getNewRuneId(){
		return this.newRuneId;
	}
	public void setNewRuneId(int _newRuneId){
		this.newRuneId = _newRuneId;
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
		sourceRuneId = (int)dch.bytesToParam("Integer");
		newRuneId = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(sourceRuneId)){
			return false;
		}
		if(!ech.paramToBytes(newRuneId)){
			return false;
		}
		if(!ech.paramToBytes(runePos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RuneReplaceRq [code=" + code +",skillId=" + skillId +",sourceRuneId=" + sourceRuneId +",newRuneId=" + newRuneId +",runePos=" + runePos +"]";
	}
}
