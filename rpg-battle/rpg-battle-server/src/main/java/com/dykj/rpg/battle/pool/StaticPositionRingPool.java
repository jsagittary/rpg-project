package com.dykj.rpg.battle.pool;

import java.util.List;

/**
 * 静态环形池，用于队列使用
 */
public class StaticPositionRingPool {

    private float[][] buffer;

    private int opacity;
    /**
     *  读下标，当前的头部位置
     */
    volatile long readIndex;

    /**
     * 写下标,下一个写入的位置，尾位置+1
     */
    volatile long writeIndex;

    public StaticPositionRingPool(int opacity){
        this.opacity = opacity;
        buffer = new float[opacity][3];

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
    public boolean add(float[] obj){
        if(writeIndex - readIndex < opacity){
            float[] floats = buffer[(int)(writeIndex%opacity)];
            floats[0] = obj[0];
            floats[1] = obj[1];
            floats[2] = obj[2];
            writeIndex ++;
            return true;
        }else{
            return false;
        }

    }

    public boolean add(float x,float y,float z){
        if(writeIndex - readIndex < opacity){
            float[] floats = buffer[(int)(writeIndex%opacity)];
            floats[0] = x;
            floats[1] = y;
            floats[2] = z;
            writeIndex ++;
            return true;
        }else{
            return false;
        }

    }

    /**
     * 添加对象，超过大小限制时会无法加入新数据
     */
    public boolean addAll(List<float[]> objs){
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
    public float[] peek(){
        if (readIndex >= writeIndex){
            return null;
        }else{
            return buffer[(int)(readIndex%opacity)];
        }
    }

    /**
     * 获取头对象并删除
     */
    public float[] poll(){
        if(readIndex >= writeIndex){
            return null;
        }else{
            readIndex ++;
            return buffer[(int)((readIndex-1)%opacity)];
        }
    }

    public void clear(){
        readIndex = 0;
        writeIndex = 0;
    }

}
