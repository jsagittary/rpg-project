package com.dykj.rpg.battle.constant;

/**
 * 战斗类型静态参数
 */
public class BattleTypeConstant {
    /**
     * ------------------------------------单人模式---------------------------------------
     */
    /**
     * 普通关卡（boss通关）
     * 玩法说明：普通的先杀怪，到最后打BOSS的玩法类型。
     * 玩法体验：在打小怪的过程中积累，最后在BOSS战中爆发的整体体验。
     */
    public final static byte BATTLE_TYPE_GENERAL = 1;

    /**
     * 擂台车轮战-精英
     * 玩法说明：多个精英小BOSS，轮流上场战斗，等待中不能回复生命值。
     * 玩法体验：多种精英BOSS，多种不同技能，玩家对未知挑战的期待感。
     */
    public final static byte BATTLE_TYPE_BOSS_WHEEL = 2;

    /**
     * 单挑BOSS
     * 玩法说明：整个关卡只设定一个BOSS。
     * 玩法体验：此起彼伏的纯BOSS战。
     */
    public final static byte BATTLE_TYPE_BOSS_SINGLE = 3;

    /**
     * 护送类玩法
     * 玩法说明：玩家护送NPC到达目的地，NPC不具备攻击能力，怪物会主动攻击NPC。NPC死亡则算失败。
     * 玩法体验：玩家需要注意NPC状态，选择适当的释放技能时2机。
     */
    public final static byte BATTLE_TYPE_NPC_ESCORT = 4;

    /**
     * 与NPC互动通关
     * 玩法说明：与NPC组队，NPC协助玩家通关，NPC即时死亡，也不会失败。
     * 玩法体验：玩家与NPC存在技能搭配效果，给玩家轻松通关的感觉。
     */
    public final static byte BATTLE_TYPE_NPC_ASSIST = 5;

    /**
     * 救援类
     * 玩法说明：拯救被折磨&俘获的NPC，获得少许奖励，救援的NPC数量达到一定数量则成功通关。
     * 玩法体验：视觉上给玩家一定的压迫感，成功拯救后给与玩家惊喜奖励。
     */
    public final static byte BATTLE_TYPE_NPC_RESCUE = 6;

    /**
     * 守护基地
     * 玩法说明：整个场景为固定区域，怪物从四面大方一波波不断侵袭过来，一波怪刷新后，固定时间后，会刷新下一波。怪物全部击杀算成功通关。
     * 玩法体验：通过不断的刷新怪物，给与玩家足够的压迫感。
     */
    public final static byte BATTLE_TYPE_BASE_GUARD = 7;

    /**
     * 维修基地
     * 玩法说明：多种实现方式，可以是玩家边战斗，边收集修理资源对组件进行修理，也可以是玩家打怪，给NPC争取修理时间。
     * 玩法体验：间歇性的战斗体验，战斗结束做修理读条，被怪物打断后，继续战斗。
     */
    public final static byte BATTLE_TYPE_BASE_REPAIR = 8;

    /**
     * 限时击杀
     * 玩法说明：在规定时间里，击杀怪物数量达到一定的数量，则记为通关。
     * 玩法体验：纯粹战斗能力的验证。简单粗暴版割草型体验。
     */
    public final static byte BATTLE_TYPE_TIME_KILL = 9;

    /**
     * 限时逃生
     * 玩法说明：多样化的表现形式，如洞穴崩塌、毒液蔓延、火势蔓延，场景崩坏等等类型表现形式，核心是要求玩家在规定的时间里击杀掉怪物，并且快速逃离到终点。
     *          若玩家在规定时间里未能达到安全区域时，则战斗记为失败。（可添加场景技能，增加玩家手操影响结果变量）
     * 玩法体验：视觉上的极限施压，让玩家会想尽办法可以快速击杀怪物并且通关。
     */
    public final static byte BATTLE_TYPE_TIME_ESCAPE = 10;

    /**
     * 解谜破局
     * 玩法说明：在某些特定关卡里，某个中间事件点添加少许玩家可操作的限时类简单解谜益智游戏，益智小游戏可以以界面的形式弹出。
     * 玩法体验：轻松休闲有挑战，在长期的杀怪体验中更换一种体验模式。
     */
    public final static byte BATTLE_TYPE_PUZZLE = 11;

    /**
     * ------------------------------------组队模式---------------------------------------
     */

}
