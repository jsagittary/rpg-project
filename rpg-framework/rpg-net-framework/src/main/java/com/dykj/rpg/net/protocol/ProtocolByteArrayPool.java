package com.dykj.rpg.net.protocol;

/**
 * 协议字节数组内存池[非线程安全]
 */
public class ProtocolByteArrayPool {

    /**
     * 极小内存块[128字节]数量
     */
    private static int TINY_POOL_NUM = 100;
    /**
     * 小内存块[256字节]数量
     */
    private static int SMALL_POOL_NUM = 100;
    /**
     * 中内存块[512字节]数量
     */
    private static int MIDDLE_POOL_NUM = 100;
    /**
     * 大内存块[1024字节]数量
     */
    private static int BIG_POOL_NUM = 100;
    /**
     * 极大内存块[1024*16=16384字节]数量
     */
    private static int LARGE_POOL_NUM = 1024;
    private BitArray[] largeByteArray;
    private int borrowLargeByteArrayIndex = 0;

    public ProtocolByteArrayPool(){
        largeByteArray = new BitArray[LARGE_POOL_NUM];
        for(int i=0;i<LARGE_POOL_NUM;i++){
            largeByteArray[i] = new BitArray(16384);
        }
    }

//    public byte[] borrowLargeByteArray(){
//        for(int i=borrowLargeByteArrayIndex;i<borrowLargeByteArrayIndex+LARGE_POOL_NUM;i++){
//            if(largeByteArrayStates[i%LARGE_POOL_NUM] == 0){
//                largeByteArrayStates[i%LARGE_POOL_NUM] = 1;
//                borrowLargeByteArrayIndex = i%LARGE_POOL_NUM+1;
//                return largeByteArray[i%LARGE_POOL_NUM];
//            }
//        }
//        return null;
//    }

    public BitArray borrowLargeByteArray(){
        BitArray bitArray;
        for(int i=borrowLargeByteArrayIndex;i<borrowLargeByteArrayIndex+LARGE_POOL_NUM;i++){
            bitArray = largeByteArray[i%LARGE_POOL_NUM];
            if(!bitArray.using){
                bitArray.using = true;
                borrowLargeByteArrayIndex = (i+1)%LARGE_POOL_NUM;
                return bitArray;
            }
        }
        return null;
    }

    public void restoreLargeByteArray(BitArray bitArray){
        if(bitArray.using){
            bitArray.release();
        }
    }

}
