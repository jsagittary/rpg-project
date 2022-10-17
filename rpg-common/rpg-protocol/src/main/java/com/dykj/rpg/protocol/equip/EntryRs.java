package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EntryRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1302;
	// eq_entry_effect 的id，客户端根据这个去读取显示字符串
	private int entryEffectId;
	//词条的唯一表示,依据position在装备显示面板从上到下依次排序
	private int position;
	//词条的详细信息
	private List<com.dykj.rpg.protocol.equip.EntryUnitRs> entryUnitRs;
	//当前词条是否上锁
	private int isLock;
	//是否是必出词条(0.不是 1.是)
	private int isNecessary;
	public EntryRs(){
		this.entryUnitRs = new ArrayList<>();
	}
	public EntryRs(int entryEffectId,int position,List<com.dykj.rpg.protocol.equip.EntryUnitRs> entryUnitRs,int isLock,int isNecessary){
		this.entryEffectId = entryEffectId;
		this.position = position;
		this.entryUnitRs = entryUnitRs;
		this.isLock = isLock;
		this.isNecessary = isNecessary;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.entryEffectId = 0;
		this.position = 0;
		for(com.dykj.rpg.protocol.equip.EntryUnitRs value : entryUnitRs){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.entryUnitRs.clear();
		this.isLock = 0;
		this.isNecessary = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getEntryEffectId(){
		return this.entryEffectId;
	}
	public void setEntryEffectId(int _entryEffectId){
		this.entryEffectId = _entryEffectId;
	}
	public int getPosition(){
		return this.position;
	}
	public void setPosition(int _position){
		this.position = _position;
	}
	public List<com.dykj.rpg.protocol.equip.EntryUnitRs> getEntryUnitRs(){
		return this.entryUnitRs;
	}
	public void setEntryUnitRs(List<com.dykj.rpg.protocol.equip.EntryUnitRs> _entryUnitRs){
		this.entryUnitRs = _entryUnitRs;
	}
	public int getIsLock(){
		return this.isLock;
	}
	public void setIsLock(int _isLock){
		this.isLock = _isLock;
	}
	public int getIsNecessary(){
		return this.isNecessary;
	}
	public void setIsNecessary(int _isNecessary){
		this.isNecessary = _isNecessary;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		entryEffectId = (int)dch.bytesToParam("Integer");
		position = (int)dch.bytesToParam("Integer");
		entryUnitRs = (List<com.dykj.rpg.protocol.equip.EntryUnitRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.equip.EntryUnitRs>");
		isLock = (int)dch.bytesToParam("Integer");
		isNecessary = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(entryEffectId)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		if(!ech.paramToBytes(entryUnitRs)){
			return false;
		}
		if(!ech.paramToBytes(isLock)){
			return false;
		}
		if(!ech.paramToBytes(isNecessary)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EntryRs [code=" + code +",entryEffectId=" + entryEffectId +",position=" + position +",entryUnitRs=" + entryUnitRs +",isLock=" + isLock +",isNecessary=" + isNecessary +"]";
	}
}
