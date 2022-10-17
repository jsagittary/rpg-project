package com.dykj.rpg.game.module.quartz.job;

import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.quartz.core.QuartzConsts;
import com.dykj.rpg.util.spring.BeanFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2021/3/16 14:07
 * @Description
 */
public class HelloJob implements Job {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info(jobExecutionContext.getJobDetail().getJobDataMap().get(QuartzConsts.JOB_PRAMS).toString());
        System.out.println(BeanFactory.getBean(PlayerCacheService.class) );
    }
}