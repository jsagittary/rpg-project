package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RegisterMsgRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1106;
	//职业
	private int profession;
	//名字
	private String name;
	//性别
	private int sex;
	public RegisterMsgRq(){
	}
	public RegisterMsgRq(int profession,String name,int sex){
		this.profession = profession;
		this.name = name;
		this.sex = sex;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.profession = 0;
		this.name = null;
		this.sex = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getProfession(){
		return this.profession;
	}
	public void setProfession(int _profession){
		this.profession = _profession;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String _name){
		this.name = _name;
	}
	public int getSex(){
		return this.sex;
	}
	public void setSex(int _sex){
		this.sex = _sex;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		profession = (int)dch.bytesToParam("Integer");
		name = (String)dch.bytesToParam("String");
		sex = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(profession)){
			return false;
		}
		if(!ech.paramToBytes(name)){
			return false;
		}
		if(!ech.paramToBytes(sex)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RegisterMsgRq [code=" + code +",profession=" + profession +",name=" + name +",sex=" + sex +"]";
	}
}
