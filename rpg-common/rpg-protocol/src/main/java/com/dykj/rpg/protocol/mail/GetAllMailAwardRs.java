package com.dykj.rpg.protocol.mail;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GetAllMailAwardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2010;
	//邮件列表
	private List<com.dykj.rpg.protocol.mail.MailRs> mails;
	public GetAllMailAwardRs(){
		this.mails = new ArrayList<>();
	}
	public GetAllMailAwardRs(List<com.dykj.rpg.protocol.mail.MailRs> mails){
		this.mails = mails;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.mail.MailRs value : mails){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.mails.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.mail.MailRs> getMails(){
		return this.mails;
	}
	public void setMails(List<com.dykj.rpg.protocol.mail.MailRs> _mails){
		this.mails = _mails;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		mails = (List<com.dykj.rpg.protocol.mail.MailRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.mail.MailRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(mails)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "GetAllMailAwardRs [code=" + code +",mails=" + mails +"]";
	}
}
