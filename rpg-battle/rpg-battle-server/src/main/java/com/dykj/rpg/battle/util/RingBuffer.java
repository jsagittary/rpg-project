package com.dykj.rpg.battle.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RingBuffer<T> {

    private Object[] buffer;

    private int opacity;
    /**
     *  读下标，当前的头部位置
     */
    volatile long readIndex;

    /**
     * 写下标,下一个写入的位置，尾位置+1
     */
    volatile long writeIndex;

    public RingBuffer(int opacity,Class<T> clazz){
        this.opacity = opacity;
        buffer = new Object[opacity];

        for(int i=0;i<opacity;i++){
            try {
                buffer[i] = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        readIndex = 0;
        writeIndex = 0;
    }

    public int size(){
        if (readIndex >= writeIndex){
            return 0;
        }else{
            return (int)(writeIndex-readIndex);
        }
    }

    /**
     * 添加对象，超过大小限制时会无法加入新数据
     */
    public boolean add(T obj){
        if(writeIndex - readIndex < opacity){
            buffer[(int)(writeIndex%opacity)] = obj;
            writeIndex ++;
            return true;
        }else{
            return false;
        }

    }

    /**
     * 添加对象，超过大小限制时会无法加入新数据
     */
    public T getAddObj(){
        if(writeIndex - readIndex < opacity){
            Object obj = buffer[(int)(writeIndex%opacity)];
            writeIndex ++;
            return (T)obj;
        }else{
            return null;
        }

    }

    /**
     * 添加对象，超过大小限制时会无法加入新数据
     */
    public boolean addAll(List<T> objs){
        int size = objs.size();
        if((writeIndex+size) - readIndex > opacity){
            return false;
        }
        for(int i=0;i<size;i++){
            if(!add(objs.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 查看头对象
     */
    public T peek(){
        if (readIndex >= writeIndex){
            return null;
        }else{
            return (T)buffer[(int)(readIndex%opacity)];
        }
    }

    /**
     * 获取头对象并删除
     */
    public T poll(){
        if(readIndex >= writeIndex){
            return null;
        }else{
            readIndex ++;
            return (T)buffer[(int)((readIndex-1)%opacity)];
        }
    }

    public void clear(){
        readIndex = 0;
        writeIndex = 0;
    }

}
