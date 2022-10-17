package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattlePosition extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9020;
	//地图块的序列号
	private byte mapIndex;
	//x轴坐标*100,客户端使用时需要除以100
	private int posX;
	//z轴坐标*100,客户端使用时需要除以100
	private int posZ;
	//方向角度(0~360)
	private short direction;
	//角色移动方式 当BattleRoleInfo中action=2时有用 1=跑步，2=冲刺
	private byte moveType;
	public BattlePosition(){
	}
	public BattlePosition(byte mapIndex,int posX,int posZ,short direction,byte moveType){
		this.mapIndex = mapIndex;
		this.posX = posX;
		this.posZ = posZ;
		this.direction = direction;
		this.moveType = moveType;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.mapIndex = 0;
		this.posX = 0;
		this.posZ = 0;
		this.direction = 0;
		this.moveType = 0;
	}
	public short getCode(){
		return this.code;
	}
	public byte getMapIndex(){
		return this.mapIndex;
	}
	public void setMapIndex(byte _mapIndex){
		this.mapIndex = _mapIndex;
	}
	public int getPosX(){
		return this.posX;
	}
	public void setPosX(int _posX){
		this.posX = _posX;
	}
	public int getPosZ(){
		return this.posZ;
	}
	public void setPosZ(int _posZ){
		this.posZ = _posZ;
	}
	public short getDirection(){
		return this.direction;
	}
	public void setDirection(short _direction){
		this.direction = _direction;
	}
	public byte getMoveType(){
		return this.moveType;
	}
	public void setMoveType(byte _moveType){
		this.moveType = _moveType;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		mapIndex = (byte)dch.bytesToParam("Byte");
		posX = (int)dch.bytesToParam("Integer");
		posZ = (int)dch.bytesToParam("Integer");
		direction = (short)dch.bytesToParam("Short");
		moveType = (byte)dch.bytesToParam("Byte");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(mapIndex)){
			return false;
		}
		if(!ech.paramToBytes(posX)){
			return false;
		}
		if(!ech.paramToBytes(posZ)){
			return false;
		}
		if(!ech.paramToBytes(direction)){
			return false;
		}
		if(!ech.paramToBytes(moveType)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattlePosition [code=" + code +",mapIndex=" + mapIndex +",posX=" + posX +",posZ=" + posZ +",direction=" + direction +",moveType=" + moveType +"]";
	}
}
