/**
 * 
 */
package com.dykj.rpg.common.consts;

/**
 * 指令号定义接口,所有应用指令都应该在此接口定义。 指令号定义必须按照:模块id|序列号，格式定义。每个字段占8bit
 * 
 * @author david.dai 2013-12-4
 */
/**
 * @author admin
 *
 */

/**
 * @author admin
 *
 */

/**
 * @author admin
 *
 */
public interface CmdCodeConstants extends BaseStatusCodeConstants {
	// ###################通用模块##############
	/**
	 * 通用错误消息
	 */
	short ERROR_MSG = ModuleCodeConstants.GENERAL << 8 | 0xFF;

	/**
	 * 错误信息上报
	 */
	short ERROR_REPORT = ModuleCodeConstants.GENERAL << 8 | 0xFE;

	/**
	 * 获取通过奖励
	 */
	short GET_MY_REWARD_BY_TYPE = ModuleCodeConstants.GENERAL << 8 | 0x00;

	/**
	 * 通过类型查询个人奖励预览
	 */
	short QUERY_MY_REWARD_BY_TYPE = ModuleCodeConstants.GENERAL << 8 | 0x01;

	/**
	 * 奖励通知
	 */
	short NOTIFY_REWARD_BY_TYPE = ModuleCodeConstants.GENERAL << 8 | 0x02;

	/**
	 * 通知玩家资源更新
	 */
	short NOTIFY_PLAYER_RES_UPDATE = ModuleCodeConstants.GENERAL << 8 | 0x10;

	/**
	 * 服务器信息下发
	 */
	short SERVER_INFO = ModuleCodeConstants.GENERAL << 8 | 0x12;

	/**
	 * 下发所有功能状态
	 */
	short ALL_FUNCTIONS_STATE = ModuleCodeConstants.GENERAL << 8 | 0x20;
	/**
	 * 更新功能状态
	 */
	short UPDATE_FUNCTIONS_STATE = ModuleCodeConstants.GENERAL << 8 | 0x21;

	/**
	 * 禁言玩家
	 */
	short GAG_PLAYER = ModuleCodeConstants.GENERAL << 8 | 0x30;
	/**
	 * 举报玩家
	 */
	short REPORT_PLAYER = ModuleCodeConstants.GENERAL << 8 | 0x31;
	/**
	 * GM协议
	 */
	short GM = ModuleCodeConstants.GENERAL << 8 | 0x32;

	// ###############################登录、帐号#######################################
	/**
	 * 登录游戏服务器，在多角色游戏中为选中角色后登录游戏服务器
	 */
	short LOGIN_GAME_SERVER = ModuleCodeConstants.LOGON << 8 | 0x00;

	/**
	 * 注册玩家角色
	 */
	short REGIST_PLAYER = ModuleCodeConstants.LOGON << 8 | 0x01;

	/**
	 * 重新登录通知
	 */
	short S2C_RELOGIN_NOTIFY = ModuleCodeConstants.LOGON << 8 | 0xff;

	/**
	 * 重新连接
	 */
	short RE_CONNECT = ModuleCodeConstants.LOGON << 8 | 0x10;

	/**
	 * 重新连接，数据推送完成
	 */
	short RE_CONNECT_FINISH = ModuleCodeConstants.LOGON << 8 | 0x11;

	/**
	 * 开始下发玩家登录数据前的通知指令
	 */
	short S2C_BEFORE_ENTER_GAME = ModuleCodeConstants.LOGON << 8 | 0x20;

	/**
	 * 开始下发玩家重连接数据前的通知指令
	 */
	short S2C_BEFORE_RECONNECT = ModuleCodeConstants.LOGON << 8 | 0x21;

	// ###############################角色#######################################
	/**
	 * 玩家以及队友信息
	 */
	short ME_AND_PARTNER_INFO = ModuleCodeConstants.ROLE << 8 | 0x01;

	/**
	 * 玩家角色选择技能
	 */
	short PLAYER_ROLE_SELECT_SPELL = ModuleCodeConstants.ROLE << 8 | 0x0c;

	/**
	 * 主角角色技能
	 */
	short PLAYER_ROLE_SPELL_LIST = ModuleCodeConstants.ROLE << 8 | 0x0d;

	/**
	 * 角色技能开放
	 */
	short ROLE_SPELL_ENABLED = ModuleCodeConstants.ROLE << 8 | 0x0e;

	/**
	 * 角色技能屏蔽，取消开放
	 */
	short ROLE_SPELL_DISABLED = ModuleCodeConstants.ROLE << 8 | 0x0f;

	/**
	 * 更新玩家名称
	 */
	short PLAYER_UPDATE_NAME = ModuleCodeConstants.ROLE << 8 | 0x11;

	/**
	 * 玩家等级变更（团队等级）
	 */
	short PLAYER_LEVEL_CHANGE = ModuleCodeConstants.ROLE << 8 | 0x21;

	/**
	 * 一次性更新方阵配置
	 */
	short C2S_BATCH_WAR_MATIX_CONFIGURE = ModuleCodeConstants.ROLE << 8 | 0x20;

	/**
	 * 方阵配置请求结果返回
	 */
	short S2C_WAR_MATIX_CONFIGURE_RESPONSE = ModuleCodeConstants.ROLE << 8 | 0x20;

	/**
	 * 方阵配置-上阵
	 */
	short C2S_WAR_MATIX_TO_BATTLE = ModuleCodeConstants.ROLE << 8 | 0x21;

	/**
	 * 方阵配置-更换位置
	 */
	short C2S_WAR_MATIX_CHANGE_INDEX = ModuleCodeConstants.ROLE << 8 | 0x22;

	/**
	 * 方阵配置-角色下阵
	 */
	short C2S_ROLE_OFF_WAR_MATIX = ModuleCodeConstants.ROLE << 8 | 0x23;

	/**
	 * 方阵配置-一键布阵
	 */
	short C2S_WAR_MATIX_CONF = ModuleCodeConstants.ROLE << 8 | 0x24;

	/**
	 * 战阵开放位置上限更新
	 */
	short WAR_MATIX_OPEND_SIZE_UPDATE = ModuleCodeConstants.ROLE << 8 | 0x27;

	/**
	 * 方阵配置更新(服务器主动推送)
	 */
	short S2C_WAR_MATIX_CONFIGURE = ModuleCodeConstants.ROLE << 8 | 0x28;

	/**
	 * 方阵更新(客户端发起 new)
	 */
	short S2C_WAR_MATIX_NEW_CONFIGURE = ModuleCodeConstants.ROLE << 8 | 0x29;

	/**
	 * 每日属性更新
	 */
	short S2C_PERDAY_PROPERTIES_UPDATE = ModuleCodeConstants.ROLE << 8 | 0x30;

	/**
	 * 每日重置通知指令
	 */
	short S2C_RESET_DAILY_NOTIFY = ModuleCodeConstants.ROLE << 8 | 0x31;
	/**
	 * 多倍产出通知列表
	 */
	short MULTIPLE_OUT_PUT_LIST = ModuleCodeConstants.ROLE << 8 | 0x32;
	/**
	 * 多倍产出通知
	 */
	short MULTIPLE_OUT_PUT = ModuleCodeConstants.ROLE << 8 | 0x33;

	/**
	 * 发送角色信息到客户端
	 */
	short S2C_PLAYER_BASE_INFO = ModuleCodeConstants.ROLE << 8 | 0x41;

	/**
	 * 玩家上线信息通知，发送给登录的玩家
	 */
	short S2C_PLAYER_ONLINE_FINISH = ModuleCodeConstants.ROLE << 8 | 0x42;

	/**
	 * 角色升级
	 */
	short S2C_UNIT_LEVEL_UP = ModuleCodeConstants.ROLE << 8 | 0x50;

	/**
	 * 角色经验变更
	 */
	short S2C_UNIT_EXP_CHANGED = ModuleCodeConstants.ROLE << 8 | 0x51;

	/**
	 * 获得新队友
	 */
	short S2C_OBTAIN_NEW_PARTNER = ModuleCodeConstants.ROLE << 8 | 0x60;

	/**
	 * 删除队友
	 */
	short S2C_REMOVE_PARTNER = ModuleCodeConstants.ROLE << 8 | 0x61;

	/**
	 * 获取其他玩家基础信息
	 */
	short GET_OTHER_PLAYER_BASE_INFO = ModuleCodeConstants.ROLE << 8 | 0x70;

	/**
	 * 获取其他玩家详细信息
	 */
	short GET_OTHER_PLAYER_DETAILS = ModuleCodeConstants.ROLE << 8 | 0x71;

	/**
	 * 获取其他角色明细
	 */
	short GET_OTHER_ROLE_DETAILS = ModuleCodeConstants.ROLE << 8 | 0x73;

	/**
	 * 新手引导进度
	 */
	short UPDATE_BEGINNERS_GUIDE = ModuleCodeConstants.ROLE << 8 | 0x80;

	/**
	 * 新手引导详情
	 */
	short UPDATE_GUIDE_DETAILS = ModuleCodeConstants.ROLE << 8 | 0x88;

	/**
	 * 功能点击信息
	 */
	short UPDATE_FUNCTION_CLICK_INFO = ModuleCodeConstants.ROLE << 8 | 0x89;

	/**
	 * 召唤侠士
	 */
	short SUMMON_PALADIN = ModuleCodeConstants.ROLE << 8 | 0x90;

	/**
	 * 更新VIP信息
	 */
	short CHANGE_VIP_INFO = ModuleCodeConstants.ROLE << 8 | 0x9c;
	/**
	 * 发送玩家记录(新手引导,剧情记录)
	 */
	short SEND_PLAYER_GUIDE_INFO = ModuleCodeConstants.ROLE << 8 | 0x92;
	/**
	 * 查询玩家记录(新手引导,剧情记录)
	 */
	short QUERY_PLAYER_GUIDE_INFO = ModuleCodeConstants.ROLE << 8 | 0x93;
	/**
	 * 发送玩家所有记录
	 */
	short SEND_PLAYER_ALL_GUIDE_INFO = ModuleCodeConstants.ROLE << 8 | 0x94;

	/**
	 * 请求随机名称
	 */
	short REQUEST_RANDOM_NAME = ModuleCodeConstants.ROLE << 8 | 0x95;

	/**
	 * 发送引导记录
	 */
	short SEND_GUIDE_INFO = ModuleCodeConstants.ROLE << 8 | 0x96;

	/**
	 * 更新玩家引导记录
	 */
	short UPDATE_GUIDE_INFO = ModuleCodeConstants.ROLE << 8 | 0x97;
	// ###############################关卡#########################################

	/**
	 * 玩家所有角色经脉信息
	 */
	short PLAYER_ALL_PULSE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x0;

	/**
	 * 刷新单个角色
	 */
	short C2S_ROLE_PULSE_REFRESH = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x1;
	/**
	 * 玩家单个角色的经脉
	 */
	short S2C_ROLE_PULSE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x1;

	/**
	 * 升级角色培养
	 */
	short C2S_ROLE_UPLEVEL_PULSE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x2;

	/**
	 * 角色升星节点
	 */
	short ROLE_STAR_NODE_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x3;

