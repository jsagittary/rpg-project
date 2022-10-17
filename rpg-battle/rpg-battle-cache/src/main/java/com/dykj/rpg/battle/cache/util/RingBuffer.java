package com.dykj.rpg.battle.cache.util;

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

    public RingBuffer(int opacity){
        this.opacity = opacity;
        buffer = new Object[opacity];
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
     * 添加
     */
    public boolean add(T obj){
        if(writeIndex - readIndex < opacity){
            buffer[(int)(writeIndex%opacity)] = obj;
            writeIndex++;
            return true;
        }else{
            return false;
        }

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
            readIndex++;
            return (T)buffer[(int)((readIndex-1)%opacity)];
        }
    }

    public void clear(){
        readIndex = writeIndex;
    }

}
