package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillAttribute extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8904;
	//skill_character_basic kill_character_carrier skill_character_effect skill_character_state表的主键
	private int tableKey;
	//skill_attr_basic 的id
	private int skillAttrId;
	//修正参数
	private int prams;
	//值
	private int value;
	public SkillAttribute(){
	}
	public SkillAttribute(int tableKey,int skillAttrId,int prams,int value){
		this.tableKey = tableKey;
		this.skillAttrId = skillAttrId;
		this.prams = prams;
		this.value = value;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.tableKey = 0;
		this.skillAttrId = 0;
		this.prams = 0;
		this.value = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getTableKey(){
		return this.tableKey;
	}
	public void setTableKey(int _tableKey){
		this.tableKey = _tableKey;
	}
	public int getSkillAttrId(){
		return this.skillAttrId;
	}
	public void setSkillAttrId(int _skillAttrId){
		this.skillAttrId = _skillAttrId;
	}
	public int getPrams(){
		return this.prams;
	}
	public void setPrams(int _prams){
		this.prams = _prams;
	}
	public int getValue(){
		return this.value;
	}
	public void setValue(int _value){
		this.value = _value;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		tableKey = (int)dch.bytesToParam("Integer");
		skillAttrId = (int)dch.bytesToParam("Integer");
		prams = (int)dch.bytesToParam("Integer");
		value = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(tableKey)){
			return false;
		}
		if(!ech.paramToBytes(skillAttrId)){
			return false;
		}
		if(!ech.paramToBytes(prams)){
			return false;
		}
		if(!ech.paramToBytes(value)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillAttribute [code=" + code +",tableKey=" + tableKey +",skillAttrId=" + skillAttrId +",prams=" + prams +",value=" + value +"]";
	}
}
