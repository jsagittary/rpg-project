package com.dykj.rpg.game.module.quartz.event;

import com.dykj.rpg.common.config.dao.TaskTypeDao;
import com.dykj.rpg.common.config.model.TaskTypeModel;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.quartz.core.JobEnum;
import com.dykj.rpg.game.module.quartz.core.QuartzConsts;
import com.dykj.rpg.game.module.quartz.core.QuartzManager;
import com.dykj.rpg.game.module.task.consts.TaskTypeEnum;
import com.dykj.rpg.game.module.quartz.job.TaskRefreshJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 任务刷新定时器
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/12
 */
@Component
public class TaskRefreshJobEvent extends AbstractEvent
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TaskTypeDao taskTypeDao;

    @Resource
    private QuartzManager quartzManager;

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Collection<TaskTypeModel> typeModelCollection = taskTypeDao.getConfigs();
        if (null != typeModelCollection && !typeModelCollection.isEmpty())
        {
            for (TaskTypeModel taskTypeModel : typeModelCollection)
            {
                Map<String, Object> params = new HashMap<>();
                params.put(QuartzConsts.JOB_TASK_TYPE_BASIS, taskTypeModel);
                JobEnum jobEnum = null;
                //主线任务
                if (taskTypeModel.getTaskTypeId() == TaskTypeEnum.MAIN_LINE.getType())
                {
                    continue;
                }
                //日常
                else if (taskTypeModel.getTaskTypeId() == TaskTypeEnum.DAILY.getType())
                {
                    jobEnum = JobEnum.DAILY_TASK_REFRESH;
                }
                //周常
                else if (taskTypeModel.getTaskTypeId() == TaskTypeEnum.WEEK.getType())
                {
                    jobEnum = JobEnum.WEEK_TASK_REFRESH;
                }
                quartzManager.execCronJob(jobEnum, taskTypeModel.getRefreshTime(), TaskRefreshJob.class, params);
                logger.info("JobEnum:{}, taskType:{}, refreshTime:{}, 定时执行任务刷新!", jobEnum.getName(), taskTypeModel.getTaskTypeId(), taskTypeModel.getRefreshTime());
            }
        }
    }
}