	/**
	 * 角色升星
	 */
	short ROLE_STAR_LEVEL_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x04;

	/**
	 * 角色突破（品质提升）
	 */
	short ROLE_QUALITY_SURMOUNT = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x05;

	/**
	 * 刷新单个角色经脉
	 */
	short ROLE_REFRESH_PULSE_RESULT = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x6;

	/**
	 * 升级单个角色经脉
	 */
	short S2C_ROLE_UPLEVEL_PULSE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x7;

	/**
	 * 角色传功
	 */
	short ROLE_TRANSFER = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x08;

	/**
	 * 角色修炼
	 */
	short ROLE_PRACTICE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x09;

	/**
	 * 穴位突破
	 */
	short ACUPOINT_BREACH = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x0A;

	/**
	 * 穴位突破概率购买
	 */
	short ACUPOINT_BREACH_EXTRA_RATE_BUY = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x0B;

	/**
	 * 角色觉醒
	 */
	short ROLE_AWAKEN = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x0C;

	/**
	 * 角色归隐
	 */
	short ROLE_HERMIT = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x10;
	/**
	 * 角色重生
	 */
	short ROLE_REBIRTH = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x12;
	/**
	 * 角色一键强化
	 */
	short ONE_KEY_INTENSIFY_ROLE = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x13;
	/**
	 * 角色技能升级
	 */
	short ROLE_SPELL_LEVEL_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x20;
	/**
	 * 角色部件强化
	 */
	short ROLE_PARTS_INTENSIFY = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x21;
	/**
	 * 角色部件一键强化
	 */
	short ROLE_PARTS_ONE_KEY_INTENSIFY = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x22;
	/**
	 * 角色部件升阶
	 */
	short ROLE_PARTS_QUALITY_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x23;
	/**
	 * 角色升阶
	 */
	short ROLE_QUALITY_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x24;
	/**
	 * 玩家所有角色部件信息
	 */
	short ROLE_PARTS_ALL_INFO = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x25;
	/**
	 * 使用角色经验池升级角色
	 */
	short ROLE_LEVEL_UP = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x26;
	/**
	 * 解锁技能
	 */
	short DEBLOCKING_SKILL = ModuleCodeConstants.ROLE_TRAIN << 8 | 0x27;
	// ###############################关卡#########################################
	/**
	 * 所有关卡记录
	 */
	short SECTION_RECORDS_ALL = ModuleCodeConstants.CHAPTER_SECTION << 8;

	/**
	 * 更新关卡记录信息（单个）
	 */
	short SECTION_UPDATE_SINGLE = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x01;

	/**
	 * 开启章节宝箱
	 */
	short OPEN_CHAPTER_BOX = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x02;
	/**
	 * 开启章节宝箱
	 */
	short ONE_KEY_OPEN_CHAPTER_BOX = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x07;
	/**
	 * 通知各难度宝箱领取状态
	 */
	short NOTIFY_BOX_REWARD = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x08;

	/**
	 * 重置挑战次数
	 */
	short SECTION_RESET_CHALLENGE = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x03;

	/**
	 * 扫荡
	 */
	short SECTION_FREE_QUICK_PASS = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x04;

	/**
	 * 扫荡N次
	 */
	short SECTION_MANY_QUICK_PASS = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x05;

	/**
	 * 领取宝箱副本奖励
	 */
	short SECTION_REWARD_BOX = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x06;
	/**
	 * 更新领取宝箱状态
	 */
	short SEND_UPDATE_BOX_STATUS = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x8;
	/**
	 * 更新剧情信息
	 */
	short UPDATE_STORY = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x09;
	/**
	 * 下发玩家所有剧情记录
	 */
	short SEND_ALL_STORY_PROGRESS = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x10;
	/**
	 * 领取剧情全通关奖励
	 */
	short DRAW_STORY_FULL_CLEARANCE = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x11;
	/**
	 * 剧情结束
	 */
	short END_OF_STORY = ModuleCodeConstants.CHAPTER_SECTION << 8 | 0x12;

	// ###############################背包、装备栏、物品#############################
	/**
	 * 所有背包物品信息
	 */
	short GOODS_PACKAGE_ALL = ModuleCodeConstants.GOODS << 8;

	/**
	 * 请求装备物品
	 */
	short C2S_REQUEST_EQUIP = ModuleCodeConstants.GOODS << 8 | 0x11;

	/**
	 * 请求卸除装备
	 */
	short C2S_REQUEST_UNEQUIP = ModuleCodeConstants.GOODS << 8 | 0x12;

	/**
	 * 创建四魂方案
	 */
	short CREATE_SOULJADE_PROJECT = ModuleCodeConstants.GOODS << 8 | 0x13;

	/**
	 * 更新四魂方案
	 */
	short UPDATE_SOULJADE_PROJECT = ModuleCodeConstants.GOODS << 8 | 0x14;

	/**
	 * 通过方案,一键换装
	 */
	short ONE_KEY_EQUIP_REQUEST = ModuleCodeConstants.GOODS << 8 | 0x15;

	/**
	 * 推送玩家预设四魂方案
	 */
	short SEND_PLAYER_SOULJADE_PROJECT = ModuleCodeConstants.GOODS << 8 | 0x16;

	/**
	 * 删除预设四魂方案
	 */
	short DELETE_SOULJADE_PROJECT = ModuleCodeConstants.GOODS << 8 | 0x17;

	/**
	 * 使用道具
	 */
	short GOODS_USE_PROP = ModuleCodeConstants.GOODS << 8 | 0x18;

	/**
	 * 四魂强化
	 */
	short INTENSIFY_SOULJADE = ModuleCodeConstants.GOODS << 8 | 0x19;

	/**
	 * 锁定/解锁四魂
	 */
	short CHANGE_SOULJADE_LOCK_STATU = ModuleCodeConstants.GOODS << 8 | 0x20;

	/**
	 * 分解四魂
	 */
	short DECOMPOSE_SOULJADE = ModuleCodeConstants.GOODS << 8 | 0x21;

	/**
	 * 注入四魂魂值
	 */
	short INJECT_SOUL_VALUE = ModuleCodeConstants.GOODS << 8 | 0x22;

	/**
	 * 使用多选宝箱
	 */
	short CHECK_BOX_USED = ModuleCodeConstants.GOODS << 8 | 0x30;

	/**
	 * 通知物品更新，多个物品
	 */
	short NOTIFY_HOLD_GOODS_UPDATE_MULTIPLE = ModuleCodeConstants.GOODS << 8 | 0x40;

	/**
	 * 通知物品更新，单个物品
	 */
	short NOTIFY_HOLD_GOODS_UPDATE_SINGLE = ModuleCodeConstants.GOODS << 8 | 0x41;

	/**
	 * 通知装备物品，在装备物品成功时服务器下发到客户端
	 */
	short NOTIFY_EQUIP = ModuleCodeConstants.GOODS << 8 | 0x42;

	/**
	 * 通知卸除装备，在卸除装备成功时服务器下发到客户端
	 */
	short NOTIFY_UNEQUIP = ModuleCodeConstants.GOODS << 8 | 0x43;

	/**
	 * 批量使用道具
	 */
	short GOODS_BATCH_USE_PROP = ModuleCodeConstants.GOODS << 8 | 0x61;

	/**
	 * 道具使用结果（是否成功）
	 */
	short USED_ITEM_RESULT = ModuleCodeConstants.GOODS << 8 | 0x85;

	/**
	 * 出售非装备物品
	 */
	short GOODS_SELL = ModuleCodeConstants.GOODS << 8 | 0x95;

	/**
	 * 使用概率礼包
	 */
	short USE_PROBABILITYGIFT = ModuleCodeConstants.GOODS << 8 | 0x96;

	// ###############################战斗#############################
	/**
	 * 开始战斗
	 */
	short BATTLE_START = ModuleCodeConstants.BATTLE << 8 | 0x00;

	/**
	 * 战斗回合
	 */
	short BATTLE_ROUND = ModuleCodeConstants.BATTLE << 8 | 0x01;

	/**
	 * 战斗结束
	 */
	short BATTLE_END = ModuleCodeConstants.BATTLE << 8 | 0x02;

	// ###############################PVP#############################
	/**
	 * 获取可挑战的群豪谱信息
	 */
	short GET_CHALLENGE_HERO_LIST = ModuleCodeConstants.PVP << 8 | 0x00;

	/**
	 * 获取群豪谱信息
	 */
	short GET_ALL_HERO_LIST = ModuleCodeConstants.PVP << 8 | 0x01;

	/**
	 * 获取群豪谱奖励信息
	 */
	short GET_HERO_LUCKY_NUMBERS = ModuleCodeConstants.PVP << 8 | 0x02;

	/**
	 * 获取群豪谱奖励
	 */
	short RECEIVE_HERO_LIST_REWARD = ModuleCodeConstants.PVP << 8 | 0x03;

	/**
	 * 挑战群豪谱
	 */
	short CHALLENGE_HERO_LIST = ModuleCodeConstants.PVP << 8 | 0x04;

	/**
	 * 获取个人群豪谱信息
	 */
	short GET_MY_HERO_LIST_INFORMATION = ModuleCodeConstants.PVP << 8 | 0x05;

	/**
	 * 快速挑战
	 */
	short CHALLENGE_QUICK = ModuleCodeConstants.PVP << 8 | 0x06;

	/**
	 * 群豪谱最佳成绩刷新
	 */
	short HERO_LIST_BEST_UPDATE = ModuleCodeConstants.PVP << 8 | 0x07;

	/**
	 * 重置群豪谱挑战冷却时间
	 */
	short HERO_LIST_RESET_CD = ModuleCodeConstants.PVP << 8 | 0x08;

	/**
	 * 挑战天罡星
	 */
	short CHALLENGE_TIANGANG_36 = ModuleCodeConstants.PVP << 8 | 0x10;

	/**
	 * 获取天罡星列表信息
	 */
	short GET_CHALLENGE_TIANGANG_36_INFO = ModuleCodeConstants.PVP << 8 | 0x11;

	/**
	 * 获取个人天罡排名
	 */
	short GET_MY_TG_RANK = ModuleCodeConstants.PVP << 8 | 0x12;

	/**
	 * 获取个人天罡奖励
	 */
	short GET_MY_TG_REWARD = ModuleCodeConstants.PVP << 8 | 0x13;

	/**
	 * 获取个人天罡星剩余挑战次数
	 */
	short GET_MY_TG_TIME_REMAINING = ModuleCodeConstants.PVP << 8 | 0x14;

	/**
	 * 获取天罡星其他玩家详细信息
	 */
	short GET_TG_OTHER_PLAYER_DETAILS = ModuleCodeConstants.PVP << 8 | 0x15;

	// ######################无量山####################
	/**
	 * 重置无量山挑战层数
	 */
	short RESET_CLIMB_STATE = ModuleCodeConstants.CLIMB << 8 | 0x00;

	/**
	 * 挑战结果
	 */
	short CLIMB_CHALLENGE_RESULT = ModuleCodeConstants.CLIMB << 8 | 0x01;

	/**
	 * 主页信息
	 */
	short CLIMB_HOME_INFO = ModuleCodeConstants.CLIMB << 8 | 0x02;

