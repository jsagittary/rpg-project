package com.dykj.rpg.battle.attribute;

import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;

import java.util.*;

public class RoleAttributes {

    Map<Integer, List<AttributeConfig>> attrs;

    public RoleAttributes(){
        attrs = new HashMap<>();
    }

    /**
     * 添加属性
     * @param attrStrList
     */
    public void addAttributes(List<String> attrStrList){
        if(attrStrList != null && attrStrList.size() > 0){
            for(String attrStr : attrStrList){
                AttributeConfig config = new AttributeConfig(attrStr);
                List<AttributeConfig> configList = attrs.get(config.id);
                if(configList == null){
                    configList = new ArrayList<>();
                    attrs.put(config.id,configList);
                }
                configList.add(config);
            }
        }
    }

    /**
     * 覆盖属性
     * @param id
     * @param type
     * @param num
     */
    public void coverAttribute(int id,int type,int num){
        List<AttributeConfig> configList = attrs.get(id);
        if(configList == null){
            configList = new ArrayList<>();
            AttributeConfig config = new AttributeConfig(id,type,num);
            configList.add(config);
            attrs.put(id,configList);
        }else{
            boolean findOldConfig = false;
            for(AttributeConfig attributeConfig : configList){
                if(attributeConfig.type1 == type){
                    attributeConfig.num = num;
                    findOldConfig = true;
                    break;
                }
            }
            if(!findOldConfig){
                AttributeConfig config = new AttributeConfig(id,type,num);
                configList.add(config);
            }
        }
    }

    /**
     * 获取角色所有属性配置
     * @return
     */
    public List<AttributeConfig> getAllAttributeConfigs(){
        List<AttributeConfig> configs = new ArrayList<>();
        Set<Integer> set = attrs.keySet();
        for(int id : set){
            if(attrs.get(id) != null){
                configs.addAll(attrs.get(id));
            }
        }
        return configs;
    }

