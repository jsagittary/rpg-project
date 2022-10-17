package com.dykj.rpg.protocol.rune;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RuneUninstallRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2305;
	//true-成功, false-失败
	private boolean status;
	public RuneUninstallRs(){
	}
	public RuneUninstallRs(boolean status){
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
		return "RuneUninstallRs [code=" + code +",status=" + status +"]";
	}
}
