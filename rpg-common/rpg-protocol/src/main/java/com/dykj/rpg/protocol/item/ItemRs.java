package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1201;
	//服务器唯一id
	private long instId;
	//道具id
	private int itemId;
	//道具类型
	private int itemType;
	//道具数量
	private int itemNum;
	//如果是装备,装备的详细信息
	private com.dykj.rpg.protocol.equip.EquipRs equip;
	//如果是雕纹,雕纹的详细信息
	private com.dykj.rpg.protocol.ai.AiRs ai;
	//过期时间
	private long expiration;
	//0-不可上锁 1-已解锁 2-已上锁
	private int isLock;
	public ItemRs(){
	}
	public ItemRs(long instId,int itemId,int itemType,int itemNum,com.dykj.rpg.protocol.equip.EquipRs equip,com.dykj.rpg.protocol.ai.AiRs ai,long expiration,int isLock){
		this.instId = instId;
		this.itemId = itemId;
		this.itemType = itemType;
		this.itemNum = itemNum;
		this.equip = equip;
		this.ai = ai;
		this.expiration = expiration;
		this.isLock = isLock;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
		this.itemId = 0;
		this.itemType = 0;
		this.itemNum = 0;
		ProtocolPool.getInstance().restoreProtocol(this.equip);
		this.equip = null;
		ProtocolPool.getInstance().restoreProtocol(this.ai);
		this.ai = null;
		this.expiration = 0;
		this.isLock = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getInstId(){
		return this.instId;
	}
	public void setInstId(long _instId){
		this.instId = _instId;
	}
	public int getItemId(){
		return this.itemId;
	}
	public void setItemId(int _itemId){
		this.itemId = _itemId;
	}
	public int getItemType(){
		return this.itemType;
	}
	public void setItemType(int _itemType){
		this.itemType = _itemType;
	}
	public int getItemNum(){
		return this.itemNum;
	}
	public void setItemNum(int _itemNum){
		this.itemNum = _itemNum;
	}
	public com.dykj.rpg.protocol.equip.EquipRs getEquip(){
		return this.equip;
	}
	public void setEquip(com.dykj.rpg.protocol.equip.EquipRs _equip){
		this.equip = _equip;
	}
	public com.dykj.rpg.protocol.ai.AiRs getAi(){
		return this.ai;
	}
	public void setAi(com.dykj.rpg.protocol.ai.AiRs _ai){
		this.ai = _ai;
	}
	public long getExpiration(){
		return this.expiration;
	}
	public void setExpiration(long _expiration){
		this.expiration = _expiration;
	}
	public int getIsLock(){
		return this.isLock;
	}
	public void setIsLock(int _isLock){
		this.isLock = _isLock;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
		itemId = (int)dch.bytesToParam("Integer");
		itemType = (int)dch.bytesToParam("Integer");
		itemNum = (int)dch.bytesToParam("Integer");
		equip = (com.dykj.rpg.protocol.equip.EquipRs)dch.bytesToParam("com.dykj.rpg.protocol.equip.EquipRs");
		ai = (com.dykj.rpg.protocol.ai.AiRs)dch.bytesToParam("com.dykj.rpg.protocol.ai.AiRs");
		expiration = (long)dch.bytesToParam("Long");
		isLock = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(instId)){
			return false;
		}
		if(!ech.paramToBytes(itemId)){
			return false;
		}
		if(!ech.paramToBytes(itemType)){
			return false;
		}
		if(!ech.paramToBytes(itemNum)){
			return false;
		}
		if(!ech.paramToBytes(equip)){
			return false;
		}
		if(!ech.paramToBytes(ai)){
			return false;
		}
		if(!ech.paramToBytes(expiration)){
			return false;
		}
		if(!ech.paramToBytes(isLock)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemRs [code=" + code +",instId=" + instId +",itemId=" + itemId +",itemType=" + itemType +",itemNum=" + itemNum +",equip=" + equip +",ai=" + ai +",expiration=" + expiration +",isLock=" + isLock +"]";
	}
}
