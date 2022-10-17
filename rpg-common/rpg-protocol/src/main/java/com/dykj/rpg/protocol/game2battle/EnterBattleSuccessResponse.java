package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EnterBattleSuccessResponse extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8908;
	//战斗服ip
	private String addr;
	//战斗服端口
	private int port;
	//战斗类型
	private byte battleType;
	//战场ID，客户端与战斗服使用
	private int battleId;
	//参与战斗该服的玩家
	private List<Integer> playerIds;
	//关卡id
	private int missionId;
	public EnterBattleSuccessResponse(){
		this.playerIds = new ArrayList<>();
	}
	public EnterBattleSuccessResponse(String addr,int port,byte battleType,int battleId,List<Integer> playerIds,int missionId){
		this.addr = addr;
		this.port = port;
		this.battleType = battleType;
		this.battleId = battleId;
		this.playerIds = playerIds;
		this.missionId = missionId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.addr = null;
		this.port = 0;
		this.battleType = 0;
		this.battleId = 0;
		this.playerIds.clear();
		this.missionId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public String getAddr(){
		return this.addr;
	}
	public void setAddr(String _addr){
		this.addr = _addr;
	}
	public int getPort(){
		return this.port;
	}
	public void setPort(int _port){
		this.port = _port;
	}
	public byte getBattleType(){
		return this.battleType;
	}
	public void setBattleType(byte _battleType){
		this.battleType = _battleType;
	}
	public int getBattleId(){
		return this.battleId;
	}
	public void setBattleId(int _battleId){
		this.battleId = _battleId;
	}
	public List<Integer> getPlayerIds(){
		return this.playerIds;
	}
	public void setPlayerIds(List<Integer> _playerIds){
		this.playerIds = _playerIds;
	}
	public int getMissionId(){
		return this.missionId;
	}
	public void setMissionId(int _missionId){
		this.missionId = _missionId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		addr = (String)dch.bytesToParam("String");
		port = (int)dch.bytesToParam("Integer");
		battleType = (byte)dch.bytesToParam("Byte");
		battleId = (int)dch.bytesToParam("Integer");
		playerIds = (List<Integer>)dch.bytesToParam("List<Integer>");
		missionId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(addr)){
			return false;
		}
		if(!ech.paramToBytes(port)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		if(!ech.paramToBytes(battleId)){
			return false;
		}
		if(!ech.paramToBytes(playerIds)){
			return false;
		}
		if(!ech.paramToBytes(missionId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EnterBattleSuccessResponse [code=" + code +",addr=" + addr +",port=" + port +",battleType=" + battleType +",battleId=" + battleId +",playerIds=" + playerIds +",missionId=" + missionId +"]";
	}
}
