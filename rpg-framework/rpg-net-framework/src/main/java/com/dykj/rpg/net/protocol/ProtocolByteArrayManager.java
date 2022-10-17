package com.dykj.rpg.net.protocol;


import java.util.HashMap;
import java.util.Map;

/**
 * 协议字节数组内存池管理器[保证内存池的线程安全]
 */
public class ProtocolByteArrayManager {

    /**
     * 协议字节数组内存池集合，由于协议字节数组内存池是非线程安全的，所以为每个线程分配一个单独的内存池
     */
    ThreadLocal<ProtocolByteArrayPool> byteArrayPools;

    private static ProtocolByteArrayManager instance;

    private ProtocolByteArrayManager(){
        byteArrayPools = new ThreadLocal<>();
    }

    public static ProtocolByteArrayManager getInstance() {
        if(instance == null){
            instance = new ProtocolByteArrayManager();
        }
        return instance;
    }

    public BitArray getProtocolBitArray(){
        ProtocolByteArrayPool byteArrayPool = byteArrayPools.get();
        if(byteArrayPool == null){
            byteArrayPool = new ProtocolByteArrayPool();
            byteArrayPools.set(byteArrayPool);
        }
        return byteArrayPool.borrowLargeByteArray();

    }
}
