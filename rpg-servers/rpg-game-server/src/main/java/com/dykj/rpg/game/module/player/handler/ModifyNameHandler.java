package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.player.ModifyNameRq;
import com.dykj.rpg.protocol.player.ModifyNameRs;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/11 11:26
 * @Description:
 */
public class ModifyNameHandler extends GameHandler<ModifyNameRq> {
    @Override
    public void doHandler(ModifyNameRq modifyNameRq, Player player) {
        player.cache().getPlayerInfoModel().setName(modifyNameRq.getName());
        BeanFactory.getBean(PlayerInfoDao.class).queueUpdate(player.cache().getPlayerInfoModel());
        sendMsg(player,new ModifyNameRs(modifyNameRq.getName()));
    }
}
