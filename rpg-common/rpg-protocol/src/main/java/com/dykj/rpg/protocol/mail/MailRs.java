package com.dykj.rpg.protocol.mail;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class MailRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2001;
	//服务器唯一id
	private long instId;
	//道具id
	private int mailId;
	//道具类型
	private List<String> titleParam;
	//道具数量
	private List<String> contentParam;
	//附件奖励列表
	private List<com.dykj.rpg.protocol.item.ItemRs> items;
	//是否有奖励 0.没有奖励 1.有奖励
	private int isAward;
	//是否已读取 0.未读 1.已读取
	private int isRead;
	//是否领取奖励 0.没有领取 1.已领取
	private int isReceive;
	//创建日期
	private long createTime;
	public MailRs(){
		this.titleParam = new ArrayList<>();
		this.contentParam = new ArrayList<>();
		this.items = new ArrayList<>();
	}
	public MailRs(long instId,int mailId,List<String> titleParam,List<String> contentParam,List<com.dykj.rpg.protocol.item.ItemRs> items,int isAward,int isRead,int isReceive,long createTime){
		this.instId = instId;
		this.mailId = mailId;
		this.titleParam = titleParam;
		this.contentParam = contentParam;
		this.items = items;
		this.isAward = isAward;
		this.isRead = isRead;
		this.isReceive = isReceive;
		this.createTime = createTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
		this.mailId = 0;
		this.titleParam.clear();
		this.contentParam.clear();
		for(com.dykj.rpg.protocol.item.ItemRs value : items){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.items.clear();
		this.isAward = 0;
		this.isRead = 0;
		this.isReceive = 0;
		this.createTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getInstId(){
		return this.instId;
	}
	public void setInstId(long _instId){
		this.instId = _instId;
	}
	public int getMailId(){
		return this.mailId;
	}
	public void setMailId(int _mailId){
		this.mailId = _mailId;
	}
	public List<String> getTitleParam(){
		return this.titleParam;
	}
	public void setTitleParam(List<String> _titleParam){
		this.titleParam = _titleParam;
	}
	public List<String> getContentParam(){
		return this.contentParam;
	}
	public void setContentParam(List<String> _contentParam){
		this.contentParam = _contentParam;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getItems(){
		return this.items;
	}
	public void setItems(List<com.dykj.rpg.protocol.item.ItemRs> _items){
		this.items = _items;
	}
	public int getIsAward(){
		return this.isAward;
	}
	public void setIsAward(int _isAward){
		this.isAward = _isAward;
	}
	public int getIsRead(){
		return this.isRead;
	}
	public void setIsRead(int _isRead){
		this.isRead = _isRead;
	}
	public int getIsReceive(){
		return this.isReceive;
	}
	public void setIsReceive(int _isReceive){
		this.isReceive = _isReceive;
	}
	public long getCreateTime(){
		return this.createTime;
	}
	public void setCreateTime(long _createTime){
		this.createTime = _createTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
		mailId = (int)dch.bytesToParam("Integer");
		titleParam = (List<String>)dch.bytesToParam("List<String>");
		contentParam = (List<String>)dch.bytesToParam("List<String>");
		items = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		isAward = (int)dch.bytesToParam("Integer");
		isRead = (int)dch.bytesToParam("Integer");
		isReceive = (int)dch.bytesToParam("Integer");
		createTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(instId)){
			return false;
		}
		if(!ech.paramToBytes(mailId)){
			return false;
		}
		if(!ech.paramToBytes(titleParam)){
			return false;
		}
		if(!ech.paramToBytes(contentParam)){
			return false;
		}
		if(!ech.paramToBytes(items)){
			return false;
		}
		if(!ech.paramToBytes(isAward)){
			return false;
		}
		if(!ech.paramToBytes(isRead)){
			return false;
		}
		if(!ech.paramToBytes(isReceive)){
			return false;
		}
		if(!ech.paramToBytes(createTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "MailRs [code=" + code +",instId=" + instId +",mailId=" + mailId +",titleParam=" + titleParam +",contentParam=" + contentParam +",items=" + items +",isAward=" + isAward +",isRead=" + isRead +",isReceive=" + isReceive +",createTime=" + createTime +"]";
	}
}
