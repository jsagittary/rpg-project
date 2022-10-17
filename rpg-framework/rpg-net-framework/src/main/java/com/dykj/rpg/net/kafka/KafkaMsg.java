package com.dykj.rpg.net.kafka;

import java.util.Arrays;
import java.util.List;

/**
 * @author: jianbo.gong
 * @version: 2018年6月28日 下午5:15:09
 * @describe:
 */
public class KafkaMsg {
	/**
	 * kafka的协议号
	 */
	private short kafkaCmd;
	/**
	 * 推送给玩家的指令号
	 */
	private short cmd;
	/**
	 * 玩家ids
	 */
	private List<Integer> playerIds;
	/**
	 * 消息内容
	 */
	private byte[] bytes;
	/**
	 * 消息内容(json字符串)
	 */
	private String parms;

	/**
	 *线程id ，如果不是玩家id 可以放到这里
	 */
	private int threadKey;

	/**
	 * 消息在发送者服务器的唯一key,做同步获取用
	 */
	private int signKey;

	/**
	 * 是否是回复消息
	 */
	private boolean back ;




	public KafkaMsg() {

	}

	public KafkaMsg(short cmd, List<Integer> playerIds, byte[] bytes) {
		super();
		this.cmd = cmd;
		this.playerIds = playerIds;
		this.bytes = bytes;
	}
	
	


	public KafkaMsg(short kafkaCmd, short cmd, List<Integer> playerIds, byte[] bytes) {
		super();
		this.kafkaCmd = kafkaCmd;
		this.cmd = cmd;
		this.playerIds = playerIds;
		this.bytes = bytes;
	}
	
	

	public KafkaMsg(short kafkaCmd, List<Integer> playerIds, String parms) {
		super();
		this.kafkaCmd = kafkaCmd;
		this.playerIds = playerIds;
		this.parms = parms;
	}

	public KafkaMsg(short kafkaCmd, String parms) {
		super();
		this.kafkaCmd = kafkaCmd;
		this.parms = parms;
	}
	
	public KafkaMsg(short kafkaCmd, byte[] bytes) {
		super();
		this.kafkaCmd = kafkaCmd;
		this.bytes = bytes;
	}
	public KafkaMsg(short kafkaCmd, short cmd, byte[] bytes) {
		super();
		this.kafkaCmd = kafkaCmd;
		this.cmd = cmd;
		this.bytes = bytes;
	}


	public KafkaMsg(short kafkaCmd, short cmd, byte[] bytes, int threadKey) {
		this.kafkaCmd = kafkaCmd;
		this.cmd = cmd;
		this.bytes = bytes;
		this.threadKey = threadKey;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

	public List<Integer> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(List<Integer> playerIds) {
		this.playerIds = playerIds;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	
	
	public short getKafkaCmd() {
		return kafkaCmd;
	}

	public void setKafkaCmd(short kafkaCmd) {
		this.kafkaCmd = kafkaCmd;
	}

	@Override
	public String toString() {
		return "KafkaMsg{" +
				"kafkaCmd=" + kafkaCmd +
				", cmd=" + cmd +
				", playerIds=" + playerIds +
				", bytes=" + Arrays.toString(bytes) +
				", parms='" + parms + '\'' +
				", threadKey=" + threadKey +
				", signKey=" + signKey +
				", back=" + back +
				'}';
	}

	public String getParms() {
		return parms;
	}

	public void setParms(String parms) {
		this.parms = parms;
	}

	public int getThreadKey() {
		return threadKey;
	}

	public void setThreadKey(int threadKey) {
		this.threadKey = threadKey;
	}

	public int getSignKey() {
		return signKey;
	}

	public void setSignKey(int signKey) {
		this.signKey = signKey;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}
}
