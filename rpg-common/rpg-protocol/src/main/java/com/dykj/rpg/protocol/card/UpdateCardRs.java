package com.dykj.rpg.protocol.card;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class UpdateCardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1805;
	private com.dykj.rpg.protocol.card.CardRs card;
	public UpdateCardRs(){
	}
	public UpdateCardRs(com.dykj.rpg.protocol.card.CardRs card){
		this.card = card;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.card);
		this.card = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.card.CardRs getCard(){
		return this.card;
	}
	public void setCard(com.dykj.rpg.protocol.card.CardRs _card){
		this.card = _card;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		card = (com.dykj.rpg.protocol.card.CardRs)dch.bytesToParam("com.dykj.rpg.protocol.card.CardRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(card)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "UpdateCardRs [code=" + code +",card=" + card +"]";
	}
}
