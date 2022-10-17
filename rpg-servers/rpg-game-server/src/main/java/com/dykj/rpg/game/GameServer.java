package com.dykj.rpg.game;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dykj.rpg.common.config.BaseManager;
import com.dykj.rpg.game.module.quartz.event.JobRegisterEventManager;
import com.dykj.rpg.game.module.server.service.GameServerService;
import com.dykj.rpg.game.nacos.service.GameServerStart;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Author: jyb
 * @Date: 2018/12/25 16:49
 * @Description:
 */
public class GameServer {

	public static Logger logger = LoggerFactory.getLogger("game");

	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		Map<String, BaseManager> base = appContext.getBeansOfType(BaseManager.class);
		for (Entry<String, BaseManager> entry : base.entrySet()) {
			BaseManager baseManager = entry.getValue();
			baseManager.setApplicationContext(appContext);
			baseManager.load();
		}
		
		// 加载所有配置信息
		/*ConfigManage configManage = appContext.getBean(ConfigManage.class);
		configManage.setApplicationContext(appContext);
		configManage.load();*/
		// 加载GM指令manage
		/*GmStrategyManage gmStrategyManage = appContext.getBean(GmStrategyManage.class);
		gmStrategyManage.setApplicationContext(appContext);
		gmStrategyManage.load();*/
		// 加载背包操作类型服务类
		/*ItemOperateRealizeManage itemOperateRealizeManage = appContext.getBean(ItemOperateRealizeManage.class);
		itemOperateRealizeManage.setApplicationContext(appContext);
		itemOperateRealizeManage.load();*/
		// 加载定时事件
		JobRegisterEventManager jobRegisterEventManager = appContext.getBean(JobRegisterEventManager.class);
		jobRegisterEventManager.doEvents(null);

		BeanFactory.setApplicationContext(appContext);
		GameServerService gameServerService = BeanFactory.getBean(GameServerService.class);
		gameServerService.stopMasterKafkaListener();

		GameServerStart gameServerStart = appContext.getBean(GameServerStart.class);
		gameServerStart.startJetty();
		gameServerStart.startNetty();

		logger.info("GameServer START SUCCESS %% STARTTIME=" + (System.currentTimeMillis() - now));
		logger.info("GameServer START COMPLETE");
		logger.info("LINUX LOG SUCCESS");

	}
}
