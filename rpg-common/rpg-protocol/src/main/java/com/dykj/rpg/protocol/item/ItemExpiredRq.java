package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemExpiredRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1208;
	public ItemExpiredRq(){
	}
	public void release(){
		this.dch.release();
		this.ech.release();
	}
	public short getCode(){
		return this.code;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemExpiredRq [code=" + code +"]";
	}
}
