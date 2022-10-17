package com.dykj.rpg.common.attribute.consts;

/**
* 属性配置表
* 生成时间 2021-02-24 14:12:08
*/
public enum AttributeBasicEnum {
	LI_LIANG_ZHI(1,"力量值"),
	MIN_JIE_ZHI(2,"敏捷值"),
	ZHI_LI_ZHI(3,"智力值"),
	TI_ZHI_ZHI(4,"体质值"),
	NAI_LI_ZHI(5,"耐力值"),
	YUN_QI_ZHI(6,"运气值"),
	MING_ZHONG_ZHI(11,"命中值"),
	SHAN_BI_ZHI(12,"闪避值"),
	BAO_JI_ZHI(13,"暴击值"),
	MIAN_BAO_ZHI(14,"免暴值"),
	BAO_SHANG_ZHI(15,"暴伤值"),
	REN_XING_ZHI(16,"韧性值"),
	PO_JI_ZHI(17,"破击值"),
	GE_DANG_ZHI(18,"格挡值"),
	PO_SHANG_ZHI(19,"破伤值"),
	DANG_SHANG_ZHI(20,"档伤值"),
	GONG_JI_ZHI(21,"攻击值"),
	FANG_YU_ZHI(22,"防御值"),
	PO_FANG_ZHI(23,"破防值"),
	JING_TONG_ZHI(24,"精通值"),
	KANG_XING_ZHI(25,"抗性值"),
	TE_SHU_ZHUANG_TAI_JING_TONG_ZHI(26,"特殊状态精通值"),
	TE_SHU_ZHUANG_TAI_KANG_XING_ZHI(27,"特殊状态抗性值"),
	SHANG_HAI_FU_DONG_ZHI(49,"伤害浮动值"),
	SHANG_HAI_FU_DONG_LV(50,"伤害浮动率"),
	FU_JIA_MING_ZHONG_LV(51,"附加命中率"),
	FU_JIA_BAO_JI_LV(52,"附加暴击率"),
	FU_JIA_BAO_JI_SHANG_HAI_BEI_LV(53,"附加暴击伤害倍率"),
	FU_JIA_GE_DANG_LV(54,"附加格挡率"),
	FU_JIA_GE_DANG_SHANG_HAI_BEI_LV(55,"附加格挡伤害倍率"),
	FU_JIA_YI_CHANG_ZHUANG_TAI_LV(56,"附加异常状态率"),
	FU_JIA_TE_SHU_ZHUANG_TAI_LV(57,"附加特殊状态率"),
	YUAN_SU_XIAO_GUO_QIANG_HUA_XI_SHU(61,"元素效果强化系数"),
	YUAN_SU_XIAO_GUO_RUO_HUA_XI_SHU(62,"元素效果弱化系数"),
	TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU(63,"条件效果强化系数"),
	TIAO_JIAN_XIAO_GUO_RUO_HUA_XI_SHU(64,"条件效果弱化系数"),
	TE_SHU_XIAO_GUO_QIANG_HUA_XI_SHU(65,"特殊效果强化系数"),
	TE_SHU_XIAO_GUO_RUO_HUA_XI_SHU(66,"特殊效果弱化系数"),
	ZUI_DA_SHENG_MING_ZHI(101,"最大生命值"),
	SHENG_MING_ZHI_HUI_FU_ZHI(102,"生命值回复值"),
	SHENG_MING_ZHI_HUI_FU_YAN_CHI_ZHI(103,"生命值回复延迟值"),
	SHENG_MING_ZHI_HUI_FU_JIAN_GE_ZHI(104,"生命值回复间隔值"),
	ZUI_DA_HU_DUN_ZHI(111,"最大护盾值"),
	HU_DUN_ZHI_HUI_FU_ZHI(112,"护盾值回复值"),
	HU_DUN_ZHI_HUI_FU_YAN_CHI_ZHI(113,"护盾值回复延迟值"),
	HU_DUN_ZHI_HUI_FU_JIAN_GE_ZHI(114,"护盾值回复间隔值"),
	ZUI_DA_BIAN_SHEN_NENG_LIANG_ZHI(121,"最大变身能量值"),
	BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_ZHI(122,"变身能量值回复值"),
	BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_YAN_CHI_ZHI(123,"变身能量值回复延迟值"),
	BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_JIAN_GE_ZHI(124,"变身能量值回复间隔值"),
	ZUI_DA_JI_NENG_ZI_YUAN_ZHI(131,"最大技能资源值"),
	JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI(132,"技能资源值回复值"),
	JI_NENG_ZI_YUAN_ZHI_HUI_FU_YAN_CHI_ZHI(133,"技能资源值回复延迟值"),
	JI_NENG_ZI_YUAN_ZHI_HUI_FU_JIAN_GE_ZHI(134,"技能资源值回复间隔值"),
	JI_NENG_ZI_YUAN_XIAO_HAO_XI_SHU(151,"技能资源消耗系数"),
	JI_NENG_LENG_QUE_HUI_FU_XI_SHU(152,"技能冷却回复系数"),
	JI_NENG_SHI_FANG_SU_DU_XI_SHU(153,"技能释放速度系数"),
	YI_DONG_SU_DU_PAO_ZHI(201,"移动速度跑值"),
	YI_DONG_SU_DU_ZOU_ZHI(202,"移动速度走值"),
	SHI_YE_FAN_WEI_ZHI(203,"视野范围值"),
	SHOU_JI_DONG_ZUO_FANG_FANG_DENG_JI(204,"受击动作防方等级"),
	ZHUANG_TAI_SHU_XING(300,"状态属性");
	public int id;
	public String desc;
	private AttributeBasicEnum(int id,String desc){
		this.id = id;
		this.desc = desc;
	}
}