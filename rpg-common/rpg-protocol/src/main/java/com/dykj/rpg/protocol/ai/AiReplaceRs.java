package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiReplaceRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1907;
	//移动的雕纹信息1
	private com.dykj.rpg.protocol.item.ItemRs item1;
	//移动的雕纹信息2
	private com.dykj.rpg.protocol.item.ItemRs item2;
	public AiReplaceRs(){
	}
	public AiReplaceRs(com.dykj.rpg.protocol.item.ItemRs item1,com.dykj.rpg.protocol.item.ItemRs item2){
		this.item1 = item1;
		this.item2 = item2;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.item1);
		this.item1 = null;
		ProtocolPool.getInstance().restoreProtocol(this.item2);
		this.item2 = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.item.ItemRs getItem1(){
		return this.item1;
	}
	public void setItem1(com.dykj.rpg.protocol.item.ItemRs _item1){
		this.item1 = _item1;
	}
	public com.dykj.rpg.protocol.item.ItemRs getItem2(){
		return this.item2;
	}
	public void setItem2(com.dykj.rpg.protocol.item.ItemRs _item2){
		this.item2 = _item2;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		item1 = (com.dykj.rpg.protocol.item.ItemRs)dch.bytesToParam("com.dykj.rpg.protocol.item.ItemRs");
		item2 = (com.dykj.rpg.protocol.item.ItemRs)dch.bytesToParam("com.dykj.rpg.protocol.item.ItemRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(item1)){
			return false;
		}
		if(!ech.paramToBytes(item2)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiReplaceRs [code=" + code +",item1=" + item1 +",item2=" + item2 +"]";
	}
}
