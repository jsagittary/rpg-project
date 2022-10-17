package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattlePotionInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9018;
	//药水ID
	private int potionId;
	//药水数量
	private int num;
	//使用cd 单位为毫秒
	private int cdTime;
	public BattlePotionInfo(){
	}
	public BattlePotionInfo(int potionId,int num,int cdTime){
		this.potionId = potionId;
		this.num = num;
		this.cdTime = cdTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.potionId = 0;
		this.num = 0;
		this.cdTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPotionId(){
		return this.potionId;
	}
	public void setPotionId(int _potionId){
		this.potionId = _potionId;
	}
	public int getNum(){
		return this.num;
	}
	public void setNum(int _num){
		this.num = _num;
	}
	public int getCdTime(){
		return this.cdTime;
	}
	public void setCdTime(int _cdTime){
		this.cdTime = _cdTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		potionId = (int)dch.bytesToParam("Integer");
		num = (int)dch.bytesToParam("Integer");
		cdTime = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(potionId)){
			return false;
		}
		if(!ech.paramToBytes(num)){
			return false;
		}
		if(!ech.paramToBytes(cdTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattlePotionInfo [code=" + code +",potionId=" + potionId +",num=" + num +",cdTime=" + cdTime +"]";
	}
}