	/**
	 * 奖励
	 */
	short CLIMB_REWARD = ModuleCodeConstants.CLIMB << 8 | 0x03;

	/**
	 * 获取万能副本信息
	 */
	short CLIMB_WANNNEG_GET_INFO = ModuleCodeConstants.CLIMB << 8 | 0x04;

	/**
	 * 挑战万能副本
	 */
	short CLIMB_WANNNEG_CHALLENGE = ModuleCodeConstants.CLIMB << 8 | 0x05;
	/**
	 * 摩诃崖信息
	 */
	short MHY_INFO = ModuleCodeConstants.CLIMB << 8 | 0x07;

	/**
	 * 领取过关斩将星级奖励
	 */
	short CLIMB_STAR_REWARD = ModuleCodeConstants.CLIMB << 8 | 0x06;
	/**
	 * 无量山扫荡
	 */
	short CLIMB_SWEEP = ModuleCodeConstants.CLIMB << 8 | 0x10;

	/**
	 * 摩诃崖扫荡
	 */
	short MHY_SWEEP = ModuleCodeConstants.CLIMB << 8 | 0x20;

	/**
	 * 摩诃崖通关信息列表
	 */
	short MHY_INFO_LIST = ModuleCodeConstants.CLIMB << 8 | 0x21;

	/**
	 * 获得无量山星数
	 */
	short GAIN_CLIMB_STAR = ModuleCodeConstants.CLIMB << 8 | 0x22;

	// ################商店###########

	/**
	 * 商店中购买物品结果
	 */
	short SHOP_BUY_RESULT = ModuleCodeConstants.SHOP << 8 | 0x00;

	/**
	 * 礼包商店中购买物品
	 */
	short SHOP_BUY_GIFT = ModuleCodeConstants.SHOP << 8 | 0x00;

	/**
	 * 查询商店信息
	 */
	short SHOP_GET_GIFT_INFO = ModuleCodeConstants.SHOP << 8 | 0x01;

	/**
	 * 获取随机商店
	 */
	short SHOP_GET_RANDOM_SHOP = ModuleCodeConstants.SHOP << 8 | 0x02;

	/**
	 * 刷新随机商店商店
	 */
	short SHOP_REFRESH_RANDOM = ModuleCodeConstants.SHOP << 8 | 0x03;

	/**
	 * 获取所有随机商店
	 */
	short SHOP_GET_ALL_RANDOM_SHOP = ModuleCodeConstants.SHOP << 8 | 0x04;

	/**
	 * 随机商店中购买普通物品
	 */
	short SHOP_BUY_IN_GOODS_SHOP = ModuleCodeConstants.SHOP << 8 | 0x05;

	/**
	 * 群豪谱积分商店中购买(兑换)物品
	 */
	short SHOP_BUY_IN_HERO_SHOP = ModuleCodeConstants.SHOP << 8 | 0x06;

	/**
	 * 群豪谱积分商店中可购买(兑换)物品ID列表
	 */
	short SHOP_HERO_CAN_BUY_ID_LIST = ModuleCodeConstants.SHOP << 8 | 0x07;

	/**
	 * 随机商城开放
	 */
	short RANDOM_SHOP_OPEN = ModuleCodeConstants.SHOP << 8 | 0x08;

	/**
	 * 随机商城许愿
	 */
	short RANDOM_SHOP_WISH = ModuleCodeConstants.SHOP << 8 | 0x10;

	/**
	 * 购买铜币
	 */
	short BUY_COIN = ModuleCodeConstants.SHOP << 8 | 0x20;

	/**
	 * 已经购买铜币的次数信息
	 */
	short ALREADY_USED_BUY_COIN_INFO = ModuleCodeConstants.SHOP << 8 | 0x21;

	/**
	 * 多次购买
	 */
	short BUY_COIN_BATCH = ModuleCodeConstants.SHOP << 8 | 0x30;

	/**
	 * 商城单个礼包信息
	 */
	short SHOP_GIFT_INFO = ModuleCodeConstants.SHOP << 8 | 0x23;

	/**
	 * 礼包信息列表
	 */
	short SHOP_GIFT_LIST = ModuleCodeConstants.SHOP << 8 | 0x24;

	/**
	 * 极品商店购买
	 */
	short BUY_NEED_PROPS = ModuleCodeConstants.SHOP << 8 | 0x25;
	/**
	 * 资源商城信息
	 */
	short SEND_RESOURCE_MALL_INFO = ModuleCodeConstants.SHOP << 8 | 0x26;
	/**
	 * 资源商城购买
	 */
	short BUY_RESOURCE_MALL = ModuleCodeConstants.SHOP << 8 | 0x27;
	/**
	 * 请求购买资源中心内的资源
	 */
	short REQUEST_RESOURCE_CENTER = ModuleCodeConstants.SHOP << 8 | 0x28;
	/**
	 * 发送玩家资源中心购买记录
	 */
	short SEND_RESOURCE_CENTER_RECORD = ModuleCodeConstants.SHOP << 8 | 0x29;
	// ###############聊天####
	/**
	 * 聊天请求
	 */
	short CHAT_REQUEST = ModuleCodeConstants.CHAT << 8 | 0x01;

	/**
	 * 发送聊天信息
	 */
	short CHAT_SEND_MESSAGE = ModuleCodeConstants.CHAT << 8 | 0x02;

	/**
	 * 敏感词汇
	 */
	short CHAT_SENSITIVE_WORDS = ModuleCodeConstants.CHAT << 8 | 0x03;

	/**
	 * 切换当前聊天玩家
	 */
	short SWAP_CHAT_PLAYER = ModuleCodeConstants.CHAT << 8 | 0x04;

	/**
	 * 获取有私聊人的列表
	 */
	short GAIN_PRIVATE_CHAT_LIST = ModuleCodeConstants.CHAT << 8 | 0x05;

	/**
	 * 有新的私聊
	 */
	short NEW_PRIVATE_CHAT = ModuleCodeConstants.CHAT << 8 | 0x06;
	/**
	 * 获得历史聊天记录
	 */
	short GAIN_CHAT_INFOS = ModuleCodeConstants.CHAT << 8 | 0x07;
	/**
	 * 系统频道消息
	 */
	short SYSTEM_CHANNEL_MESSAGE = ModuleCodeConstants.CHAT << 8 | 0x08;
	/**
	 * 世界频道消息
	 */
	short WORLD_CHANNEL_MESSAGE = ModuleCodeConstants.CHAT << 8 | 0x09;
	/**
	 * 公会频道消息
	 */
	short CONSORTIA_CHANNEL_MESSAGE = ModuleCodeConstants.CHAT << 8 | 0x10;
	/**
	 * 私聊信息
	 */
	short PRIVATE_CHAT_MESSAGE = ModuleCodeConstants.CHAT << 8 | 0x11;
	/**
	 * 查询聊天记录
	 */
	short QUERY_PRIVATE_CHAT_RECORD = ModuleCodeConstants.CHAT << 8 | 0x12;
	/**
	 * 清空聊天记录
	 */
	short CLEAR_PRIVATE_CHAT_RECORD = ModuleCodeConstants.CHAT << 8 | 0x13;
	/**
	 * 请求发送聊天信息
	 */
	short REQUEST_SEND_CHAT_MSG = ModuleCodeConstants.CHAT << 8 | 0x14;
	/**
	 * 请求发送超链接信息
	 */
	short REQUEST_SEND_HYPERLINK_MSG = ModuleCodeConstants.CHAT << 8 | 0x15;

	/**
	 * 世界答题开始
	 */
	short WORLD_QUESTION_START = ModuleCodeConstants.CHAT << 8 | 0x16;
	/**
	 * 世界答题结束
	 */
	short WORLD_QUESTION_END = ModuleCodeConstants.CHAT << 8 | 0x17;
	/**
	 * 聊天信息
	 */
	short CHAT_MSG = ModuleCodeConstants.CHAT << 8 | 0x18;
	// ###############充值####
	/**
	 * 请求充值
	 */
	short REQUEST_PAY = ModuleCodeConstants.PAY << 8 | 0x00;

	/**
	 * 充值结果通知
	 */
	short PAY_RESULT_NOTIFY = ModuleCodeConstants.PAY << 8 | 0x01;

	/**
	 * 充值信息变更通知
	 */
	short RECHARGE_INFO_CHANGED_NOTIFY = ModuleCodeConstants.PAY << 8 | 0x02;

	/**
	 * 充值信息通知
	 */
	short RECHARGE_INFO_NOTIFY = ModuleCodeConstants.PAY << 8 | 0x03;

	/**
	 * 获取是否首冲记录
	 */
	short RECHARGE_RECORDLIST_INFO = ModuleCodeConstants.PAY << 8 | 0x04;

	/**
	 * 获取vip奖励领取记录
	 */
	short VIP_REC_INFO_NOTIFY = ModuleCodeConstants.PAY << 8 | 0x05;

	/**
	 * 领取vip奖励
	 */
	short VIP_REC_NOTIFY = ModuleCodeConstants.PAY << 8 | 0x06;

	/**
	 * 领取首充奖励
	 */
	short FIRST_RECHARGE_REWARD = ModuleCodeConstants.PAY << 8 | 0x10;

	/**
	 * 首充奖励状态
	 */
	short FIRST_RECHARGE_STATE = ModuleCodeConstants.PAY << 8 | 0x11;

	/**
	 * 贵宾信息
	 */
	short VIP_QQ_TELPHONE_DETAILS = ModuleCodeConstants.PAY << 8 | 0x20;

	// #########招募###
	/**
	 * 查询招募信息
	 */
	short QUERY_RECRUIT_INFO = ModuleCodeConstants.RECRUIT << 8 | 0x00;

	/**
	 * 招募新武将
	 */
	short RECRUIT_NEW = ModuleCodeConstants.RECRUIT << 8 | 0x01;

	/**
	 * 单个招募信息
	 */
	short QUERY_SINGLE_RECRUIT_INFO = ModuleCodeConstants.RECRUIT << 8 | 0x02;

	/**
	 * 招募新武将批量招募武将
	 */
	short BATCH_RECRUIT = ModuleCodeConstants.RECRUIT << 8 | 0x03;

	// #############消息
	/**
	 * 获得卡牌通知
	 */
	short NOTIFY_OBTAIN_ROLE_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x00;

	/**
	 * 获得物品通知
	 */
	short NOTIFY_OBTAIN_GOODS_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x01;

	/**
	 * 获得秘籍通知
	 */
	short NOTIFY_OBTAIN_BOOK_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x02;

	/**
	 * 宝石合成超过或者等于10级通知
	 */
	short NOTIFY_GEM_LEVEL_TO_10_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x03;

	/**
	 * 群豪谱排名第一变更通知
	 */
	short NOTIFY_HERO_NO_1_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x04;

	/**
	 * 消息盒子消息个数
	 */
	short MESSAGE_BOX_STATE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x05;

	/**
	 * 战斗消息
	 */
	short BATTLE_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x06;

	/**
	 * 社交信息
	 */
	short SOCIAL_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x07;

