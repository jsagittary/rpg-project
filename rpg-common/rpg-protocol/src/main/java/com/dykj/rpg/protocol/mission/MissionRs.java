package com.dykj.rpg.protocol.mission;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class MissionRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1501;
	//关卡id
	private int missionId;
	//开始挂机的时间
	private long handUpTime;
	//挑战boss关卡cd结束时间
	private long battleCdEndTime;
	public MissionRs(){
	}
	public MissionRs(int missionId,long handUpTime,long battleCdEndTime){
		this.missionId = missionId;
		this.handUpTime = handUpTime;
		this.battleCdEndTime = battleCdEndTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.missionId = 0;
		this.handUpTime = 0;
		this.battleCdEndTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getMissionId(){
		return this.missionId;
	}
	public void setMissionId(int _missionId){
		this.missionId = _missionId;
	}
	public long getHandUpTime(){
		return this.handUpTime;
	}
	public void setHandUpTime(long _handUpTime){
		this.handUpTime = _handUpTime;
	}
	public long getBattleCdEndTime(){
		return this.battleCdEndTime;
	}
	public void setBattleCdEndTime(long _battleCdEndTime){
		this.battleCdEndTime = _battleCdEndTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		missionId = (int)dch.bytesToParam("Integer");
		handUpTime = (long)dch.bytesToParam("Long");
		battleCdEndTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(missionId)){
			return false;
		}
		if(!ech.paramToBytes(handUpTime)){
			return false;
		}
		if(!ech.paramToBytes(battleCdEndTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "MissionRs [code=" + code +",missionId=" + missionId +",handUpTime=" + handUpTime +",battleCdEndTime=" + battleCdEndTime +"]";
	}
}
