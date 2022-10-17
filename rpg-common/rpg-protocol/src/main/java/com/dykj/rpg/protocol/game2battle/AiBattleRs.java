package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiBattleRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8911;
	//物品ID
	private int itemId;
	//雕纹ID
	private int aiId;
	//雕纹的位置 -1表示未穿戴 1~6 为穿戴的技能
	private int position;
	//行为参数
	private List<Integer> aiConditionParam;
	//动作参数
	private List<Integer> aiActionParam;
	public AiBattleRs(){
		this.aiConditionParam = new ArrayList<>();
		this.aiActionParam = new ArrayList<>();
	}
	public AiBattleRs(int itemId,int aiId,int position,List<Integer> aiConditionParam,List<Integer> aiActionParam){
		this.itemId = itemId;
		this.aiId = aiId;
		this.position = position;
		this.aiConditionParam = aiConditionParam;
		this.aiActionParam = aiActionParam;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.itemId = 0;
		this.aiId = 0;
		this.position = 0;
		this.aiConditionParam.clear();
		this.aiActionParam.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getItemId(){
		return this.itemId;
	}
	public void setItemId(int _itemId){
		this.itemId = _itemId;
	}
	public int getAiId(){
		return this.aiId;
	}
	public void setAiId(int _aiId){
		this.aiId = _aiId;
	}
	public int getPosition(){
		return this.position;
	}
	public void setPosition(int _position){
		this.position = _position;
	}
	public List<Integer> getAiConditionParam(){
		return this.aiConditionParam;
	}
	public void setAiConditionParam(List<Integer> _aiConditionParam){
		this.aiConditionParam = _aiConditionParam;
	}
	public List<Integer> getAiActionParam(){
		return this.aiActionParam;
	}
	public void setAiActionParam(List<Integer> _aiActionParam){
		this.aiActionParam = _aiActionParam;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemId = (int)dch.bytesToParam("Integer");
		aiId = (int)dch.bytesToParam("Integer");
		position = (int)dch.bytesToParam("Integer");
		aiConditionParam = (List<Integer>)dch.bytesToParam("List<Integer>");
		aiActionParam = (List<Integer>)dch.bytesToParam("List<Integer>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(itemId)){
			return false;
		}
		if(!ech.paramToBytes(aiId)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		if(!ech.paramToBytes(aiConditionParam)){
			return false;
		}
		if(!ech.paramToBytes(aiActionParam)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiBattleRs [code=" + code +",itemId=" + itemId +",aiId=" + aiId +",position=" + position +",aiConditionParam=" + aiConditionParam +",aiActionParam=" + aiActionParam +"]";
	}
}