	/**
	 * 邮件信息
	 */
	short MAIL_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x08;

	/**
	 * 系统推送信息
	 */
	short NOTIFY_SYSTEM_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x09;

	/**
	 * 群豪谱结算
	 */
	short ARENA_BALANCE_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0A;

	/**
	 * 群豪谱结算预告
	 */
	short ARENA_BALANCE_TRAILER_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0B;

	/**
	 * 世界BOSS结算
	 */
	short WORLD_BOSS_BALANCE_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0C;

	/**
	 * 群豪谱前5名变更
	 */
	short ARENA_TOP_5_CHANGED_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0D;

	/**
	 * 无量山通关
	 */
	short CLIMB_PASSED_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0E;

	/**
	 * 英雄榜第一名上线
	 */
	short HERO_RANK_TOP_1_ONLINE_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x0F;

	/**
	 * 群豪谱第一名上线
	 */
	short ARENA_RANK_TOP_1_OFFLINE_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x30;

	/**
	 * 禁言玩家
	 */
	short GAG_PLAYER_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x31;

	/**
	 * 砸金蛋
	 */
	short EGG_FRENZY_NOTIFY_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x32;

	/**
	 * 消息通知
	 */
	short MESSAGE_NOTIFY = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x33;

	/**
	 * 领取邮件
	 */
	short MAIL_RECEIVE_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x10;

	/**
	 * 请求领取所有邮件物品
	 */
	short GET_ALL_MAIL_REWARD = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x11;

	/**
	 * 删除邮件
	 */
	short DELETE_MAIL = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x12;

	/**
	 * 删除所有邮件
	 */
	short DELETE_ALL_MAIL = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x13;

	/**
	 * 单个邮件状态变更
	 */
	short SINGLE_MAIL_STATUS_CHANGED = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x20;

	/**
	 * 多个邮件状态变更
	 */
	short MUTIL_MAIL_STATUS_CHANGED = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x21;

	/**
	 * 新邮件列表
	 */
	short NEW_MAIL_LIST = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x22;

	/**
	 * 删除邮件成功
	 */
	short DELETE_MAIL_SUCCESS = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x23;

	/**
	 * 领取邮件奖励成功
	 */
	short GET_MAIL_REWARD_SUCCESS = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x24;

	/**
	 * 开启特殊返利宝箱
	 */
	short SPECAIL_REBATE_OPEN = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x25;
	/**
	 * 标记邮件已读
	 */
	short READ_MAIL = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x26;
	// ****************************2016年11月7日12:23:17添加，游戏公告***************************************************
	/**
	 * 可重复系统消息主动推出
	 */
	short REPEAT_SYSTEM_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x60;

	/**
	 * 可重复系统消息列表主动推出
	 */
	short REPEAT_SYSTEM_MESSAGE_LIST = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x61;

	/**
	 * 可重复系统消息删除
	 */
	short DEL_REPEAT_SYSTEM_MESSAGE = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x63;

	/**
	 * 满足某个条件上线发送全服通知
	 */
	short ONLINE_TO_SERVER = ModuleCodeConstants.NOTIFY_MESSAGE << 8 | 0x70;
	// ************************************************************************************

	// ######系统设置######
	/**
	 * 获取系统设置信息
	 */
	short SETTING_INFO = ModuleCodeConstants.SETTING << 8 | 0x01;

	/**
	 * 玩家反馈(我也不明白玩家反馈怎么会在系统设置模块，但是策划文档是这样，所以....)
	 */
	short SETTING_FEEDBACK = ModuleCodeConstants.SETTING << 8 | 0x02;

	/**
	 * 更改VIP标识的显示状态
	 */
	short CHANGE_VIP_SHOW = ModuleCodeConstants.SETTING << 8 | 0x03;

	/**
	 * 分享
	 */
	short SHARE = ModuleCodeConstants.SETTING << 8 | 0x04;

	/**
	 * 分享回调
	 */
	short SHARE_CALL_BACK = ModuleCodeConstants.SETTING << 8 | 0x05;

	/**
	 * 防沉迷系统
	 */
	short ANTI_ADDICTION = ModuleCodeConstants.SETTING << 8 | 0x06;

	/**
	 * 实名奖励
	 */
	short REAL_NAME_REWARD = ModuleCodeConstants.SETTING << 8 | 0x07;

	// ######任务(成就)######

	/**
	 * 任务列表
	 */
	short MISSION_LIST = ModuleCodeConstants.MISSION << 8 | 0x01;
	/**
	 * 领取任务(成就)奖励
	 */
	short MISSION_REWARD = ModuleCodeConstants.MISSION << 8 | 0x02;

	/**
	 * 任务进度完成
	 */
	short MISSION_COMPLETED = ModuleCodeConstants.MISSION << 8 | 0x03;

	/**
	 * 通知新任务
	 */
	short MISSION_NOTIFY_NEW = ModuleCodeConstants.MISSION << 8 | 0x04;
	/**
	 * 任务进度变化
	 */
	short MISSION_STEP_UPDATE = ModuleCodeConstants.MISSION << 8 | 0x05;

	/**
	 * 7日目标列表
	 */
	short SEVEN_DAYS_GOAL_TASK_LIST = ModuleCodeConstants.MISSION << 8 | 0x51;
	/**
	 * 领取7日目标奖励
	 */
	short SEVEN_DAYS_GOAL_TASK_REWARD = ModuleCodeConstants.MISSION << 8 | 0x52;

	/**
	 * 7日目标进度完成
	 */
	short SEVEN_DAYS_GOAL_TASK_COMPLETED = ModuleCodeConstants.MISSION << 8 | 0x53;

	/**
	 * 通知新7日目标
	 */
	short SEVEN_DAYS_GOAL_TASK_NOTIFY_NEW = ModuleCodeConstants.MISSION << 8 | 0x54;

	/**
	 * 7日目标进度变化
	 */
	short SEVEN_DAYS_GOAL_TASK_STEP_UPDATE = ModuleCodeConstants.MISSION << 8 | 0x55;
	/**
	 * 7日目标活跃奖励领取
	 */
	short SEVEN_DAYS_GOAL_TASK_RECEIVE_REWARD = ModuleCodeConstants.MISSION << 8 | 0x56;
	/**
	 * 通知强化大师改变
	 */
	short NOTIFY_TASK_EXTRA_CHANGE = ModuleCodeConstants.MISSION << 8 | 0x57;

	/**
	 * 折扣商品信息
	 */
	short DISCOUNT_SHOP_ITEM_INFO = ModuleCodeConstants.MISSION << 8 | 0x60;

	/**
	 * 领取活跃度奖励
	 */
	short AWARD_DAILY_MISSION_ACTIVITY = ModuleCodeConstants.MISSION << 8 | 0x61;

	/**
	 * 跟新活跃度信息
	 */
	short UPDATE_DAILY_MISSION_ACTIVITY = ModuleCodeConstants.MISSION << 8 | 0x62;
	
	/**
	 * 领取周活跃度奖励
	 */
	short AWARD_WEEK_MISSION_ACTIVITY = ModuleCodeConstants.MISSION << 8 | 0x63;
	
	/**
	 * 领取成长值奖励
	 */
	short AWARD_GROWTH_VALUE= ModuleCodeConstants.MISSION << 8 | 0x64;

	// ########体力系统(挑战次数)(可恢复资源)########

	/**
	 * 挑战次数纪录
	 */
	short CHALLENGE_TIME = ModuleCodeConstants.CHALLENGE_TIME << 8 | 0x01;

	/**
	 * 挑战次数纪录列表
	 */
	short CHALLENGE_TIME_LIST = ModuleCodeConstants.CHALLENGE_TIME << 8 | 0x02;

	/**
	 * 购买挑战次数结果
	 */
	short BUY_CHALLENGE_TIME_RESULT = ModuleCodeConstants.CHALLENGE_TIME << 8 | 0x03;

	/**
	 * 购买挑战次数
	 */
	short BUY_CHALLENGE_TIME = ModuleCodeConstants.CHALLENGE_TIME << 8 | 0x03;

	/**
	 * 
	 */
	short RESET_CHALLENGE_WAIT_TIME = ModuleCodeConstants.CHALLENGE_TIME << 8 | 0x05;

	/**
	 * 获得用餐信息
	 */
	short QIYU_GET_DINING = ModuleCodeConstants.QIYU << 8 | 0x01;
	/**
	 * 用餐
	 */
	short QIYU_DINING = ModuleCodeConstants.QIYU << 8 | 0x02;
	/**
	 * 补领
	 */
	short QIYU_DINING_COST = ModuleCodeConstants.QIYU << 8 | 0x04;

	/**
	 * 获得运营活动开关信息
	 */
	short QIYU_FACTION_SWITH = ModuleCodeConstants.QIYU << 8 | 0x03;

	/**
	 * 获得签到信息
	 */
	short SIGN_GET_SIGN = ModuleCodeConstants.SIGN << 8 | 0x01;
	/**
	 * 签到
	 */
	short SIGN_DO_SIGN = ModuleCodeConstants.SIGN << 8 | 0x02;
	/**
	 * 领取连续签到奖励
	 */
	short RECEIVE_ACCUMULATIVESIGN_REWARD = ModuleCodeConstants.SIGN << 8 | 0x03;

	// ************************契约(合同)模块*************************
	/**
	 * 获取契约(合同)
	 */
	short CONTRACT_QUERY = ModuleCodeConstants.CONTRACT << 8 | 0x00;

	/**
	 * 购买契约(合同)
	 */
	short CONTRACT_BUY = ModuleCodeConstants.CONTRACT << 8 | 0x01;

	/**
	 * 每日领取契约(合同)奖励
	 */
	short CONTRACT_GET_DAILY_REWARD = ModuleCodeConstants.CONTRACT << 8 | 0x02;

	// ************************契约(合同)模块*************************
	/**
	 * 领取(验证)邀请码
	 */
	short MY_INVITE_CODE = ModuleCodeConstants.INVITE_CODE << 8 | 0x00;
	/**
	 * 领取(验证)邀请码
	 */
	short VERIFY_INVITE_CODE = ModuleCodeConstants.INVITE_CODE << 8 | 0x01;

	/**
	 * 领取累计发送邀请码奖励
	 */
	short GET_SEND_REWARD = ModuleCodeConstants.INVITE_CODE << 8 | 0x02;

	/**
	 * 发送邀请码详情
	 */
	short GET_MY_INVITE_CODE_INFO = ModuleCodeConstants.INVITE_CODE << 8 | 0x03;

	/**
	 * 发送新邀请码详情
	 */
	short GET_MY_NEW_INVITE_CODE_INFO = ModuleCodeConstants.INVITE_CODE << 8 | 0x04;

	/**
	 * 领取(验证)新邀请码
	 */
	short VERIFY_NEW_INVITE_CODE = ModuleCodeConstants.INVITE_CODE << 8 | 0x05;

	// ------------------------图谱------------------------
	/**
	 * 
	 */
	short QUERY_TUPU = ModuleCodeConstants.TUPU << 8 | 0x00;

	// ------------------------血战------------------------
	/**
	 * 查询血战战阵信息
	 */
	short BLOODY_INFO = ModuleCodeConstants.BLOODY << 8 | 0x00;