    /**
     * 力量值
     */
    public int getLiLiangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.LI_LIANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 敏捷值
     */
    public int getMinJieZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.MIN_JIE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 智力值
     */
    public int getZhiLiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZHI_LI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 体质值
     */
    public int getTiZhiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TI_ZHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 耐力值
     */
    public int getNaiLiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.NAI_LI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 运气值
     */
    public int getYunQiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.YUN_QI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 命中值
     */
    public int getMingZhongZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.MING_ZHONG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 闪避值
     */
    public int getShanBiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHAN_BI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 暴击值
     */
    public int getBaoJiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.BAO_JI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 免暴值
     */
    public int getMianBaoZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.MIAN_BAO_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 暴伤值
     */
    public int getBaoShangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.BAO_SHANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 韧性值
     */
    public int getRenXingZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.REN_XING_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 破击值
     */
    public int getPoJiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.PO_JI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 格挡值
     */
    public int getGeDangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.GE_DANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 破伤值
     */
    public int getPoShangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.PO_SHANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 档伤值
     */
    public int getDangShangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.DANG_SHANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 攻击值
     */
    public int getGongJiZhi(int elementType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.GONG_JI_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == elementType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 防御值
     */
    public int getFangYuZhi(int elementType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FANG_YU_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == elementType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 破防值
     */
    public int getPoFangZhi(int elementType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.PO_FANG_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == elementType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 精通值
     */
    public int getJingTongZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JING_TONG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 抗性值
     */
    public int getKangXingZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.KANG_XING_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 特殊状态精通值
     */
    public int getTeShuZhuangTaiJingTongZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TE_SHU_ZHUANG_TAI_JING_TONG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 特殊状态抗性值
     */
    public int getTeShuZhuangTaiKangXingZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TE_SHU_ZHUANG_TAI_KANG_XING_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 伤害浮动值
     */
    public int getShangHaiFuDongZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHANG_HAI_FU_DONG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 伤害浮动率
     */
    public int getShangHaiFuDongLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHANG_HAI_FU_DONG_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加命中率
     */
    public int getFuJiaMingZhongLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_MING_ZHONG_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加暴击率
     */
    public int getFuJiaBaoJiLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_BAO_JI_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加暴击伤害倍率
     */
    public int getFuJiaBaoJiShangHaiBeiLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_BAO_JI_SHANG_HAI_BEI_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加格挡率
     */
    public int getFuJiaGeDangLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_GE_DANG_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加格挡伤害倍率
     */
    public int getFuJiaGeDangShangHaiBeiLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_GE_DANG_SHANG_HAI_BEI_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加异常状态率
     */
    public int getFuJiaYiChangZhuangTaiLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_YI_CHANG_ZHUANG_TAI_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 附加特殊状态率
     */
    public int getFuJiaTeShuZhuangTaiLv(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.FU_JIA_TE_SHU_ZHUANG_TAI_LV);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 元素效果强化系数
     */
    public int getYuanSuXiaoGuoQiangHuaXiShu(int elementType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.YUAN_SU_XIAO_GUO_QIANG_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == elementType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 元素效果弱化系数
     */
    public int getYuanSuXiaoGuoRuoHuaXiShu(int elementType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.YUAN_SU_XIAO_GUO_RUO_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == elementType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 条件效果强化系数
     */
    public int getTiaoJianXiaoGuoQiangHuaXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 条件效果弱化系数
     */
    public int getTiaoJianXiaoGuoRuoHuaXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_RUO_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 特殊效果强化系数
     */
    public int getTeShuXiaoGuoQiangHuaXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TE_SHU_XIAO_GUO_QIANG_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 特殊效果弱化系数
     */
    public int getTeShuXiaoGuoRuoHuaXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.TE_SHU_XIAO_GUO_RUO_HUA_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 最大生命值
     */
    public int getZuiDaShengMingZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_SHENG_MING_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 生命值回复值
     */
    public int getShengMingZhiHuiFuZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 生命值回复延迟值
     */
    public int getShengMingZhiHuiFuYanChiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 生命值回复间隔值
     */
    public int getShengMingZhiHuiFuJianGeZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 最大护盾值
     */
    public int getZuiDaHuDunZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_HU_DUN_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 护盾值回复值
     */
    public int getHuDunZhiHuiFuZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 护盾值回复延迟值
     */
    public int getHuDunZhiHuiFuYanChiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 护盾值回复间隔值
     */
    public int getHuDunZhiHuiFuJianGeZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 最大变身能量值
     */
    public int getZuiDaBianShenNengLiangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_BIAN_SHEN_NENG_LIANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 变身能量值回复值
     */
    public int getBianShenNengLiangZhiHuiFuZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 变身能量值回复延迟值
     */
    public int getBianShenNengLiangZhiHuiFuYanChiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 变身能量值回复间隔值
     */
    public int getBianShenNengLiangZhiHuiFuJianGeZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.BIAN_SHEN_NENG_LIANG_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 最大魂能量值
     */
    public int getZuiDaHunNengLiangZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_HUN_NENG_LIANG_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 魂能量值回复值
     */
    public int getHunNengLiangZhiHuiFuZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 魂能量值回复延迟值
     */
    public int getHunNengLiangZhiHuiFuYanChiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 魂能量值回复间隔值
     */
    public int getHunNengLiangZhiHuiFuJianGeZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 最大技能资源值
     */
    public List<AttributeConfig> getZuiDaJiNengZiYuanZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_JI_NENG_ZI_YUAN_ZHI);
        if(configs != null && configs.size() > 0){
            return configs;
        }
        return null;
    }
    /**
     * 最大技能资源值
     */
    public int getZuiDaJiNengZiYuanZhi(int skillSourceType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.ZUI_DA_JI_NENG_ZI_YUAN_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == skillSourceType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 技能资源值回复值
     */
    public List<AttributeConfig> getJiNengZiYuanZhiHuiFuZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs;
        }
        return null;
    }
    /**
     * 技能资源值回复值
     */
    public int getJiNengZiYuanZhiHuiFuZhi(int skillSourceType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == skillSourceType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 技能资源值回复延迟值
     */
    public List<AttributeConfig> getJiNengZiYuanZhiHuiFuYanChiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs;
        }
        return null;
    }
    /**
     * 技能资源值回复延迟值
     */
    public int getJiNengZiYuanZhiHuiFuYanChiZhi(int skillSourceType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_YAN_CHI_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == skillSourceType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 技能资源值回复间隔值
     */
    public List<AttributeConfig> getJiNengZiYuanZhiHuiFuJianGeZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            return configs;
        }
        return null;
    }
    /**
     * 技能资源值回复间隔值
     */
    public int getJiNengZiYuanZhiHuiFuJianGeZhi(int skillSourceType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_JIAN_GE_ZHI);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == skillSourceType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 技能资源消耗系数
     */
    public int getJiNengZiYuanXiaoHaoXiShu(int skillSourceType){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_ZI_YUAN_XIAO_HAO_XI_SHU);
        if(configs != null && configs.size() > 0){
            for(AttributeConfig config : configs){
                if(config.type1 == skillSourceType){
                    return config.num;
                }
            }
        }
        return 0;
    }

    /**
     * 技能冷却回复系数
     */
    public int getJiNengLengQueHuiFuXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_LENG_QUE_HUI_FU_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 技能释放速度系数
     */
    public int getJiNengShiFangSuDuXiShu(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.JI_NENG_SHI_FANG_SU_DU_XI_SHU);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 移动速度跑值
     */
    public int getYiDongSuDuPaoZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.YI_DONG_SU_DU_PAO_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 移动速度走值
     */
    public int getYiDongSuDuZouZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.YI_DONG_SU_DU_ZOU_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 视野范围值
     */
    public int getShiYeFanWeiZhi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHI_YE_FAN_WEI_ZHI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    /**
     * 受击动作防方等级
     */
    public int getShouJiDongZuoFangFangDengJi(){
        List<AttributeConfig> configs = attrs.get(AttributeBasicConstant.SHOU_JI_DONG_ZUO_FANG_FANG_DENG_JI);
        if(configs != null && configs.size() > 0){
            return configs.get(0).num;
        }
        return 0;
    }

    public RoleAttributes copy(){

        RoleAttributes roleAttributes = new RoleAttributes();
        Map<Integer, List<AttributeConfig>> copyAttrs = roleAttributes.attrs;
        Set<Map.Entry<Integer,List<AttributeConfig>>> keyValues = attrs.entrySet();
        for(Map.Entry<Integer,List<AttributeConfig>> keyValue : keyValues){
            List<AttributeConfig> copyAttributeConfigs = new ArrayList<>();
            List<AttributeConfig> attributeConfigs = keyValue.getValue();
            if(attributeConfigs.size() > 0){
                for(AttributeConfig config : attributeConfigs){
                    copyAttributeConfigs.add(new AttributeConfig(config.id, config.type1,config.type2,config.num));
                }
            }
            copyAttrs.put(keyValue.getKey(),copyAttributeConfigs);
        }

        return roleAttributes;
    }

}
