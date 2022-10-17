package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1301;
	//装备的随机词条 entryIndex index =-1 为主属性
	private List<com.dykj.rpg.protocol.equip.EntryRs> entries;
	//-1表示未穿戴，1~10为装备栏位置
	private int equipPos;
	//装备基础评分
	private int equipScore;
	//装备总评分
	private int equipTotalScore;
	//装备上锁(1-已解锁 2-已上锁)
	private int equipLock;
	public EquipRs(){
		this.entries = new ArrayList<>();
	}
	public EquipRs(List<com.dykj.rpg.protocol.equip.EntryRs> entries,int equipPos,int equipScore,int equipTotalScore,int equipLock){
		this.entries = entries;
		this.equipPos = equipPos;
		this.equipScore = equipScore;
		this.equipTotalScore = equipTotalScore;
		this.equipLock = equipLock;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.equip.EntryRs value : entries){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.entries.clear();
		this.equipPos = 0;
		this.equipScore = 0;
		this.equipTotalScore = 0;
		this.equipLock = 0;
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.equip.EntryRs> getEntries(){
		return this.entries;
	}
	public void setEntries(List<com.dykj.rpg.protocol.equip.EntryRs> _entries){
		this.entries = _entries;
	}
	public int getEquipPos(){
		return this.equipPos;
	}
	public void setEquipPos(int _equipPos){
		this.equipPos = _equipPos;
	}
	public int getEquipScore(){
		return this.equipScore;
	}
	public void setEquipScore(int _equipScore){
		this.equipScore = _equipScore;
	}
	public int getEquipTotalScore(){
		return this.equipTotalScore;
	}
	public void setEquipTotalScore(int _equipTotalScore){
		this.equipTotalScore = _equipTotalScore;
	}
	public int getEquipLock(){
		return this.equipLock;
	}
	public void setEquipLock(int _equipLock){
		this.equipLock = _equipLock;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		entries = (List<com.dykj.rpg.protocol.equip.EntryRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.equip.EntryRs>");
		equipPos = (int)dch.bytesToParam("Integer");
		equipScore = (int)dch.bytesToParam("Integer");
		equipTotalScore = (int)dch.bytesToParam("Integer");
		equipLock = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(entries)){
			return false;
		}
		if(!ech.paramToBytes(equipPos)){
			return false;
		}
		if(!ech.paramToBytes(equipScore)){
			return false;
		}
		if(!ech.paramToBytes(equipTotalScore)){
			return false;
		}
		if(!ech.paramToBytes(equipLock)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipRs [code=" + code +",entries=" + entries +",equipPos=" + equipPos +",equipScore=" + equipScore +",equipTotalScore=" + equipTotalScore +",equipLock=" + equipLock +"]";
	}
}
