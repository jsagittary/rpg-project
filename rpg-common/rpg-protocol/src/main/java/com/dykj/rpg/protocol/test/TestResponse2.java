package com.dykj.rpg.protocol.test;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class TestResponse2 extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 3;
	//响应数据
	private List<Byte> data;
	public TestResponse2(){
		this.data = new ArrayList<>();
	}
	public TestResponse2(List<Byte> data){
		this.data = data;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.data.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<Byte> getData(){
		return this.data;
	}
	public void setData(List<Byte> _data){
		this.data = _data;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		data = (List<Byte>)dch.bytesToParam("List<Byte>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(data)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "TestResponse2 [code=" + code +",data=" + data +"]";
	}
}
