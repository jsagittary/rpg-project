package com.dykj.rpg.uc.remote.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dykj.rpg.common.remote.game.IGameService;
import com.dykj.rpg.util.spring.BeanFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: jyb
 * @Date: 2020/9/15 11:50
 * @Description:
 */
@Service
public class GameConsumer {

    @Reference
    private IGameService gameService;

    public String getHost(int serverId){
        IGameService gameService = BeanFactory.getBean(IGameService.class);
        return  gameService.getHost(serverId);
    }
}
