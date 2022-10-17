package com.dykj.rpg.game.module.quartz.core;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author jyb
 * @date 2021/3/16 13:53
 * @Description
 */
@Component
public class QuartzManager implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    private ApplicationContext appCtx;

    public SchedulerFactoryBean schedulerFactoryBean = null;



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.appCtx == null) {
            this.appCtx = applicationContext;
        }
        schedulerFactoryBean = appCtx.getBean(SchedulerFactoryBean.class);
        //registerJob();
    }


    /**
     * 执行无参数的定时任务
     *
     * @param corn
     * @param jobClass
     * @param <T>
     */
    public <T extends Job> void execCronJob(JobEnum jobEnum, String corn, Class<T> jobClass, Map<String, Object> prams) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail detail = JobBuilder.newJob(jobClass).withIdentity(new JobKey(jobEnum.getName(), jobEnum.getGroup())).build();
            if (prams != null && prams.size() > 0) {
                detail.getJobDataMap().putAll(prams);
            }
            //美每秒触发一次任务
            CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(corn)).build();
            scheduler.scheduleJob(detail, trigger);
        } catch (Exception e) {
            logger.error("InitQuartzJob execJob error ", e);
        }
    }

    public <T extends Job> void execCronJob(JobEnum jobEnum,String corn, Class<T> jobClass) {
        execCronJob(jobEnum,corn, jobClass, null);
    }


    /**
     * 移除任务
     *
     * @param name
     * @param group
     */
    public void deleteJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.deleteJob(new JobKey(name, group));
        } catch (Exception e) {
            logger.error("InitQuartzJob deleteJob error ", e);
        }
    }

    /**
     * 移除任务
     *
     * @param jobEnum
     */
    public void deleteJob(JobEnum jobEnum) {
        deleteJob(jobEnum.getName(),jobEnum.getGroup());
    }

    /**
     * @param jobClass
     * @param date                   第一次执行的时间
     * @param intervalInMilliseconds 间隔多久执行  单位ms
     * @param repeatTime             重复执行多少次 -1 为永久
     * @param <T>
     */
    public <T extends Job> void execRepeatJob(JobEnum jobEnum, Class<T> jobClass, Date date, long intervalInMilliseconds, int repeatTime, Map<String, Object> prams) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail detail = JobBuilder.newJob(jobClass).withIdentity(new JobKey(jobEnum.getName(), jobEnum.getGroup())).build();
            if (prams != null && prams.size() > 0) {
                detail.getJobDataMap().putAll(prams);
            }
            detail.getJobDataMap().putAll(prams);
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
            simpleScheduleBuilder.withIntervalInMilliseconds(intervalInMilliseconds);
            if (repeatTime == -1) {
                simpleScheduleBuilder.repeatForever();
            } else {
                simpleScheduleBuilder.withRepeatCount(repeatTime);
            }
            TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withSchedule(simpleScheduleBuilder);
            if (date != null) {
                triggerBuilder.startAt(date);
            }
            Trigger trigger = triggerBuilder.withSchedule(simpleScheduleBuilder).build();
            scheduler.scheduleJob(detail, trigger);
        } catch (Exception e) {
            logger.error("InitQuartzJob execJob error ", e);
        }
    }

    /**
     * 执行带参数的永久性任务
     *
     * @param jobClass
     * @param intervalInMilliseconds
     * @param prams
     * @param <T>
     */
    public <T extends Job> void execRepeatForever(JobEnum jobEnum, Class<T> jobClass, long intervalInMilliseconds, Map<String, Object> prams) {
        execRepeatJob(jobEnum, jobClass, null, intervalInMilliseconds, -1, prams);
    }

    /**
     * 执行无参数的永久性任务
     *
     * @param jobClass
     * @param intervalInMilliseconds
     * @param <T>
     */
    public <T extends Job> void execRepeatForever(JobEnum jobEnum, Class<T> jobClass, long intervalInMilliseconds) {
        execRepeatJob(jobEnum, jobClass, null, intervalInMilliseconds, -1, null);
    }
}