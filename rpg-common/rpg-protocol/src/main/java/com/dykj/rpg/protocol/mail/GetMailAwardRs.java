package com.dykj.rpg.protocol.mail;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GetMailAwardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2012;
	private com.dykj.rpg.protocol.mail.MailRs mail;
	public GetMailAwardRs(){
	}
	public GetMailAwardRs(com.dykj.rpg.protocol.mail.MailRs mail){
		this.mail = mail;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.mail);
		this.mail = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.mail.MailRs getMail(){
		return this.mail;
	}
	public void setMail(com.dykj.rpg.protocol.mail.MailRs _mail){
		this.mail = _mail;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		mail = (com.dykj.rpg.protocol.mail.MailRs)dch.bytesToParam("com.dykj.rpg.protocol.mail.MailRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(mail)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "GetMailAwardRs [code=" + code +",mail=" + mail +"]";
	}
}
