package com.dykj.rpg.battle.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;

public class BattleObjectPool<T extends BattleObject> {

    private Logger logger = LoggerFactory.getLogger("BattleObjectPool");

    private int size;

    private int outTime;

    private T[] objArray;

    private int nextUid;

    private int borrowStartIndex;

    public BattleObjectPool(Class<T> clazz, int size , int outTime){
        try {
            this.size = size;
            this.outTime = outTime;

            objArray = (T[])Array.newInstance(clazz,size);
            for(int i=0;i<size;i++){
                objArray[i] = clazz.newInstance();
                objArray[i].setUID(getUid());
                objArray[i].setUsing(false);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public T borrow(){
        int index = 0;
        for(int i=borrowStartIndex;i<size+borrowStartIndex;i++){
            index = i%size;
            if(!objArray[index].isUsing()){
                borrowStartIndex = (index+1)%size;
                objArray[index].setUsing(true);
                objArray[index].borrowTime = System.currentTimeMillis();
                return objArray[index];
            }
        }
        return null;
    }

    public boolean restore(T obj){
        if(obj.isUsing()){
            obj.release();
            obj.setUsing(false);
            obj.restoreTime = System.currentTimeMillis();

            return true;
        }

        return false;

    }

    public void releaseAll(){
        for(int i=0;i<size;i++){
            if(objArray[i].isUsing()){
                objArray[i].release();
                objArray[i].setUsing(false);
                objArray[i].restoreTime = System.currentTimeMillis();
            }
        }

    }

    public int getUnuseObject(){
        int count = 0;
        for(int i=0;i<size;i++){
            if(!objArray[i].isUsing()){
                count ++ ;
            }
        }
        return count;
    }

    public int getUid(){
        return ++nextUid;
    }

    public int getSize(){
        return size;
    }

    public void selfCheck(){
        for(int i=0;i<size;i++){
            if(objArray[i].isUsing()){
                objArray[i].selfCheak();
            }
        }
    }

    public void printOverTimeObject(long curTime){
        for(int i=0;i<size;i++){
            if(objArray[i].isUsing()){
                if((curTime - objArray[i].borrowTime > outTime)){
                    logger.error("battle object over time !!! objArray = "+objArray[i].toString());
                    restore(objArray[i]);
                }
            }
        }
    }
}
