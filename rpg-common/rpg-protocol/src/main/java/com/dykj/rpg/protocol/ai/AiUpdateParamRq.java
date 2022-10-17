package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiUpdateParamRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1908;
	//物品ID
	private int itemId;
	//行为参数
	private int aiConditionParam;
	//动作参数
	private int aiActionParam;
	public AiUpdateParamRq(){
	}
	public AiUpdateParamRq(int itemId,int aiConditionParam,int aiActionParam){
		this.itemId = itemId;
		this.aiConditionParam = aiConditionParam;
		this.aiActionParam = aiActionParam;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.itemId = 0;
		this.aiConditionParam = 0;
		this.aiActionParam = 0;
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
	public int getAiConditionParam(){
		return this.aiConditionParam;
	}
	public void setAiConditionParam(int _aiConditionParam){
		this.aiConditionParam = _aiConditionParam;
	}
	public int getAiActionParam(){
		return this.aiActionParam;
	}
	public void setAiActionParam(int _aiActionParam){
		this.aiActionParam = _aiActionParam;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemId = (int)dch.bytesToParam("Integer");
		aiConditionParam = (int)dch.bytesToParam("Integer");
		aiActionParam = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(aiConditionParam)){
			return false;
		}
		if(!ech.paramToBytes(aiActionParam)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiUpdateParamRq [code=" + code +",itemId=" + itemId +",aiConditionParam=" + aiConditionParam +",aiActionParam=" + aiActionParam +"]";
	}
}
