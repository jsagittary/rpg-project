package com.dykj.rpg.battle.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时坐标[x,y,z]数组对象池，默认生命周期3秒
 * 非线程安全，需要给每个线程分别分配一个对象池
 */
public class TemporaryPositionPool {

    private Logger logger = LoggerFactory.getLogger("TemporaryPositionPool");

    /**
     * 单个对象组容量 2的10次方
     */
    private int singleSize = 1024;

    /**
     * 单个对象生命周期（毫秒）默认3秒
     */
    private int liveTime = 3000;

    /**
     * 对象组总容量
     */
    private int capacity = 0;
    /**
     * 对象组数量
     */
    private int listSize = 0;


    /**
     * 对象组集合
     */
    private List<PositionArrayAndTime[]> floatArrayList;

    /**
     * 下次借取的对象起点位置
     */
    private int floatArrayIndex;

    /**
     * 循环起点位置
     */
    private int tempArrayIndex;

    public TemporaryPositionPool(){
        floatArrayList = new ArrayList<>();
        expandCapacity();
    }

    public float[] getPositionFloat(){
        long now = System.currentTimeMillis();

        int index = (floatArrayIndex >> 10)%listSize;
        PositionArrayAndTime result = floatArrayList.get(index)[floatArrayIndex & 0x3ff];

        if(now - result.usingTime < liveTime){
            expandCapacity();
            tempArrayIndex = floatArrayIndex;
            floatArrayIndex = (listSize-1)*singleSize;
            result = floatArrayList.get(floatArrayList.size()-1)[0];
        }
        result.usingTime = now;

        floatArrayIndex ++;
        if(floatArrayIndex == capacity){
            floatArrayIndex = tempArrayIndex;
            tempArrayIndex = 0;
        }

        return result.floats;
    }

    /**
     * 扩大容量
     */
    private void expandCapacity(){
        PositionArrayAndTime[] array = new PositionArrayAndTime[singleSize];

        for(int i=0;i<singleSize;i++){
            array[i] = new PositionArrayAndTime();
        }

        floatArrayList.add(array);

        capacity += singleSize;
        listSize += 1;

        logger.info("TemporaryPositionPool expand capacity !!! capacity = "+capacity);

    }

    class PositionArrayAndTime{
        float[] floats = new float[]{0,0,0};
        long usingTime = 0;
    }
}
