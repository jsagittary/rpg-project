package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SkillRisingStar extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1423;
	//道具id
	private int itemId;
	//道具数量
	private int itemNum;
	public SkillRisingStar(){
	}
	public SkillRisingStar(int itemId,int itemNum){
		this.itemId = itemId;
		this.itemNum = itemNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.itemId = 0;
		this.itemNum = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getItemId(){
		return this.itemId;
	}
	public void setItemId(int _itemId){
		this.itemId = _itemId;
	}
	public int getItemNum(){
		return this.itemNum;
	}
	public void setItemNum(int _itemNum){
		this.itemNum = _itemNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemId = (int)dch.bytesToParam("Integer");
		itemNum = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(itemId)){
			return false;
		}
		if(!ech.paramToBytes(itemNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SkillRisingStar [code=" + code +",itemId=" + itemId +",itemNum=" + itemNum +"]";
	}
}
