package com.dykj.rpg.protocol.card;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class CardListRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1803;
	//抽卡记录列表
	private List<com.dykj.rpg.protocol.card.CardRs> cardList;
	public CardListRs(){
		this.cardList = new ArrayList<>();
	}
	public CardListRs(List<com.dykj.rpg.protocol.card.CardRs> cardList){
		this.cardList = cardList;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.card.CardRs value : cardList){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.cardList.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.card.CardRs> getCardList(){
		return this.cardList;
	}
	public void setCardList(List<com.dykj.rpg.protocol.card.CardRs> _cardList){
		this.cardList = _cardList;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		cardList = (List<com.dykj.rpg.protocol.card.CardRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.card.CardRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(cardList)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "CardListRs [code=" + code +",cardList=" + cardList +"]";
	}
}