	/**
	 * 血战角色上阵
	 */
	short BLOODY_ROLE_ON_WAR_SIDE = ModuleCodeConstants.BLOODY << 8 | 0x01;

	/**
	 * 血战角色角色改变阵位
	 */
	short BLOODY_CHANGE_STATION = ModuleCodeConstants.BLOODY << 8 | 0x02;

	/**
	 * 血战角色下阵
	 */
	short BLOODY_ROLE_OFF_WAR_SIDE = ModuleCodeConstants.BLOODY << 8 | 0x03;

	/**
	 * 血战战阵容量
	 */
	short BLOODY_WAR_SIDE_CAPACITY = ModuleCodeConstants.BLOODY << 8 | 0x05;

	/**
	 * 血战布阵结果
	 */
	short BLOODY_WAR_SIDE_CONFIG_RESULT = ModuleCodeConstants.BLOODY << 8 | 0x06;

	/**
	 * 血战敌人npc简单信息列表
	 */
	short BLOODY_ENEMY_SIMPLE_INFO_LIST = ModuleCodeConstants.BLOODY << 8 | 0x07;
	/**
	 * 血战敌人npc详细信息信息列表
	 */
	short BLOODY_ENEMY_INFO_LIST = ModuleCodeConstants.BLOODY << 8 | 0x08;

	/**
	 * 血战敌人关卡信息
	 */
	short BLOODY_ENEMY_DETAIL = ModuleCodeConstants.BLOODY << 8 | 0x09;

	/**
	 * 挑战血战敌人关卡
	 */
	short BLOODY_CHALLENGE_ENEMY = ModuleCodeConstants.BLOODY << 8 | 0x10;

	/**
	 * 查询血战宝箱详情
	 */
	short BLOODY_QUERY_BOX_DETAIL = ModuleCodeConstants.BLOODY << 8 | 0x11;

	/**
	 * 血战宝箱随机
	 */
	short BLOODY_RONDOM_BLOODY_BOX = ModuleCodeConstants.BLOODY << 8 | 0x12;

	/**
	 * 血战宝箱抽奖(洗牌)
	 */
	short BLOODY_SHUFFLE_BLOODY_BOX = ModuleCodeConstants.BLOODY << 8 | 0x13;

	/**
	 * 血战宝箱抽奖(领奖)
	 */
	short BLOODY_GET_BOX = ModuleCodeConstants.BLOODY << 8 | 0x14;

	/**
	 * 血战鼓舞
	 */
	short BLOODY_INSPIRE = ModuleCodeConstants.BLOODY << 8 | 0x15;

	/**
	 * 血战重置
	 */
	short BLOODY_RESET = ModuleCodeConstants.BLOODY << 8 | 0x20;

	/**
	 * 血战重置前下发通知客户端进行准备
	 */
	short BLOODY_RESET_BEFORE_NOTIFY = ModuleCodeConstants.BLOODY << 8 | 0x21;

	/**
	 * 血战扫荡
	 */
	short BLOODY_SWEEP = ModuleCodeConstants.BLOODY << 8 | 0x30;

	// ----------------------武学----------------------------
	/**
	 * 装备武学
	 */
	short EQUIP_MARTIAL = ModuleCodeConstants.MARTIAL << 8 | 0x01;

	/**
	 * 武学列表
	 */
	short MARTIAL_ROLE_LIST = ModuleCodeConstants.MARTIAL << 8 | 0x02;

	/**
	 * 武学等级升级
	 */
	short MARTIAL_LEVEL_UP = ModuleCodeConstants.MARTIAL << 8 | 0x03;

	/**
	 * 武学合成
	 */
	short MARTIAL_SYNTHESIS = ModuleCodeConstants.MARTIAL << 8 | 0x04;

	/**
	 * 武学附魔
	 */
	short MARTIAL_ENCHANT = ModuleCodeConstants.MARTIAL << 8 | 0x05;

	/**
	 * 玩家持有所有角色的武学列表信息
	 */
	short MARTIAL_ALL_INFO = ModuleCodeConstants.MARTIAL << 8 | 0x06;

	/**
	 * 一键附魔
	 */
	short ONEKEY_MARTIAL_ENCHANT = ModuleCodeConstants.MARTIAL << 8 | 0x07;

	/**
	 * 一键装备武学
	 */
	short ONE_KEY_MARTIAL_EQUIP = ModuleCodeConstants.MARTIAL << 8 | 0x10;

	// ####################################活动##################################
	// ##########开服活动############
	/**
	 * 所有排行榜
	 */
	short ACTIVITY_STARTUP_RANK_LIST_ALL = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x04;
	/**
	 * 特定排行榜（单个）
	 */
	short ACTIVITY_STARTUP_RANK_LIST_SINGLE = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x03;
	/**
	 * 活动领奖记录
	 */
	short ACTIVITY_STARTUP_REWARD_RECORD = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x02;
	/**
	 * 所有活动状态
	 */
	short ACTIVITY_STARTUP_STATUS_LIST = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x01;
	/**
	 * 活动状态（单个）
	 */
	short ACTIVITY_STARTUP_STATUS_SINGLE = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x00;

	/**
	 * 获取活动奖励
	 */
	short GET_ACTIVITY_REWARD = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x05;

	/**
	 * 一键领取所有奖励
	 */
	short ONE_KEY_GOT_ACTIVITY_REWARD = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x06;

	/**
	 * 战斗力返还活动单档
	 */
	short POWER_RETURN_GROUP = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x07;

	/**
	 * 战斗力返还配置
	 */
	short POWER_RETURN_CONFIG = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x08;

	/**
	 * 战斗力达成进度
	 */
	short POWER_RETURN_DELAY = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x09;

	/**
	 * 单条领奖
	 */
	short POWER_RETURN_GET_SIGNLE_REWARD = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x10;

	/**
	 * 所有领奖记录
	 */
	short POWER_RETURGN_RET_REWARD_LIST = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x11;

	/**
	 * 通知新的激活档位
	 */
	short NOTIFY_OPEN_POWER_RETURN_GEAR = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x12;

	/**
	 * 战斗力返还请求领奖
	 */
	short GET_POWER_RETURN_REWARD = ModuleCodeConstants.ACTIVITY_STARTUP << 8 | 0x13;

	// ################--------运营活动--------------######################
	/**
	 * 单条活动信息
	 */
	short ACTIVITY_INFO_SINGLE = ModuleCodeConstants.ACTIVITY << 8 | 0x00;
	/**
	 * 所有活动信息
	 */
	short ACTIVITY_INFO_LIST = ModuleCodeConstants.ACTIVITY << 8 | 0x01;
	/**
	 * 单个活动进度
	 */
	short ACTIVITY_PROGRESS_SINGLE = ModuleCodeConstants.ACTIVITY << 8 | 0x02;
	/**
	 * 所有活动进度
	 */
	short ACTIVITY_PROGRESS_LIST = ModuleCodeConstants.ACTIVITY << 8 | 0x03;

	/**
	 * 请求领取活动奖励
	 */
	short GOT_ACTIVITY_REWARD = ModuleCodeConstants.ACTIVITY << 8 | 0x04;

	/**
	 * 购买通宝类型
	 */
	short BUY_MONEY_SHOP = ModuleCodeConstants.ACTIVITY << 8 | 0x05;

	/**
	 * 一键获得所有奖励
	 */
	short ONE_KEY_GOT_REWARD = ModuleCodeConstants.ACTIVITY << 8 | 0x06;

	/**
	 * 一键领取所有奖励的结果
	 */
	short ONE_KEY_GOT_REWARD_RESULT = ModuleCodeConstants.ACTIVITY << 8 | 0x07;

	// --------------兑换----------------------------
	/**
	 * 兑换礼包
	 */
	short EXCHANGE_GIFTS = ModuleCodeConstants.EXCHANGE << 8 | 0x00;

	/**
	 * 所有服务器开关
	 */
	short SERVER_SWITCH_INFO = ModuleCodeConstants.SERVER_SWITCH << 8 | 0x00;

	/**
	 * 单条服务器开关
	 */
	short SERVER_SWITCH_ONE = ModuleCodeConstants.SERVER_SWITCH << 8 | 0x01;

	// -------------------------排行榜-----------------------------------------
	/**
	 * 所有英雄战力榜
	 */
	short RANKING_LIST_HERO = ModuleCodeConstants.RANKING_LIST << 8 | 0x00;
	/**
	 * 5英雄战力榜
	 */
	short RANKING_LIST_FIVE = ModuleCodeConstants.RANKING_LIST << 8 | 0x01;
	/**
	 * 15英雄战力榜
	 */
	short RANKING_LIST_FIFTEEN = ModuleCodeConstants.RANKING_LIST << 8 | 0x02;
	/**
	 * 群好榜
	 */
	short RANKING_LIST_ARENA = ModuleCodeConstants.RANKING_LIST << 8 | 0x03;
	/**
	 * 侠客榜
	 */
	short RANKING_LIST_XIAKE = ModuleCodeConstants.RANKING_LIST << 8 | 0x04;
	/**
	 * 等级榜
	 */
	short RANKING_LIST_LEVEL = ModuleCodeConstants.RANKING_LIST << 8 | 0x05;
	/**
	 * 星星榜
	 */
	short RANKING_LIST_STAR = ModuleCodeConstants.RANKING_LIST << 8 | 0x06;
	/**
	 * 军团榜
	 */
	short RANKING_LIST_CORPS = ModuleCodeConstants.RANKING_LIST << 8 | 0x07;
	/**
	 * 无量榜
	 */
	short RANKING_LIST_WULIANG = ModuleCodeConstants.RANKING_LIST << 8 | 0x08;
	/**
	 * 神兵榜
	 */
	short RANKING_LIST_SHENBING = ModuleCodeConstants.RANKING_LIST << 8 | 0x09;
	/**
	 * 世界BOSS
	 */
	short RANKING_LIST_WORLD_BOSS = ModuleCodeConstants.RANKING_LIST << 8 | 0x10;

	/**
	 * 招募积分排行榜
	 */
	short RANKING_LIST_RECRUIT_INTEGRAL = ModuleCodeConstants.RANKING_LIST << 8 | 0x11;

	/**
	 * 招募积分榜概要信息
	 */
	short RANKING_LIST_RECRUIT_INTEGRAL_OUTLINE = ModuleCodeConstants.RANKING_LIST << 8 | 0x12;

	/**
	 * 锦囊妙计排行榜
	 */
	short PACKAGE_PLOT_RANKING_LIST = ModuleCodeConstants.RANKING_LIST << 8 | 0x13;

	/**
	 * 查询排行榜信息
	 */
	short RANKING_LIST_QUERY_BASE_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x50;

	/**
	 * 排行榜点赞
	 */
	short RANKING_LIST_PRAISE = ModuleCodeConstants.RANKING_LIST << 8 | 0x60;

	/**
	 * 排行榜点赞信息
	 */
	short RANKING_LIST_PRAISE_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x70;
	/**
	 * 公会等级排名信息
	 */
	short RANKING_LIST_GUILD_LEVEL_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x80;

