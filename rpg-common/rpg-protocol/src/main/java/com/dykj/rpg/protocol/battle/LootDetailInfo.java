package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class LootDetailInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9027;
	// 1=道具物品，2=小物件
	private byte type;
	//1 = 装备,2 = 技能书,3 = 宝石,4 = 材料,5 = 碎片,6 = 消耗品, 7 = 货币,8 = 经验
	private int detailType;
	//type=1时为道具ID，type=2时为小物件ID
	private int detailId;
	//物品数量
	private int num;
	//小物件ID type=2时此值有效
	private int miniObjId;
	public LootDetailInfo(){
	}
	public LootDetailInfo(byte type,int detailType,int detailId,int num,int miniObjId){
		this.type = type;
		this.detailType = detailType;
		this.detailId = detailId;
		this.num = num;
		this.miniObjId = miniObjId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.type = 0;
		this.detailType = 0;
		this.detailId = 0;
		this.num = 0;
		this.miniObjId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public byte getType(){
		return this.type;
	}
	public void setType(byte _type){
		this.type = _type;
	}
	public int getDetailType(){
		return this.detailType;
	}
	public void setDetailType(int _detailType){
		this.detailType = _detailType;
	}
	public int getDetailId(){
		return this.detailId;
	}
	public void setDetailId(int _detailId){
		this.detailId = _detailId;
	}
	public int getNum(){
		return this.num;
	}
	public void setNum(int _num){
		this.num = _num;
	}
	public int getMiniObjId(){
		return this.miniObjId;
	}
	public void setMiniObjId(int _miniObjId){
		this.miniObjId = _miniObjId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		type = (byte)dch.bytesToParam("Byte");
		detailType = (int)dch.bytesToParam("Integer");
		detailId = (int)dch.bytesToParam("Integer");
		num = (int)dch.bytesToParam("Integer");
		miniObjId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(detailType)){
			return false;
		}
		if(!ech.paramToBytes(detailId)){
			return false;
		}
		if(!ech.paramToBytes(num)){
			return false;
		}
		if(!ech.paramToBytes(miniObjId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "LootDetailInfo [code=" + code +",type=" + type +",detailType=" + detailType +",detailId=" + detailId +",num=" + num +",miniObjId=" + miniObjId +"]";
	}
}
