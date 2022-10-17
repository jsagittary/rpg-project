//package com.dykj.rpg.net.netty.codex;
//
//import com.dykj.rpg.net.protocol.DecodeClassProHandler;
//import com.dykj.rpg.net.protocol.EncodeClassProHandler;
//import com.dykj.rpg.net.protocol.Protocol;
//
//public class LoginMsgRq implements Protocol{
//	private Short code = 1103;
//	//玩家id
//	private Integer playerId;
//	public LoginMsgRq(){
//	}
//	public LoginMsgRq(Integer playerId){
//		this.playerId = playerId;
//	}
//	public Short getCode(){
//		return this.code;
//	}
//	public Integer getPlayerId(){
//		return this.playerId;
//	}
//	public void setPlayerId(Integer _playerId){
//		this.playerId = _playerId;
//	}
//	public boolean decode(byte[] bytes){
//		DecodeClassProHandler dch = new DecodeClassProHandler(bytes);
//		code = (Short)dch.bytesToParam("Short");
//		playerId = (Integer)dch.bytesToParam("Integer");
//		return true;
//	}
//	public byte[] encode(){
//		EncodeClassProHandler ech = new EncodeClassProHandler();
//		if(!ech.paramToBytes(code)){
//			return null;
//		}
//		if(!ech.paramToBytes(playerId)){
//			return null;
//		}
//		return ech.complete();
//	}
//	public int getEncodeSize(){
//		return this.encode().length;
//	}
//	public String toString() {
//		return "LoginMsgRq [code=" + code +",playerId=" + playerId +"]";
//	}
//};
