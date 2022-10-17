package com.dykj.rpg.protocol.card;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class CardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1801;
	//卡池id
	private int cardId;
	//按钮id
	private int buttonId;
	//按钮冷却截止时间
	private long buttonCutoffTime;
	//按钮抽取次数
	private int buttonExtractNumber;
	public CardRs(){
	}
	public CardRs(int cardId,int buttonId,long buttonCutoffTime,int buttonExtractNumber){
		this.cardId = cardId;
		this.buttonId = buttonId;
		this.buttonCutoffTime = buttonCutoffTime;
		this.buttonExtractNumber = buttonExtractNumber;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.cardId = 0;
		this.buttonId = 0;
		this.buttonCutoffTime = 0;
		this.buttonExtractNumber = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getCardId(){
		return this.cardId;
	}
	public void setCardId(int _cardId){
		this.cardId = _cardId;
	}
	public int getButtonId(){
		return this.buttonId;
	}
	public void setButtonId(int _buttonId){
		this.buttonId = _buttonId;
	}
	public long getButtonCutoffTime(){
		return this.buttonCutoffTime;
	}
	public void setButtonCutoffTime(long _buttonCutoffTime){
		this.buttonCutoffTime = _buttonCutoffTime;
	}
	public int getButtonExtractNumber(){
		return this.buttonExtractNumber;
	}
	public void setButtonExtractNumber(int _buttonExtractNumber){
		this.buttonExtractNumber = _buttonExtractNumber;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		cardId = (int)dch.bytesToParam("Integer");
		buttonId = (int)dch.bytesToParam("Integer");
		buttonCutoffTime = (long)dch.bytesToParam("Long");
		buttonExtractNumber = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(cardId)){
			return false;
		}
		if(!ech.paramToBytes(buttonId)){
			return false;
		}
		if(!ech.paramToBytes(buttonCutoffTime)){
			return false;
		}
		if(!ech.paramToBytes(buttonExtractNumber)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "CardRs [code=" + code +",cardId=" + cardId +",buttonId=" + buttonId +",buttonCutoffTime=" + buttonCutoffTime +",buttonExtractNumber=" + buttonExtractNumber +"]";
	}
}