	/**
	 * 公会战斗力排名信息
	 */
	short RANKING_LIST_GUILD_POWER_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x81;
	/**
	 * 公会副本通关排行
	 */
	short RANKING_LIST_GUILD_ZONE_PASS_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x82;
	/**
	 * 公会伤害排行
	 */
	short RANKING_LIST_GUILD_ZONE_HURT_INFO = ModuleCodeConstants.RANKING_LIST << 8 | 0x83;

	// ------------------------压力测试------------------------
	short PROFILE_LOGIN = ModuleCodeConstants.PROFILE << 8 | 0x00;

	// -------------------------------好友---------------------------------------
	/** 获取好友列表 */
	short GAIN_FRIEND_LIST = ModuleCodeConstants.FRIEND << 8 | 0x00;
	/** 获取申请列表 */
	short GAIN_FRIEND_APPLY_LIST = ModuleCodeConstants.FRIEND << 8 | 0x01;
	/** 获取推荐列表 */
	short GAIN_RECOMMEND_FRIEND_LIST = ModuleCodeConstants.FRIEND << 8 | 0x02;
	/** 申请好友 */
	short APPLY_FRIEND = ModuleCodeConstants.FRIEND << 8 | 0x03;
	/** 处理好友申请 */
	short EXEC_APPLY_FRIEND = ModuleCodeConstants.FRIEND << 8 | 0x04;
	/** 赠送礼物 */
	short GIVE_GIFI = ModuleCodeConstants.FRIEND << 8 | 0x05;
	/** 领取赠送礼物 */
	short DRAW_GIVE_GIFI = ModuleCodeConstants.FRIEND << 8 | 0x06;
	/** 删除好友 */
	short DELETE_FRIEND = ModuleCodeConstants.FRIEND << 8 | 0x07;
	/** 新的申请 */
	short NEW_APPLY = ModuleCodeConstants.FRIEND << 8 | 0x08;
	/** 新的好友 */
	short NEW_FRIEND = ModuleCodeConstants.FRIEND << 8 | 0x09;
	/** 新的礼物 */
	short NEW_GIFI = ModuleCodeConstants.FRIEND << 8 | 0x0A;
	/** 有好友删除 */
	short NEW_DELETE_FRIEND = ModuleCodeConstants.FRIEND << 8 | 0x0B;
	/** 好友挑战 (未被使用) */
	short FRIEND_CHALLENGE = ModuleCodeConstants.FRIEND << 8 | 0x0C;
	/** 查询玩家 */
	short QUERY_PLAYER = ModuleCodeConstants.FRIEND << 8 | 0x0D;

	/** 修改需要的侠客 */
	short UPDATE_DEMAND = ModuleCodeConstants.FRIEND << 8 | 0x0E;
	/** 修改提供的侠客 */
	short UPDATE_PROVIDE = ModuleCodeConstants.FRIEND << 8 | 0x0F;

	/** 跨服新好友的处理结果 */
	short NEW_FRIEND_RESULT = ModuleCodeConstants.FRIEND << 8 | 0x10;

	/** 请求最近玩家列表 */
	short GAIN_RECENTLY_PLAYER = ModuleCodeConstants.FRIEND << 8 | 0x11;

	/** 请求玩家礼物记录 */
	short GAIN_GIFT_RECORD = ModuleCodeConstants.FRIEND << 8 | 0x12;

	/** 请求领取好友亲密度奖励 */
	short DRAW_INTIMATE_REWARD = ModuleCodeConstants.FRIEND << 8 | 0x13;

	/** 黑名单 */
	short BLACKLIST = ModuleCodeConstants.FRIEND << 8 | 0x14;

	/** 添加黑名单 */
	short ADD_BLACKLIST = ModuleCodeConstants.FRIEND << 8 | 0x15;

	/** 移除黑名单 */
	short REMOVE_BLACKLIST = ModuleCodeConstants.FRIEND << 8 | 0x16;

	/** 添加至最近聊天 */
	short ADD_RECENTLY_CHAT_PLAYER = ModuleCodeConstants.FRIEND << 8 | 0x17;
	/** 新的聊天通知 */
	short NEW_CHAT_NOTIFY = ModuleCodeConstants.FRIEND << 8 | 0x18;
	// -------------------------------公会---------------------------------------
	/** 我的公会成员信息 */
	short MY_GUILD_MEMBER_INFO = ModuleCodeConstants.GUILD << 8 | 0x00;
	/** 创建公会 */
	short CREATE_GUILD = ModuleCodeConstants.GUILD << 8 | 0x01;
	/** 申请公会 */
	short APPLY_GUILD = ModuleCodeConstants.GUILD << 8 | 0x02;
	/** 同意申请 */
	short AGREED_APPLY = ModuleCodeConstants.GUILD << 8 | 0x03;
	/** 删除申请 */
	short DELETE_APPLY = ModuleCodeConstants.GUILD << 8 | 0x04;
	/** 退出公会 */
	short EXIT_GUILD = ModuleCodeConstants.GUILD << 8 | 0x05;
	/** 获取公会信息 */
	short GAIN_GUILD_INFO = ModuleCodeConstants.GUILD << 8 | 0x06;
	/** 获取公会成员信息 */
	short GAIN_GUILD_MEMBER_INFO = ModuleCodeConstants.GUILD << 8 | 0x07;
	/** 修改帮会信息 */
	short UPDATE_GUILD_INFO = ModuleCodeConstants.GUILD << 8 | 0x08;
	/** 操作公会 */
	short OPERATE_GUILD = ModuleCodeConstants.GUILD << 8 | 0x09;
	/** 获取申请列表 */
	short GAIN_APPLY_LIST = ModuleCodeConstants.GUILD << 8 | 0x0A;
	/** 取消申请 */
	short CANCEL_APPLY = ModuleCodeConstants.GUILD << 8 | 0x0B;
	/** 获取公会状态 */
	short GAIN_GUILD_STAT = ModuleCodeConstants.GUILD << 8 | 0x0C;
	/** 祭拜 */
	short WORSHIP = ModuleCodeConstants.GUILD << 8 | 0x0D;
	/** 打开祭拜宝箱 */
	short OPEN_WORSHIP_BOX = ModuleCodeConstants.GUILD << 8 | 0x0E;
	/** 结交玩家 */
	short MAKE_PLAYER = ModuleCodeConstants.GUILD << 8 | 0x0F;
	/** 领取结交玩家奖励 */
	short DRAW_MAKE_PLAYER_AWARD = ModuleCodeConstants.GUILD << 8 | 0x10;
	/** 帮会动态 */
	short GUILD_DYNAMIC = ModuleCodeConstants.GUILD << 8 | 0x11;
	/** 获取公会邀请 */
	short GAIN_GUILD_INVITATION = ModuleCodeConstants.GUILD << 8 | 0x12;
	/** 发送公会邀请 */
	short SEND_INVITATION = ModuleCodeConstants.GUILD << 8 | 0x13;
	/** 操作邀请 */
	short OPERATE_INVITATION = ModuleCodeConstants.GUILD << 8 | 0x14;
	/** 修改结交金币 */
	short UPDATE_MAKE_COIN = ModuleCodeConstants.GUILD << 8 | 0x15;
	/** 公会被操作 */
	short OPTED_GUILD = ModuleCodeConstants.GUILD << 8 | 0x16;
	/** 被邀请 */
	short INVITATIONED = ModuleCodeConstants.GUILD << 8 | 0x17;
	/** 被邀请 */
	short NEW_GUILD_APPLY = ModuleCodeConstants.GUILD << 8 | 0x18;
	/** 获取公会动态 */
	short GAIN_GUILD_DYNAMIC = ModuleCodeConstants.GUILD << 8 | 0x19;
	/** 获取公会副本信息 */
	short GAIN_GUILD_ZONE_INFO = ModuleCodeConstants.GUILD << 8 | 0x1a;
	/** 更新公会副本关卡信息 */
	short UPDATE_CHECK_POINT = ModuleCodeConstants.GUILD << 8 | 0x1b;
	/** 重置公会副本 */
	short RESET_GUILD_ZONE = ModuleCodeConstants.GUILD << 8 | 0x1c;
	/** 锁定帮派副本 */
	short LOCKED_GUILD_ZONE = ModuleCodeConstants.GUILD << 8 | 0x1d;
	/** 解锁帮派副本 */
	short UNLOCKED_GUILD_ZONE = ModuleCodeConstants.GUILD << 8 | 0x1e;
	/** 挑战帮派副本 */
	short CHALLENGE_GUILD_ZONE = ModuleCodeConstants.GUILD << 8 | 0x1f;
	/** 领取dps奖励 */
	short DRAW_DPS_AWARD = ModuleCodeConstants.GUILD << 8 | 0x20;
	/** 获得副本信息 */
	short GAIN_ZONE_INFO = ModuleCodeConstants.GUILD << 8 | 0x21;
	/** 获得公会修炼场信息 */
	short GAIN_GUILD_PRACTICE_INFO = ModuleCodeConstants.GUILD << 8 | 0x22;
	/** 公会修炼场研究 */
	short GUILD_PRACTICE_STUDY = ModuleCodeConstants.GUILD << 8 | 0x23;
	/** 开始修炼 */
	short START_PRACTICE = ModuleCodeConstants.GUILD << 8 | 0x24;
	/** 结束修炼 */
	short END_PRACTICE = ModuleCodeConstants.GUILD << 8 | 0x25;
	/** 传承 */
	short INHERITANCE = ModuleCodeConstants.GUILD << 8 | 0x26;
	/** 获取玩家修炼信息 */
	short GAIN_PLAYER_PRACTICE_INFO = ModuleCodeConstants.GUILD << 8 | 0x27;
	/** 修改帮派名称 */
	short UPDATE_GUILD_NAME = ModuleCodeConstants.GUILD << 8 | 0x28;
	/** 修改帮派旗帜 */
	short UPDATE_GUILD_BANNER_ID = ModuleCodeConstants.GUILD << 8 | 0x29;
	/** 获取玩家武将碎片互捐信息 */
	short GAIN_CHIP_DONATE_INFO = ModuleCodeConstants.GUILD << 8 | 0x2a;
	/** 捐献武将碎片 */
	short DONATE_ROLE_CHIP = ModuleCodeConstants.GUILD << 8 | 0x2b;
	/** 发送武将碎片募捐捐信息 */
	short SEND_CHIP_DONATE_INFO = ModuleCodeConstants.GUILD << 8 | 0x2c;
	/** 发起碎片募捐 */
	short SPONSOR_CHIP_DONATE = ModuleCodeConstants.GUILD << 8 | 0x2d;
	/** 设置公会申请条件 */
	short SET_GUILD_APPLY_CONDITION = ModuleCodeConstants.GUILD << 8 | 0x2e;
	/** 领取募捐的武将碎片 */
	short RECEIVE_CHIP_REWARD = ModuleCodeConstants.GUILD << 8 | 0x2f;
	/** 更新募捐信息 */
	short UPDATE_DONATE_CHIP_INFO = ModuleCodeConstants.GUILD << 8 | 0x30;
	/** 会长发送军团邮件 */
	short SEND_GUILD_MAIL = ModuleCodeConstants.GUILD << 8 | 0x31;
	/** 查询公会信息 */
	short QUERY_GUILD_INFO = ModuleCodeConstants.GUILD << 8 | 0x32;
	/** 获取公会碎片互捐信息 */
	short QUERY_GUILD_CHIP_DONATE_INFO = ModuleCodeConstants.GUILD << 8 | 0x33;
	/** 发起碎片募捐通知 */
	short SPONSOR_CHIP_DONATE_NOTIFY = ModuleCodeConstants.GUILD << 8 | 0x34;
	/** 发起武将驻守 */
	short START_GARRISON = ModuleCodeConstants.GUILD << 8 | 0x35;
	/** 驻守武将归队 */
	short END_GARRISON = ModuleCodeConstants.GUILD << 8 | 0x36;
	/** 我的驻守武将信息 */
	short MY_GARRISON_ROLE_INFO = ModuleCodeConstants.GUILD << 8 | 0x37;
	/** 军团驻守武将信息 */
	short GUILD_GARRISON_ROLE_INFO = ModuleCodeConstants.GUILD << 8 | 0x38;
	/** 玩家公会副本信息 */
	short PLAYER_GUILD_ZONE_INFO = ModuleCodeConstants.GUILD << 8 | 0x40;
	/** 更新玩家公会副本信息 */
	short UPDATE_PLAYER_GUILD_ZONE_INFO = ModuleCodeConstants.GUILD << 8 | 0x41;
	/** 一键领取奖励 */
	short ONE_KEY_DRAW_DPS_AWARD = ModuleCodeConstants.GUILD << 8 | 0x42;
	/** 科技突破 */
	short BREAK_SCIENCE = ModuleCodeConstants.GUILD << 8 | 0x43;
	/** 科技研习 */
	short STUDY_SCIENCE = ModuleCodeConstants.GUILD << 8 | 0x44;
	/** 军团科技信息（单条） */
	short SINGLE_SCIENCE_INFO = ModuleCodeConstants.GUILD << 8 | 0x45;
	/** 军团所有科技信息 */
	short ALL_GUILD_SCIENCE_INFOS = ModuleCodeConstants.GUILD << 8 | 0x46;
	/** 玩家科技信息（单条） */
	short PLAYER_SINGLE_SCIENCE_INFO = ModuleCodeConstants.GUILD << 8 | 0x47;
	/** 玩家所有科技信息 */
	short PLAYER_SCIENCE_INFOS = ModuleCodeConstants.GUILD << 8 | 0x48;
	/** 玩家征伐信息 */
	short MY_WAR_INFO = ModuleCodeConstants.GUILD << 8 | 0x49;
	/** 公会征伐信息 */
	short GUILD_WAR_INFO = ModuleCodeConstants.GUILD << 8 | 0x4a;
	/** 挑战征伐关卡 */
	short CHALLENGE_WAR = ModuleCodeConstants.GUILD << 8 | 0x4b;
	/** 开始巡逻征伐关卡 */
	short START_PATROL = ModuleCodeConstants.GUILD << 8 | 0x4c;
	/** 领取巡逻奖励 */
	short RECEIVE_PATROL_REWARD = ModuleCodeConstants.GUILD << 8 | 0x4d;
	/** 鞭策 */
	short WAR_SPUR = ModuleCodeConstants.GUILD << 8 | 0x4e;
	/** 查询巡逻奖励 */
	short QUERY_PATROL_REWARD = ModuleCodeConstants.GUILD << 8 | 0x4f;
	/** 鞭策信息 */
	short SPUR_INFO = ModuleCodeConstants.GUILD << 8 | 0x50;
	/** 公会战斗异常 */
	short GUILD_ZONE_EXCEPTION = ModuleCodeConstants.GUILD << 8 | 0x51;
	/** 公会战斗压后台返回 */
	short GUILD_ZONE_RETURN = ModuleCodeConstants.GUILD << 8 | 0x52;

