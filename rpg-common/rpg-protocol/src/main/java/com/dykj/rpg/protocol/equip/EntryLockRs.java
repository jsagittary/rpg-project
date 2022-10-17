package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EntryLockRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1312;
	//词条的信息
	private com.dykj.rpg.protocol.equip.EntryRs entry;
	public EntryLockRs(){
	}
	public EntryLockRs(com.dykj.rpg.protocol.equip.EntryRs entry){
		this.entry = entry;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.entry);
		this.entry = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.equip.EntryRs getEntry(){
		return this.entry;
	}
	public void setEntry(com.dykj.rpg.protocol.equip.EntryRs _entry){
		this.entry = _entry;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		entry = (com.dykj.rpg.protocol.equip.EntryRs)dch.bytesToParam("com.dykj.rpg.protocol.equip.EntryRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(entry)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EntryLockRs [code=" + code +",entry=" + entry +"]";
	}
}
