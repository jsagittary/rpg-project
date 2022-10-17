package com.dykj.rpg.protocol.card;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class CardRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1804;
	//卡池id
	private int cardId;
	//按钮id
	private int buttonId;
	public CardRq(){
	}
	public CardRq(int cardId,int buttonId){
		this.cardId = cardId;
		this.buttonId = buttonId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.cardId = 0;
		this.buttonId = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		cardId = (int)dch.bytesToParam("Integer");
		buttonId = (int)dch.bytesToParam("Integer");
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
		return true;
	}
	public String toString() {
		return "CardRq [code=" + code +",cardId=" + cardId +",buttonId=" + buttonId +"]";
	}
}
