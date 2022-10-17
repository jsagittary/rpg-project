package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiUpdateParamRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1909;
	//雕纹信息
	private com.dykj.rpg.protocol.item.ItemRs item;
	public AiUpdateParamRs(){
	}
	public AiUpdateParamRs(com.dykj.rpg.protocol.item.ItemRs item){
		this.item = item;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.item);
		this.item = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.item.ItemRs getItem(){
		return this.item;
	}
	public void setItem(com.dykj.rpg.protocol.item.ItemRs _item){
		this.item = _item;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		item = (com.dykj.rpg.protocol.item.ItemRs)dch.bytesToParam("com.dykj.rpg.protocol.item.ItemRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(item)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiUpdateParamRs [code=" + code +",item=" + item +"]";
	}
}
