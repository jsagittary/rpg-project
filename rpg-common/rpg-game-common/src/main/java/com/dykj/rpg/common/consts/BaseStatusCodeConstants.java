package com.dykj.rpg.common.consts;

/**
 * 基础状态码常量定义接口
 * 
 * @author wk.dai
 *
 */
public interface BaseStatusCodeConstants {

	/** 正确返回状态 */
	short OK = (short) 0;

	/** 参数错误，一般由前台错传参数或者玩家发包造成 */
	short ERROR_PARAMETER = 1;

	/**
	 * 异常错误
	 */
	short UNKNOW_EXCEPTION = -127;
	/**
	 * 游戏服登录关闭
	 */
	short SEVER_CLOSE = -1000;

	/**
	 * 客户端消息不合法
	 */
	short CLIENT_MSG_ERROR = -2000;
}
