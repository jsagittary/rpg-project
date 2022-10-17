package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleMapInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9015;
	//地图块id
	private int id;
	//地图块的序列号，从0开始
	private byte index;
	//地图块出口 1=上， 2=左， 3=下， 4=右
	private byte exit;
	//地图怪物信息
	private List<com.dykj.rpg.protocol.battle.MapMonsterInfo> monsterInfos;
	public BattleMapInfo(){
		this.monsterInfos = new ArrayList<>();
	}
	public BattleMapInfo(int id,byte index,byte exit,List<com.dykj.rpg.protocol.battle.MapMonsterInfo> monsterInfos){
		this.id = id;
		this.index = index;
		this.exit = exit;
		this.monsterInfos = monsterInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.index = 0;
		this.exit = 0;
		for(com.dykj.rpg.protocol.battle.MapMonsterInfo value : monsterInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.monsterInfos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public byte getIndex(){
		return this.index;
	}
	public void setIndex(byte _index){
		this.index = _index;
	}
	public byte getExit(){
		return this.exit;
	}
	public void setExit(byte _exit){
		this.exit = _exit;
	}
	public List<com.dykj.rpg.protocol.battle.MapMonsterInfo> getMonsterInfos(){
		return this.monsterInfos;
	}
	public void setMonsterInfos(List<com.dykj.rpg.protocol.battle.MapMonsterInfo> _monsterInfos){
		this.monsterInfos = _monsterInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (int)dch.bytesToParam("Integer");
		index = (byte)dch.bytesToParam("Byte");
		exit = (byte)dch.bytesToParam("Byte");
		monsterInfos = (List<com.dykj.rpg.protocol.battle.MapMonsterInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.MapMonsterInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(index)){
			return false;
		}
		if(!ech.paramToBytes(exit)){
			return false;
		}
		if(!ech.paramToBytes(monsterInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleMapInfo [code=" + code +",id=" + id +",index=" + index +",exit=" + exit +",monsterInfos=" + monsterInfos +"]";
	}
}
