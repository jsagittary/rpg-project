package com.dykj.rpg.protocol.common;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ErrorMsg extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 100;
	//错误码
	private int errorCode;
	//哪条协议产生的错误码
	private int handlerId;
	//参数集合
	private List<String> prams;
	public ErrorMsg(){
		this.prams = new ArrayList<>();
	}
	public ErrorMsg(int errorCode,int handlerId,List<String> prams){
		this.errorCode = errorCode;
		this.handlerId = handlerId;
		this.prams = prams;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.errorCode = 0;
		this.handlerId = 0;
		this.prams.clear();
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
	public List<String> getPrams(){
		return this.prams;
	}
	public void setPrams(List<String> _prams){
		this.prams = _prams;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		errorCode = (int)dch.bytesToParam("Integer");
		handlerId = (int)dch.bytesToParam("Integer");
		prams = (List<String>)dch.bytesToParam("List<String>");
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
		if(!ech.paramToBytes(prams)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ErrorMsg [code=" + code +",errorCode=" + errorCode +",handlerId=" + handlerId +",prams=" + prams +"]";
	}
}
