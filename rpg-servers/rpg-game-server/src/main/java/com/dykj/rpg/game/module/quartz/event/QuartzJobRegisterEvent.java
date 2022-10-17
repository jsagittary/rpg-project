package com.dykj.rpg.game.module.quartz.event;

import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.quartz.core.JobEnum;
import com.dykj.rpg.game.module.quartz.job.HelloJob;
import com.dykj.rpg.game.module.quartz.core.QuartzConsts;
import com.dykj.rpg.game.module.quartz.core.QuartzManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jyb
 * @date 2021/3/16 20:37
 * @Description
 */
@Component
public class QuartzJobRegisterEvent extends AbstractEvent {
    @Resource
    private QuartzManager quartzManager;


    @Override
    public void doEvent(Object... prams) throws Exception {
        Map<String, Object> pram = new HashMap<>();
        pram.put(QuartzConsts.JOB_PRAMS, "测试cron");
//        quartzManager.execCronJob(JobEnum.TEST_CORN, "*/1 * * * * ?", HelloJob.class, pram);

        pram.put(QuartzConsts.JOB_PRAMS, "测试repeat");
//        quartzManager.execRepeatForever(JobEnum.TEST_REPEAT, HelloJob.class, 1000, pram);
    }
}