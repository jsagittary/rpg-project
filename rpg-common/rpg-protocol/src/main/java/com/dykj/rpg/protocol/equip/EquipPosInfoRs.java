package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipPosInfoRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1310;
	//装备栏强化位置信息
	private List<com.dykj.rpg.protocol.equip.EquipPosUpRs> equipPosInfos;
	public EquipPosInfoRs(){
		this.equipPosInfos = new ArrayList<>();
	}
	public EquipPosInfoRs(List<com.dykj.rpg.protocol.equip.EquipPosUpRs> equipPosInfos){
		this.equipPosInfos = equipPosInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.equip.EquipPosUpRs value : equipPosInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.equipPosInfos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.equip.EquipPosUpRs> getEquipPosInfos(){
		return this.equipPosInfos;
	}
	public void setEquipPosInfos(List<com.dykj.rpg.protocol.equip.EquipPosUpRs> _equipPosInfos){
		this.equipPosInfos = _equipPosInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		equipPosInfos = (List<com.dykj.rpg.protocol.equip.EquipPosUpRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.equip.EquipPosUpRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(equipPosInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipPosInfoRs [code=" + code +",equipPosInfos=" + equipPosInfos +"]";
	}
}
