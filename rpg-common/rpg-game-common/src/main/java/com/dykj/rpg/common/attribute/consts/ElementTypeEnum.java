package com.dykj.rpg.common.attribute.consts;

/**
* 属性配置表
* 生成时间 2021-02-24 14:12:08
*/
public enum ElementTypeEnum {
	WU_LI(1,"物理"),
	HUO(2,"火"),
	SHUI(3,"水"),
	FENG(4,"风"),
	TU(5,"土"),
	YAN(6,"炎"),
	BING(7,"冰"),
	LEI(8,"雷"),
	DU(9,"毒"),
	HEI_AN(10,"黑暗"),
	GUANG_MING(11,"光明"),
	HUN_DUN(12,"混沌"),
	ZHI_XU(13,"秩序"),
	QUAN(100,"全"),
	ZHI_LIAO(101,"治疗"),
	ZHEN_SHI(200,"真实");
	public int id;
	public String desc;
	private ElementTypeEnum(int id,String desc){
		this.id = id;
		this.desc = desc;
	}
}