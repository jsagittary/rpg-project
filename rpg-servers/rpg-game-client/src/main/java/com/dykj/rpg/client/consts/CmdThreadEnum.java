package com.dykj.rpg.client.consts;

public enum CmdThreadEnum {
/******************************客户端和服务器内部均可传送指令执行线程 开始**********************************/
	
	/** 战斗（处理战斗相关指令），多线程，根据场景划分，指令中不允许处理任何io操作 */
	BATTLE("BATTLE"),
	
	/** 登录线程（处理创建和登录的指令），多线程，玩家平均分配，这部分IO压力比较大单独提取出来  */
	LOGIN("LOGIN"),
	
	/** 主线程（处理功能模块的指令），多线程，玩家平均分配  */
	MAIN("MAIN"),

	/** 聊天（处理聊天指令），一个线程，指令中不允许处理任何io操作*/
	PUBLIC("public"),
	
	/** ai线程*/
	AI("AI"),
	
	/** 逻辑线程*/
	LOGIC("LOGIC"),
	
	/** 跨服线程*/
	CROSSSERVER("crossServer"),
	
	/** 数据更新（排行榜等耗时比较久的功能异步更新）*/
	DATAUPDATE("dataUpdate"),
	
	/** 数据更新（排行榜等耗时比较久的功能异步更新）*/
	HTTPTHREAD("httpthread"),
	
	/** 定时任务*/
	TIMER_TASK("timerTask"),
	
	/** 任务结算task*/
	ROOM_END_TASK_THREAD("timerTask"),

	/** 重连线程*/
	RECONNECT_THREAD("reconnectThread"),
	
	
/******************************客户端和服务器内部均可传送指令执行线程 结束**********************************/
	

	
/******************************服务器传送指令执行线程 开始**********************************/
	
	/** 任务和成就（处理任务成就指令，一个线程 ，指令中不允许处理任何io操作*/
	TASK_ACHIEVEMENT("TASK_ACHIEVEMENT"),
	
	/** 世界（处理世界的公共数据），一个线程 */
	WORD("WORD"),
	
	/** 其他功能处理线程，一个线程 */
	OTHER("OTHER");

/******************************服务器传送指令执行线程 结束**********************************/
	
	
	private String code;

	private CmdThreadEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
