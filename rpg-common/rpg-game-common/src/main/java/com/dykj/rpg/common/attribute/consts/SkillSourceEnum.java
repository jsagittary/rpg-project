package com.dykj.rpg.common.attribute.consts;

/**
* 属性配置表
* 生成时间 2021-02-24 14:12:08
*/
public enum SkillSourceEnum {
	NU_QI(1,"怒气",0),
	FA_LI(2,"法力",1),
	NENG_LIANG(3,"能量",1),
	XIE_LING(4,"邪灵",0),
	YU_LIU_A(5,"预留A",0),
	YU_LIU_B(6,"预留B",0),
	YU_LIU_C(7,"预留C",0),
	YU_LIU_D(8,"预留D",0),
	YU_LIU_E(9,"预留E",0),
	YU_LIU_F(10,"预留F",0);
	public int id;
	public String desc;
	public int initType; //0=初始为0 ， 1=初始为最大值
	private SkillSourceEnum(int id,String desc,int initType){
		this.id = id;
		this.desc = desc;
		this.initType = initType;
	}

	public static SkillSourceEnum getSkillSourceEnum(int id){
		SkillSourceEnum[] skillSourceEnums = SkillSourceEnum.values();
		for(SkillSourceEnum skillSourceEnum : skillSourceEnums){
			if(skillSourceEnum.id == id){
				return skillSourceEnum;
			}
		}
		return null;
	}
}