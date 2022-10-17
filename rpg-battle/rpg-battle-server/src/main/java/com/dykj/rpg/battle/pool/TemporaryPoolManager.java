package com.dykj.rpg.battle.pool;

public class TemporaryPoolManager {

    private static ThreadLocal<TemporaryPositionPool> threadLocal = new ThreadLocal<>();

    private static boolean usePool = true;

    public static float[] getPositionArray(){
        if(usePool){
            TemporaryPositionPool positionPool = threadLocal.get();
            if(positionPool == null){
                positionPool = new TemporaryPositionPool();
                threadLocal.set(positionPool);
            }
            return positionPool.getPositionFloat();
        }else{
            return new float[]{0,0,0};
        }

    }

    public static float[] getPositionArray(float x,float y,float z){
        if(usePool){
            TemporaryPositionPool positionPool = threadLocal.get();
            if(positionPool == null){
                positionPool = new TemporaryPositionPool();
                threadLocal.set(positionPool);
            }
            float[] floats = positionPool.getPositionFloat();
            floats[0] = x;
            floats[1] = y;
            floats[2] = z;
            return floats;
        }else{
            return new float[]{x,y,z};
        }

    }

    public static void clearPositionPool(){

    }

}
