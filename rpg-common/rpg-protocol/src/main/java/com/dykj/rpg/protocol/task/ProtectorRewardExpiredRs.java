package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ProtectorRewardExpiredRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1714;
	//true-成功, false-失败
	private boolean status;
	public ProtectorRewardExpiredRs(){
	}
	public ProtectorRewardExpiredRs(boolean status){
		this.status = status;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.status = false;
	}
	public short getCode(){
		return this.code;
	}
	public boolean getStatus(){
		return this.status;
	}
	public void setStatus(boolean _status){
		this.status = _status;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		status = (boolean)dch.bytesToParam("Boolean");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(status)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ProtectorRewardExpiredRs [code=" + code +",status=" + status +"]";
	}
}
