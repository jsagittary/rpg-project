package com.dykj.rpg.common.consts;

/**
 * 模块号定义接口,定义了所有应用层的模块号。公司引擎中心需要保留指令号3072(0x0C00)个.因此我们应用模块从0x0D开始定义
 * 
 * @author wk.dai
 */
public interface ModuleCodeConstants {
	/**
	 * 通用模块,一般指的是不属于任意模块的通用指令所属模块.
	 */
	byte GENERAL = (byte) 0x7F;
	/**
	 * 登录
	 */
	byte LOGON = (byte) 0x0D;
	/**
	 * 角色
	 */
	byte ROLE = (byte) 0x0E;
	/**
	 * 战斗
	 */
	byte BATTLE = (byte) 0x0F;
	/**
	 * 物品
	 */
	byte GOODS = (byte) 0x10;
	/**
	 * 关卡、章节
	 */
	byte CHAPTER_SECTION = (byte) 0x12;

	/**
	 * PVE榜等相关系统
	 */
	byte PVE = (byte) 0x13;

	/**
	 * PVP
	 */
	byte PVP = (byte) 0x14;

	/**
	 * 角色培养
	 */
	byte ROLE_TRAIN = (byte) 0x15;
	/**
	 * 白灵山(爬塔副本)
	 */
	byte CLIMB = (byte) 0x17;

	/**
	 * 商店
	 */
	byte SHOP = (byte) 0x19;

	/**
	 * 充值
	 */
	byte PAY = (byte) 0x1A;

	/**
	 * 招募
	 */
	byte RECRUIT = (byte) 0x1C;

	/**
	 * 聊天
	 */
	byte CHAT = (byte) 0x1B;

	/**
	 * 消息通知
	 */
	byte NOTIFY_MESSAGE = 0x1D;

	/**
	 * 系统设置
	 */
	byte SETTING = 0x1E;

	/**
	 * 任务(成就)
	 */
	byte MISSION = 0x20;

	/**
	 * 体力
	 */
	byte CHALLENGE_TIME = 0x21;
	/**
	 * 活动
	 */
	byte ACTIVITY = 0x23;
	/**
	 * 掉落
	 */
	byte DROP = 0x7E;

	/**
	 * 法术技能
	 */
	byte SPELL = 0x24;

	/**
	 * 运营奇遇
	 */
	byte QIYU = 0x25;

	/**
	 * 邀请码
	 */
	byte INVITE_CODE = 0x26;

	/**
	 * 签到
	 */
	byte SIGN = 0x27;

	/**
	 * 契约
	 */
	byte CONTRACT = 0x28;

	/**
	 * 图谱
	 */
	byte TUPU = 0x31;

	/**
	 * 血战
	 */
	byte BLOODY = 0x32;

	/**
	 * 开服活动
	 */
	byte ACTIVITY_STARTUP = 0x33;

	/**
	 * 武学系统
	 */
	byte MARTIAL = 0x34;

	/**
	 * 兑换系统
	 */
	byte EXCHANGE = 0x35;
	/**
	 * 服务器开关状态
	 */
	byte SERVER_SWITCH = 0x36;

	/**
	 * 排行榜
	 */
	byte RANKING_LIST = 0x38;

	/**
	 * （战斗）帧同步
	 */
	byte BATTLE_FRAME = 0x39;

	/**
	 * BOSS战
	 */
	byte WORLD_BOSS = 0x42;

	/**
	 * 好友
	 */
	byte FRIEND = 0x43;

	/**
	 * 公会
	 */
	byte GUILD = 0x44;

	/**
	 * 挑战赛
	 */
	byte ASSISTANT = 0x46;

	/**
	 * 战斗目标条件
	 */
	byte BATTLE_LIMITED = 0x48;

	/**
	 * 无量山-北窟
	 */
	byte NORTH_CAVE = 0x49;
	/**
	 * 奇门遁
	 */
	byte QIMEN = 0x52;

	/**
	 * 角色缘分道具
	 */
	byte ROLE_FATE = 0x56;
	/**
	 * 天命
	 */
	byte DESTINY = 0x58;

	/**
	 * 跨服
	 */
	byte CS_SERVER = 0x60;

	/**
	 * 玩家杂七杂八数据
	 */
	byte MIXED = (byte) 0x61;

	/**
	 * 通用配置
	 */
	byte COMMON_CONFIG = 0x62;

	/**
	 * 月卡
	 */
	byte MONTHLY_CARD = 0x63;

	/**
	 * 压力测试
	 */
	byte PROFILE = 0x70;

	/** 称号 */
	byte TITLE = 0x72;
	/**
	 * 头像
	 */
	byte PORTRAIT = 0x74;

	/**
	 * VIP标识
	 */
	byte VIP_LOGO = 0x75;
	/**
	 * 弹幕
	 */
	byte BARRAGE = 0x77;
	/**
	 * 剿匪
	 */
	byte DESTROY_BANDIT = 0x65;
	/**
	 * 首次战斗
	 */
	byte FIRST_FIGHT = 0x66;
	/**
	 * 问卷调查
	 */
	byte QUESTIONNAIRE = 0x67;
	/**
	 * 全民福利
	 */
	byte WELFARE = 0x7C;
	/**
	 * 红包雨
	 */
	byte RED_BAG_RAIN = 0x7a;
	/**
	 * 战斗力返还
	 */
	byte POWER_RETURN = 0x7b;
	/**
	 * 锦囊妙计
	 */
	byte PACKAGE_PLOT = 0x7d;
	/**
	 * 武将评价
	 */
	byte ROLE_APPRAISE = 0x6b;
	/**
	 * 寻宝
	 */
	byte SEEK_TREASURE = 0x6d;
	/**
	 * 机遇
	 */
	byte OPPORTUNITY = 0x5b;
	/**
	 * 组队
	 */
	byte TEAM = 0x5e;
	/**
	 * 限时商店
	 */
	byte LIMITED_TIME_SHOP = 0x40;

	byte TEST = 0x41;

	byte LIVE2D = 0x71;
	/**
	 * 四魂
	 */
	byte SOULJADE = 0x73;

	/**
	 * 抽卡
	 */
	byte DRAW = (byte) 0x76;

	/**
	 * 时间
	 */
	byte TIME = (byte) 0x77;

	/**
	 * 查询信息
	 */
	byte QUERY_INFO = (byte) 0x78;

	/**
	 * 温泉
	 */
	byte SPA = (byte) 0x79;

	/**
	 * 七宝修行
	 */
	byte SHIPPOU = (byte) 0x7a;

	/**
	 * 寻玉手札
	 */
	byte PLAYER_MANUAL = (byte) 0x7b;

	/**
	 * 资源回购
	 */
	byte RESOURCE_BACK = 0x64;

	/**
	 * 特权系统
	 */
	byte PRIVILEGE = 0x68;

	/**
	 * 觉醒
	 */
	byte AWAKEN = 0x69;

	/**
	 * 新月
	 */
	byte NEW_MOON = 0x6a;

	/**
	 * 图鉴
	 */
	byte HANDBOOK = 0x59;


	/**
	 * 成就
	 */
	byte ACHIEVE = 0x6c;

	/**
	 * 远征
	 */
	byte EXPEDITION = 0x6e;

	/**
	 * 拼图
	 */
	byte PUZZLE = 0x6f;
}
