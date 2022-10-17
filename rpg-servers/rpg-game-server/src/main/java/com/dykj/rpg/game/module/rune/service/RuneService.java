package com.dykj.rpg.game.module.rune.service;

import com.dykj.rpg.common.data.model.PlayerRuneModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.rune.RuneAssemblyRq;
import com.dykj.rpg.protocol.rune.RuneReplaceRq;
import com.dykj.rpg.protocol.rune.RuneRs;
import com.dykj.rpg.protocol.rune.RuneUninstallRq;

/**
 * @Description 符文service
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/20
 */
public interface RuneService
{
    /**
     * 符文装配
     * @param player 玩家信息
     * @param runeAssemblyRq 协议
     */
    public void runeAssembly(Player player, RuneAssemblyRq runeAssemblyRq);

    /**
     * 符文卸载
     * @param player 玩家信息
     * @param runeUninstallRq 协议
     */
    public void runeUninstall(Player player, RuneUninstallRq runeUninstallRq);

    /**
     * 符文替换
     * @param player 玩家信息
     * @param runeReplaceRq 协议
     */
    public void runeReplace(Player player, RuneReplaceRq runeReplaceRq);

    /**
     * 封装协议
     * @param playerRuneModel 符文信息
     * @return 协议
     */
    public RuneRs runeRs(PlayerRuneModel playerRuneModel);
}