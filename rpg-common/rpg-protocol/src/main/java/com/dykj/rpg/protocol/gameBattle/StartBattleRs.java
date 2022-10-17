package com.dykj.rpg.protocol.gameBattle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class StartBattleRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1602;
	//战斗服ip
	private String addr;
	//战斗服端口
	private int port;
	//战斗类型
	private byte battleType;
	//战场ID，客户端与战斗服使用
	private int battleId;
	public StartBattleRs(){
	}
	public StartBattleRs(String addr,int port,byte battleType,int battleId){
		this.addr = addr;
		this.port = port;
		this.battleType = battleType;
		this.battleId = battleId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.addr = null;
		this.port = 0;
		this.battleType = 0;
		this.battleId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public String getAddr(){
		return this.addr;
	}
	public void setAddr(String _addr){
		this.addr = _addr;
	}
	public int getPort(){
		return this.port;
	}
	public void setPort(int _port){
		this.port = _port;
	}
	public byte getBattleType(){
		return this.battleType;
	}
	public void setBattleType(byte _battleType){
		this.battleType = _battleType;
	}
	public int getBattleId(){
		return this.battleId;
	}
	public void setBattleId(int _battleId){
		this.battleId = _battleId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		addr = (String)dch.bytesToParam("String");
		port = (int)dch.bytesToParam("Integer");
		battleType = (byte)dch.bytesToParam("Byte");
		battleId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(addr)){
			return false;
		}
		if(!ech.paramToBytes(port)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		if(!ech.paramToBytes(battleId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "StartBattleRs [code=" + code +",addr=" + addr +",port=" + port +",battleType=" + battleType +",battleId=" + battleId +"]";
	}
}
