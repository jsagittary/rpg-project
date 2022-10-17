package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleErrorMessage extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9009;
	//错误码 0=成功，其他值为失败
	private int errorCode;
	// 1=开始游戏的请求,2=释放技能请求
	private int handlerId;
	//错误信息
	private String errMsg;
	public BattleErrorMessage(){
	}
	public BattleErrorMessage(int errorCode,int handlerId,String errMsg){
		this.errorCode = errorCode;
		this.handlerId = handlerId;
		this.errMsg = errMsg;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.errorCode = 0;
		this.handlerId = 0;
		this.errMsg = null;
	}
	public short getCode(){
		return this.code;
	}
	public int getErrorCode(){
		return this.errorCode;
	}
	public void setErrorCode(int _errorCode){
		this.errorCode = _errorCode;
	}
	public int getHandlerId(){
		return this.handlerId;
	}
	public void setHandlerId(int _handlerId){
		this.handlerId = _handlerId;
	}
	public String getErrMsg(){
		return this.errMsg;
	}
	public void setErrMsg(String _errMsg){
		this.errMsg = _errMsg;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		errorCode = (int)dch.bytesToParam("Integer");
		handlerId = (int)dch.bytesToParam("Integer");
		errMsg = (String)dch.bytesToParam("String");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(errorCode)){
			return false;
		}
		if(!ech.paramToBytes(handlerId)){
			return false;
		}
		if(!ech.paramToBytes(errMsg)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleErrorMessage [code=" + code +",errorCode=" + errorCode +",handlerId=" + handlerId +",errMsg=" + errMsg +"]";
	}
}