	// -------------------------------助战---------------------------------------
	/** 获取助战信息 */
	short GAIN_ASSISTANT = ModuleCodeConstants.ASSISTANT << 8 | 0x1;
	/** 开启助战格子 */
	short OPEN_ASSISTANT_GRID = ModuleCodeConstants.ASSISTANT << 8 | 0x2;
	/** 修改助战角色 */
	short UPDATE_ASSISTANT_ROLE = ModuleCodeConstants.ASSISTANT << 8 | 0x3;
	/** 升级契合等级 */
	short LEVEL_UP_AGREE = ModuleCodeConstants.ASSISTANT << 8 | 0x4;

	// ------------------------------------无量山-北窟-----------------------------------------
	/**
	 * 重置北窟
	 */
	short NORTH_CAVE_RESET = ModuleCodeConstants.NORTH_CAVE << 8 | 0x00;

	/**
	 * 北窟挑战结果
	 */
	short NORTH_CAVE_CHALLENGE_RESULT = ModuleCodeConstants.NORTH_CAVE << 8 | 0x01;

	/**
	 * 北窟详细信息
	 */
	short NORTH_CAVE_DETAILS = ModuleCodeConstants.NORTH_CAVE << 8 | 0x02;

	/**
	 * 北窟新关卡列表
	 */
	short NORTH_CAVE_GAME_LEVEL_LIST = ModuleCodeConstants.NORTH_CAVE << 8 | 0x03;

	/**
	 * 北窟扫荡
	 */
	short NORTH_CAVE_SWEEP = ModuleCodeConstants.NORTH_CAVE << 8 | 0x10;

	/**
	 * 北窟属性选择成功通知
	 */
	short NORTH_CAVE_ATTRIBUTE_CHOICE = ModuleCodeConstants.NORTH_CAVE << 8 | 0x20;

	/**
	 * 北窟属性选择列表
	 */
	short NORTH_CAVE_ATTRIBUTE_CHOICE_LIST = ModuleCodeConstants.NORTH_CAVE << 8 | 0x21;

	/**
	 * 北窟属性选择单个
	 */
	short NORTH_CAVE_ATTRIBUTE_CHOICE_SINGLE = ModuleCodeConstants.NORTH_CAVE << 8 | 0x22;

	/**
	 * 北窟关卡挑战选项刷新
	 */
	short NORTH_CAVE_OPTIONS_REFRESH = ModuleCodeConstants.NORTH_CAVE << 8 | 0x23;

	/**
	 * 获取宝箱奖励
	 */
	short NORTH_CAVE_GET_CHEST_REWARD = ModuleCodeConstants.NORTH_CAVE << 8 | 0x25;

	/**
	 * 一键扫荡
	 */
	short NORTH_CAVE_ONE_KEY_SWEEP = ModuleCodeConstants.NORTH_CAVE << 8 | 0x30;


	// -----------------------------------------------人物缘分道具-----------------------------------------------
	/**
	 * 使用角色缘分道具列表
	 */
	short ROLE_FATE_LIST = ModuleCodeConstants.ROLE_FATE << 8 | 0x00;

	/**
	 * 配缘
	 */
	short MATCH_ROLE_FATE = ModuleCodeConstants.ROLE_FATE << 8 | 0x01;
	/**
	 * 单条缘分
	 */
	short ROLE_FATE_INFO = ModuleCodeConstants.ROLE_FATE << 8 | 0x02;

	// ---------------------------------------天命---------------------------------
	/** 天命界面信息 */
	short DESTINY_INFO = ModuleCodeConstants.DESTINY << 8 | 0x01;

	/** 点亮/一键点亮 */
	short DESTINY_ILLUME = ModuleCodeConstants.DESTINY << 8 | 0x02;
	/** 一键点亮 (消耗所有的星星) */
	short ONE_KEY_DESTINY_ILLUME = ModuleCodeConstants.DESTINY << 8 | 0x04;

	/** 属性加成 */
	short DESTINY_ATTR_ADD = ModuleCodeConstants.DESTINY << 8 | 0x03;

	// --------------------------------------称号-------------------------------------
	/** 穿戴或更换称号 */
	short REQUEST_CHANG_TITLE = ModuleCodeConstants.TITLE << 8 | 0x01;

	/** 登录加载 */
	short TITLE_LOGIN = ModuleCodeConstants.TITLE << 8 | 0x02;

	/** 玩家在线时，到期的称号推送ID给客户端 */
	short TIME_OUT_TITIL = ModuleCodeConstants.TITLE << 8 | 0x03;

	/** 获得新的称号 */
	short GET_NEW_TITLE = ModuleCodeConstants.TITLE << 8 | 0x04;

	/** 更改穿戴的称号 */
	short CHANGE_WEAR_TITLE = ModuleCodeConstants.TITLE << 8 | 0x05;

	/** 称号倒计时 */
	short TITLE_COUNTDOWN = ModuleCodeConstants.TITLE << 8 | 0x06;

	// ------------------------------------头像------------------------------------------

	/**
	 * 请求更换头像
	 */
	short CHANGE_IMAGE = ModuleCodeConstants.PORTRAIT << 8 | 0x01;

	/**
	 * 请求更换边框
	 */
	short CHANGE_FRAME = ModuleCodeConstants.PORTRAIT << 8 | 0x02;

	/**
	 * 登录加载边框信息
	 */
	short FRAME_LOGIN = ModuleCodeConstants.PORTRAIT << 8 | 0x03;

	/**
	 * 边框的使用期限已到期指令发至客户端
	 */
	short FRAME_TIME_OUT = ModuleCodeConstants.PORTRAIT << 8 | 0x04;

	/**
	 * 获得新的边框（有同时获得多个边框）
	 */
	short GET_NEW_FRAME = ModuleCodeConstants.PORTRAIT << 8 | 0x05;

	/**
	 * 更换边框结果
	 */
	short CHANGE_FRAME_RESULT = ModuleCodeConstants.PORTRAIT << 8 | 0x06;

	/**
	 * 登录向客户端发送头像集合
	 */
	short ALL_IMAGES = ModuleCodeConstants.PORTRAIT << 8 | 0x07;

	/**
	 * 获得新的头像
	 */
	short GET_NEW_IMAGE = ModuleCodeConstants.PORTRAIT << 8 | 0x08;

	/**
	 * 边框进度更新
	 */
	short FRAME_PROGRESS = ModuleCodeConstants.PORTRAIT << 8 | 0x09;

	/**
	 * 所有边框进度
	 */
	short ALL_FRAME_PROGRESS = ModuleCodeConstants.PORTRAIT << 8 | 0x10;

	/**
	 * 获得玩家头像信息
	 */
	short GET_HEAD_IMG_INFO = ModuleCodeConstants.PORTRAIT << 8 | 0x11;

	/**
	 * 边框倒计时
	 */
	short FRAME_COUNTDOWN = ModuleCodeConstants.PORTRAIT << 8 | 0x16;

	/**
	 * 更换立绘
	 */
	short CHANGE_PORTRAITURE = ModuleCodeConstants.PORTRAIT << 8 | 0x17;

	/**
	 * 获得玩家立绘信息
	 */
	short GET_ALL_PORTRAITURE = ModuleCodeConstants.PORTRAIT << 8 | 0x18;

	/**
	 * 新的立绘
	 */
	short NEW_PORTRAITURE = ModuleCodeConstants.PORTRAIT << 8 | 0x19;
	
