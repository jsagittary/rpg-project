package com.dykj.rpg.net.kafka;

import com.dykj.rpg.net.thread.CmdThreadEnum;

public interface IListener {

	 void doListen(KafkaMsg msg);

	 short getKfkaCmd();

	 void setKfkaCmd(short kfkaCmd);


	 CmdThreadEnum getThreadEnum();


}
