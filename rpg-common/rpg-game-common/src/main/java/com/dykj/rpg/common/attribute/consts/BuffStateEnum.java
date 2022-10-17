package com.dykj.rpg.common.attribute.consts;

/**
* 属性配置表
* 生成时间 2021-02-24 14:12:08
*/
public enum BuffStateEnum {
	WEI_YI(1,"位移"),
	ZHU_DONG_JI_NENG_SHI_FANG(2,"主动技能释放"),
	HUN_JI_SHI_FANG(3,"魂技释放"),
	SHI_LI(4,"视力");
	public int id;
	public String desc;
	private BuffStateEnum(int id,String desc){
		this.id = id;
		this.desc = desc;
	}
}