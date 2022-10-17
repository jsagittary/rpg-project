package com.dykj.rpg.protocol.mission;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class MissionLoginRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1502;
	private com.dykj.rpg.protocol.mission.MissionRs missionRs;
	public MissionLoginRs(){
	}
	public MissionLoginRs(com.dykj.rpg.protocol.mission.MissionRs missionRs){
		this.missionRs = missionRs;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.missionRs);
		this.missionRs = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.mission.MissionRs getMissionRs(){
		return this.missionRs;
	}
	public void setMissionRs(com.dykj.rpg.protocol.mission.MissionRs _missionRs){
		this.missionRs = _missionRs;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		missionRs = (com.dykj.rpg.protocol.mission.MissionRs)dch.bytesToParam("com.dykj.rpg.protocol.mission.MissionRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(missionRs)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "MissionLoginRs [code=" + code +",missionRs=" + missionRs +"]";
	}
}
