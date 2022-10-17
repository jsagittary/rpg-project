package com.dykj.rpg.common.attribute.consts;

/**
* 属性配置表
* 生成时间 2021-02-24 14:12:08
*/
public enum DateFixEnum {
	JI_NENG_TI_HUAN(0,"技能替换"),
	ZHI_XIU_ZHENG(1,"值修正"),
	ZENG_BI_XIU_ZHENG(2,"增比修正"),
	JIAN_BI_XIU_ZHENG(3,"减比修正"),
	WU(4,"无"),
	LIN_SHI_FU_GAI_XIU_ZHENG(9,"临时覆盖修正"),
	FU_GAI_XIU_ZHENG(10,"覆盖修正"),
	DIAN_LIANG_XIU_ZHENG(11,"点亮修正");
	public int id;
	public String desc;
	private DateFixEnum(int id,String desc){
		this.id = id;
		this.desc = desc;
	}
}