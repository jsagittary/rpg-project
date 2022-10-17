package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ReceiveActivityRewardsRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1709;
	//活跃度奖励宝箱id
	private int id;
	public ReceiveActivityRewardsRq(){
	}
	public ReceiveActivityRewardsRq(int id){
		this.id = id;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ReceiveActivityRewardsRq [code=" + code +",id=" + id +"]";
	}
}
