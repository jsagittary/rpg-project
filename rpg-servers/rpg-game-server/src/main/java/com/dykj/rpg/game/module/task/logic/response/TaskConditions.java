package com.dykj.rpg.game.module.task.logic.response;

import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 任务完成条件model
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/19
 */
public class TaskConditions
{
    private TaskFinishConditionEnum bigCategory;//任务完成条件-大类
    private Enum subclassCategory;//任务完成条件-子类
    private Object[] param;//参数数组

    public TaskConditions()
    {
    }

    public TaskConditions(TaskFinishConditionEnum bigCategory)
    {
        this.bigCategory = bigCategory;
    }

    public TaskConditions(TaskFinishConditionEnum bigCategory, Enum subclassCategory)
    {
        this.bigCategory = bigCategory;
        this.subclassCategory = subclassCategory;
    }

    public TaskConditions(TaskFinishConditionEnum bigCategory, Enum subclassCategory, Object[] param)
    {
        this.bigCategory = bigCategory;
        this.subclassCategory = subclassCategory;
        this.param = param;
    }

    public TaskFinishConditionEnum getBigCategory()
    {
        return bigCategory;
    }

    public void setBigCategory(TaskFinishConditionEnum bigCategory)
    {
        this.bigCategory = bigCategory;
    }

    public Enum getSubclassCategory()
    {
        return subclassCategory;
    }

    public void setSubclassCategory(Enum subclassCategory)
    {
        this.subclassCategory = subclassCategory;
    }

    public Object[] getParam()
    {
        return param;
    }

    public void setParam(Object[] param)
    {
        this.param = param;
    }

    /**
     * 解析成TaskConditionsModel集合
     * @param prams 参数
     * @return List<TaskConditionsModel>
     */
    public static List<TaskConditions> analyzeConditions(Object... prams)
    {
        List<TaskConditions> list = new ArrayList<>();
        for (Object obj : prams)
        {
            if (obj instanceof TaskConditions)
            {
                list.add((TaskConditions) obj);
            }
            else if (obj instanceof Object[])
            {
                conver((Object[]) obj, list);
            }
            else if (obj instanceof List)
            {
                List<Object> objList = (List<Object>) obj;
                conver(objList.toArray(), list);
            }
        }
        return list;
    }

    private static void conver(Object[] objects, List<TaskConditions> list)
    {
        for (Object o : objects)
        {
            if (o instanceof TaskConditions)
            {
                list.add((TaskConditions) o);
            }
        }
    }

    @Override
    public String toString()
    {
        return "TaskRefreshModel{" + "bigCategory=" + bigCategory + ", subclassCategory=" + subclassCategory + ", param=" + Arrays.toString(param) + '}';
    }
}