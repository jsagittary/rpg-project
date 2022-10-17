package com.dykj.rpg.protocol.player;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class PlayerRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1001;
	//玩家id
	private int playerId;
	//玩家名字
	private String name;
	//玩家职业
	private int profession;
	// 0未选中 1选中
	private int choose;
	//玩家等级
	private int level;
	//背包容量
	private int backCapacity;
	//vip等级
	private int vip;
	//经验
	private int exp;
	public PlayerRs(){
	}
	public PlayerRs(int playerId,String name,int profession,int choose,int level,int backCapacity,int vip,int exp){
		this.playerId = playerId;
		this.name = name;
		this.profession = profession;
		this.choose = choose;
		this.level = level;
		this.backCapacity = backCapacity;
		this.vip = vip;
		this.exp = exp;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.playerId = 0;
		this.name = null;
		this.profession = 0;
		this.choose = 0;
		this.level = 0;
		this.backCapacity = 0;
		this.vip = 0;
		this.exp = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPlayerId(){
		return this.playerId;
	}
	public void setPlayerId(int _playerId){
		this.playerId = _playerId;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String _name){
		this.name = _name;
	}
	public int getProfession(){
		return this.profession;
	}
	public void setProfession(int _profession){
		this.profession = _profession;
	}
	public int getChoose(){
		return this.choose;
	}
	public void setChoose(int _choose){
		this.choose = _choose;
	}
	public int getLevel(){
		return this.level;
	}
	public void setLevel(int _level){
		this.level = _level;
	}
	public int getBackCapacity(){
		return this.backCapacity;
	}
	public void setBackCapacity(int _backCapacity){
		this.backCapacity = _backCapacity;
	}
	public int getVip(){
		return this.vip;
	}
	public void setVip(int _vip){
		this.vip = _vip;
	}
	public int getExp(){
		return this.exp;
	}
	public void setExp(int _exp){
		this.exp = _exp;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		playerId = (int)dch.bytesToParam("Integer");
		name = (String)dch.bytesToParam("String");
		profession = (int)dch.bytesToParam("Integer");
		choose = (int)dch.bytesToParam("Integer");
		level = (int)dch.bytesToParam("Integer");
		backCapacity = (int)dch.bytesToParam("Integer");
		vip = (int)dch.bytesToParam("Integer");
		exp = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(playerId)){
			return false;
		}
		if(!ech.paramToBytes(name)){
			return false;
		}
		if(!ech.paramToBytes(profession)){
			return false;
		}
		if(!ech.paramToBytes(choose)){
			return false;
		}
		if(!ech.paramToBytes(level)){
			return false;
		}
		if(!ech.paramToBytes(backCapacity)){
			return false;
		}
		if(!ech.paramToBytes(vip)){
			return false;
		}
		if(!ech.paramToBytes(exp)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "PlayerRs [code=" + code +",playerId=" + playerId +",name=" + name +",profession=" + profession +",choose=" + choose +",level=" + level +",backCapacity=" + backCapacity +",vip=" + vip +",exp=" + exp +"]";
	}
}
