package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SuccessConditionInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9007;
	//条件类型 对应 excel表 mis_condition 中的 condition_type字段
	private byte conditionType;
	//条件参数 对应 excel表 mis_condition 中的 condition_name
	private List<Integer> conditionParams;
	//完成情况
	private boolean result;
	//进度数据 进度类型的通关条件（如击杀100个怪）此数据有效，第一个数据为进度值，第二个数据为任务总值
	private List<Integer> progress;
	public SuccessConditionInfo(){
		this.conditionParams = new ArrayList<>();
		this.progress = new ArrayList<>();
	}
	public SuccessConditionInfo(byte conditionType,List<Integer> conditionParams,boolean result,List<Integer> progress){
		this.conditionType = conditionType;
		this.conditionParams = conditionParams;
		this.result = result;
		this.progress = progress;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.conditionType = 0;
		this.conditionParams.clear();
		this.result = false;
		this.progress.clear();
	}
	public short getCode(){
		return this.code;
	}
	public byte getConditionType(){
		return this.conditionType;
	}
	public void setConditionType(byte _conditionType){
		this.conditionType = _conditionType;
	}
	public List<Integer> getConditionParams(){
		return this.conditionParams;
	}
	public void setConditionParams(List<Integer> _conditionParams){
		this.conditionParams = _conditionParams;
	}
	public boolean getResult(){
		return this.result;
	}
	public void setResult(boolean _result){
		this.result = _result;
	}
	public List<Integer> getProgress(){
		return this.progress;
	}
	public void setProgress(List<Integer> _progress){
		this.progress = _progress;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		conditionType = (byte)dch.bytesToParam("Byte");
		conditionParams = (List<Integer>)dch.bytesToParam("List<Integer>");
		result = (boolean)dch.bytesToParam("Boolean");
		progress = (List<Integer>)dch.bytesToParam("List<Integer>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(conditionType)){
			return false;
		}
		if(!ech.paramToBytes(conditionParams)){
			return false;
		}
		if(!ech.paramToBytes(result)){
			return false;
		}
		if(!ech.paramToBytes(progress)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SuccessConditionInfo [code=" + code +",conditionType=" + conditionType +",conditionParams=" + conditionParams +",result=" + result +",progress=" + progress +"]";
	}
}
