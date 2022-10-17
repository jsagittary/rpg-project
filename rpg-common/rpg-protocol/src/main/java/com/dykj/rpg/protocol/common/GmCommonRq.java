package com.dykj.rpg.protocol.common;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GmCommonRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 103;
	//gm指令
	private String command;
	public GmCommonRq(){
	}
	public GmCommonRq(String command){
		this.command = command;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.command = null;
	}
	public short getCode(){
		return this.code;
	}
	public String getCommand(){
		return this.command;
	}
	public void setCommand(String _command){
		this.command = _command;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		command = (String)dch.bytesToParam("String");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(command)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "GmCommonRq [code=" + code +",command=" + command +"]";
	}
}
