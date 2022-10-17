package com.dykj.rpg.net.kafka;


import com.dykj.rpg.net.thread.CmdThreadEnum;

public abstract class AbstractListener implements IListener {
	

	private short kfkaCmd;


	public short getKfkaCmd() {
		return kfkaCmd;
	}

	public void setKfkaCmd(short kfkaCmd) {
		this.kfkaCmd = kfkaCmd;
	}


	@Override
	public CmdThreadEnum getThreadEnum() {
		return CmdThreadEnum.MAIN;
	}
}