	/**
	 * 更换背景
	 */
	short CHANGE_BGIMAGE = ModuleCodeConstants.PORTRAIT << 8 | 0x20;
	// ---------------------------------弹幕-------------------------------------
	/**
	 * 弹幕内容
	 */
	short GET_BARRAGE_INFO = ModuleCodeConstants.BARRAGE << 8 | 0x01;
	/**
	 * 发送弹幕消息
	 */
	short SEND_BARRAGE_MASSAGE = ModuleCodeConstants.BARRAGE << 8 | 0x02;
	/**
	 * 通知弹幕消息
	 */
	short NOTIFY_BARRAGE_MASSAGE = ModuleCodeConstants.BARRAGE << 8 | 0x03;
	// ------------------------------------资源回购-------------------------------------
	/**
	 * 资源回购信息
	 */
	short RESOURCE_BACK_INFO = ModuleCodeConstants.RESOURCE_BACK << 8 | 0x01;
	/**
	 * 购买可回购的资源
	 */
	short BUY_RESOURCE_BACK = ModuleCodeConstants.RESOURCE_BACK << 8 | 0x02;
	// ---------------------------------剿匪------------------------------------------
	/**
	 * 剿匪信息
	 */
	short DESTROY_BANDIT_INFO = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x01;
	/**
	 * 击打怪物
	 */
	short BEAT_MONSTER = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x02;
	/**
	 * 购买云游商品
	 */
	short BUY_SHOP = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x03;
	/**
	 * 领取宝箱
	 */
	short RECEIVE_BOX = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x04;
	/**
	 * 回答问题
	 */
	short ANSWER = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x05;
	/**
	 * 通知boss出现
	 */
	short NOTIFY_BOSS_APPEAR = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x06;
	/**
	 * 通知新怪物出现
	 */
	short NOTIFY_MONSTER_APPEAR = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x07;
	/**
	 * 通知新的奇遇
	 */
	short NOTIFY_NEW_ACTIVITY = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x08;
	/**
	 * 挑战剿匪boss
	 */
	short CHALLENGE_DESTROY_BANDIT_BOSS = ModuleCodeConstants.DESTROY_BANDIT << 8 | 0x09;
	// ---------------------------------首次战斗------------------------------------------
	/**
	 * 保存首次战斗进度
	 */
	short SAVE_FIRST_FIGHT = ModuleCodeConstants.FIRST_FIGHT << 8 | 0x01;
	// ---------------------------------问卷调查------------------------------------------
	/**
	 * 发送问卷信息
	 */
	short SEND_QUESTIONNAIRE_INFO = ModuleCodeConstants.QUESTIONNAIRE << 8 | 0x01;
	/**
	 * 发送所有问卷信息
	 */
	short SEND_ALL_QUESTIONNAIRE_INFO = ModuleCodeConstants.QUESTIONNAIRE << 8 | 0x02;
	/**
	 * 发送问卷id
	 */
	short OPEN_QUESTIONNAIRE_ID = ModuleCodeConstants.QUESTIONNAIRE << 8 | 0x03;

	// ----------------------------------通用配置-----------------------------------------
	/**
	 * 宣传图通用配置
	 */
	short COMMON_CONFIG_ADVERTISE_PRIORITY = ModuleCodeConstants.COMMON_CONFIG << 8 | 0x00;
	/**
	 * 点将通用配置
	 */
	short COMMON_CONFIG_RECRUIT = ModuleCodeConstants.COMMON_CONFIG << 8 | 0x01;

	/**
	 * 大张伟通知全服
	 */
	short DAZHANGWEI_SEND_TO_ALL = ModuleCodeConstants.COMMON_CONFIG << 8 | 0x02;

	// ----------------------------------全民福利----------------------------------------------------
	/**
	 * 请求领取福利奖励
	 */
	short REQUEST_WELFARE_AWARD = ModuleCodeConstants.WELFARE << 8 | 0x00;
	/**
	 * 全民福利单个领奖记录
	 */
	short WELFARE_SINGLE_RECORD = ModuleCodeConstants.WELFARE << 8 | 0x01;
	/**
	 * 全民福利所有领奖记录
	 */
	short WELFARE_ALL_RECORD = ModuleCodeConstants.WELFARE << 8 | 0x02;
	/**
	 * 全服购买基金总数
	 */
	short FUND_TOTAL_COUNT = ModuleCodeConstants.WELFARE << 8 | 0x03;
	/**
	 * 购买成长基金
	 */
	short REQUEST_BUY_GROWTH_FUND = ModuleCodeConstants.WELFARE << 8 | 0x04;

	/**
	 * 获取全服购买总数
	 */
	short REQUEST_FUND_TOTAL_INFO = ModuleCodeConstants.WELFARE << 8 | 0x05;
	// ----------------------------------红包雨----------------------------------------------------
	/**
	 * 领取红包雨的红包
	 */
	short RECEIVE_RED_BAG = ModuleCodeConstants.RED_BAG_RAIN << 8 | 0x00;
	/**
	 * 红包雨的信息
	 */
	short RED_BAG_RAIN_INFO = ModuleCodeConstants.RED_BAG_RAIN << 8 | 0x01;
	/**
	 * 开始抢红包
	 */
	short START_JOIN_RED_BAG = ModuleCodeConstants.RED_BAG_RAIN << 8 | 0x02;
	// --------------------------------锦囊妙计------------------------------------------------------
	/**
	 * 锦囊妙计活动配置
	 */
	short PACKAGE_PLOT_CONFIG_INFO = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x01;
	/**
	 * 购买道具结果
	 */
	short BUY_PROP_RESULT = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x02;
	/**
	 * 锦囊妙计排行奖励配置
	 */
	short PACK_PLOT_RANK_REWARD_INFO = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x03;
	/**
	 * 打开锦囊
	 */
	short OPEN_PACKAGE = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x04;
	/**
	 * 购买锦囊道具
	 */
	short BUY_PACKAGE_PROP = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x05;
	/**
	 * 打开锦囊排行榜
	 */
	short OPEN_PACK_PLOT_RANK = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x06;
	/**
	 * 打开锦囊获得的道具
	 */
	short GIVE_PACK_PLOT_REWARD = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x07;
	/**
	 * 查询玩家开启锦囊获得道具记录
	 */
	short QUERY_JNMJ_REWARD_RECORD = ModuleCodeConstants.PACKAGE_PLOT << 8 | 0x08;

	// ------------------------------战斗力比拼-------------------------------------------------

	/**
	 * 大比拼信息
	 */
	short GET_BIGCOMPET_INFO = ModuleCodeConstants.ACTIVITY << 8 | 0x08;
	/**
	 * 请求排行榜
	 */
	short BIGCOMPET_RANK = ModuleCodeConstants.ACTIVITY << 8 | 0x09;
	/**
	 * 请求保底奖励
	 */
	short BIGCOMPETMIN_REWARD = ModuleCodeConstants.ACTIVITY << 8 | 0x10;
	/**
	 * 通知成就进度变化
	 */
	short BIGCOMPETMIN_NOTICE = ModuleCodeConstants.ACTIVITY << 8 | 0x11;

	// ----------------------------------寻宝----------------------------------------
	/**
	 * 寻宝活动配置
	 */
	short SEEK_TREASURE_INFO = ModuleCodeConstants.ACTIVITY << 8 | 0x12;
	/**
	 * 寻宝排行奖励配置
	 */
	short SEEK_RANK_REWARD_INFO = ModuleCodeConstants.ACTIVITY << 8 | 0x13;
	/**
	 * 寻宝次数额外奖励配置
	 */
	short SEEK_EXTRA_REWARD_INFO = ModuleCodeConstants.ACTIVITY << 8 | 0x14;
	/**
	 * 寻宝成功
	 */
	short SEEK_TREASURE_SUCC = ModuleCodeConstants.ACTIVITY << 8 | 0x15;
	/**
	 * 开启锦囊获得道具的记录
	 */
	short SEEK_RECORD_LIST = ModuleCodeConstants.ACTIVITY << 8 | 0x16;
	/**
	 * 开始寻宝
	 */
	short BEGAN_SEEK_TREASURE = ModuleCodeConstants.ACTIVITY << 8 | 0x17;
	/**
	 * 打开寻宝排行榜
	 */
	short OPEN_SEEK_TREASURE_RANK = ModuleCodeConstants.ACTIVITY << 8 | 0x18;
	/**
	 * 获得寻宝记录
	 */
	short GET_SEEK_RECORD = ModuleCodeConstants.ACTIVITY << 8 | 0x19;
	/**
	 * 获得寻宝的额外奖励
	 */
	short GET_SEEK_EXTRA_REWARD = ModuleCodeConstants.ACTIVITY << 8 | 0x20;
	// ---------------------------------机遇---------------------------------------------
	/**
	 * 掷骰子
	 */
	short SHOOT_DICE = ModuleCodeConstants.OPPORTUNITY << 8 | 0x00;
	/**
	 * 发送机遇配置信息
	 */
	short SEND_OPPORTUNITY_INFO = ModuleCodeConstants.OPPORTUNITY << 8 | 0x01;
	/**
	 * 发送机遇排行配置
	 */
	short OPPORTUNITY_RANK_CONFIG = ModuleCodeConstants.OPPORTUNITY << 8 | 0x02;
	/**
	 * 发送机遇事件
	 */
	short SEND_OPPORTUNITY_EVENT = ModuleCodeConstants.OPPORTUNITY << 8 | 0x03;
	/**
	 * 请求领取事件奖励
	 */
	short REQUEST_GOT_EVENT_REWARD = ModuleCodeConstants.OPPORTUNITY << 8 | 0x04;
	/**
	 * 获得机遇排行榜信息
	 */
	short GOT_OPPORTUNITY_RANK_INFO = ModuleCodeConstants.OPPORTUNITY << 8 | 0x05;

	// ----------------------- 限时商城
	// ---------------------------------------------------
	/**
	 * 发送限时商城的所有信息
	 */
	short SEND_ALL_LIMITTIME_SHOP_NIFO = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x00;
	/**
	 * 发送单个商店的配置信息
	 */
	short SEND_LIMITTIME_SINGLE_SHOP = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x01;
	/**
	 * 发送商品信息
	 */
	short SINGLE_LIMITTIME_GOODS = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x02;
	/**
	 * 购买限时商店物品
	 */
	short BUY_LIMITTIME_GOODS = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x03;
	/**
	 * 团购详情
	 */
	short REQUEST_GROUPON_DETAILS = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x04;
	/**
	 * 请求满返商店购买记录
	 */
	short REQUEST_FULL_RETURN_RECARDS = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x05;
	/**
	 * 满返消耗掉的资源数量
	 */
	short FULL_RETURN_ALREADY_COST = ModuleCodeConstants.LIMITED_TIME_SHOP << 8 | 0x06;

	// ----------------------- Live2d
	// ---------------------------------------------------
	/**
	 * 请求更换头像 | 更换头像结果
	 */
	short CHANGE_LIVE2D_ROLE = ModuleCodeConstants.LIVE2D << 8 | 0x01;
}
