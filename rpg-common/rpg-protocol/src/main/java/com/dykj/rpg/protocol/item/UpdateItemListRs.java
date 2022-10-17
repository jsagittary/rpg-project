package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class UpdateItemListRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1203;
	//操作类型 1001-GM 1002-出售 1003-兑换 1004-分解 1005-使用 1006-丢弃 1007-挂机奖励 1008-挂机奖励 1009-关卡通关奖励 1010-战斗使用物品 1011-培养灵魂之影技能消耗 1012-培养装备栏等级消耗 1013-任务完成奖励 1014-守护者奖励 1015-活跃度奖励 1016-开通守护者 1017-抽卡奖励
	private int operation;
	//0-无 1-弹窗
	private int type;
	//true-成功 false-失败
	private boolean status;
	private List<com.dykj.rpg.protocol.item.ItemRs> itemArr;
	public UpdateItemListRs(){
		this.itemArr = new ArrayList<>();
	}
	public UpdateItemListRs(int operation,int type,boolean status,List<com.dykj.rpg.protocol.item.ItemRs> itemArr){
		this.operation = operation;
		this.type = type;
		this.status = status;
		this.itemArr = itemArr;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.operation = 0;
		this.type = 0;
		this.status = false;
		for(com.dykj.rpg.protocol.item.ItemRs value : itemArr){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.itemArr.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getOperation(){
		return this.operation;
	}
	public void setOperation(int _operation){
		this.operation = _operation;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int _type){
		this.type = _type;
	}
	public boolean getStatus(){
		return this.status;
	}
	public void setStatus(boolean _status){
		this.status = _status;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getItemArr(){
		return this.itemArr;
	}
	public void setItemArr(List<com.dykj.rpg.protocol.item.ItemRs> _itemArr){
		this.itemArr = _itemArr;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		operation = (int)dch.bytesToParam("Integer");
		type = (int)dch.bytesToParam("Integer");
		status = (boolean)dch.bytesToParam("Boolean");
		itemArr = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(operation)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(status)){
			return false;
		}
		if(!ech.paramToBytes(itemArr)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "UpdateItemListRs [code=" + code +",operation=" + operation +",type=" + type +",status=" + status +",itemArr=" + itemArr +"]";
	}
}
