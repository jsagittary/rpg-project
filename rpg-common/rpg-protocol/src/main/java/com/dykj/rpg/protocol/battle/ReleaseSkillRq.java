package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ReleaseSkillRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9012;
	//技能ID
	private int skillId;
	//技能释放时运行帧数
	private int frameNum;
	//技能释放的地块序号，地点，方向
	private com.dykj.rpg.protocol.battle.BattlePosition position;
	public ReleaseSkillRq(){
	}
	public ReleaseSkillRq(int skillId,int frameNum,com.dykj.rpg.protocol.battle.BattlePosition position){
		this.skillId = skillId;
		this.frameNum = frameNum;
		this.position = position;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.skillId = 0;
		this.frameNum = 0;
		ProtocolPool.getInstance().restoreProtocol(this.position);
		this.position = null;
	}
	public short getCode(){
		return this.code;
	}
	public int getSkillId(){
		return this.skillId;
	}
	public void setSkillId(int _skillId){
		this.skillId = _skillId;
	}
	public int getFrameNum(){
		return this.frameNum;
	}
	public void setFrameNum(int _frameNum){
		this.frameNum = _frameNum;
	}
	public com.dykj.rpg.protocol.battle.BattlePosition getPosition(){
		return this.position;
	}
	public void setPosition(com.dykj.rpg.protocol.battle.BattlePosition _position){
		this.position = _position;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		skillId = (int)dch.bytesToParam("Integer");
		frameNum = (int)dch.bytesToParam("Integer");
		position = (com.dykj.rpg.protocol.battle.BattlePosition)dch.bytesToParam("com.dykj.rpg.protocol.battle.BattlePosition");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(skillId)){
			return false;
		}
		if(!ech.paramToBytes(frameNum)){
			return false;
		}
		if(!ech.paramToBytes(position)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ReleaseSkillRq [code=" + code +",skillId=" + skillId +",frameNum=" + frameNum +",position=" + position +"]";
	}
}
