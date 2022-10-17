package com.dykj.rpg.net.protocol;

import java.util.*;

public class ProtocolPool {

    private Map<Integer,Class> classMap;

    private ThreadLocal<Map<Integer, List<Protocol>>> protocolPools;

    private int startIndex;

    private static ProtocolPool instance;

    public static ProtocolPool getInstance() {
        if(instance == null){
            instance = new ProtocolPool();
        }
        return instance;
    }

    public void init(Map<Integer,Class> classMap){
        this.classMap = classMap;
    }

    private ProtocolPool(){
        protocolPools = new ThreadLocal<>();
        startIndex = 0;
    }

    public Protocol borrowProtocol(int code){

        Map<Integer, List<Protocol>> protocolPool = protocolPools.get();

        if(protocolPool == null){
            protocolPool = new HashMap<>();
            protocolPools.set(protocolPool);
        }

        List<Protocol> protocolList = protocolPool.get(code);
        if(protocolList == null){
            protocolList = new ArrayList<>();
            protocolPool.put(code,protocolList);
        }

        int size = protocolList.size();
        int index = 0;
        while(index<size){
            Protocol protocol = protocolList.get(index);
            if(protocol == null){
                protocolList.remove(index);
                size --;
            }else{
                index ++;
                if(!protocol.using){
                    protocol.using = true;
                    //System.out.println("borrowProtocol code = " + protocol.getCode() +"  idx = " + protocol.idx);
                    return protocol;
                }
            }
        }

        try{

            Class clazz = classMap.get(code);
            if(clazz != null){
                Protocol protocol = (Protocol) clazz.newInstance();
                protocol.using = true;
                protocol.idx = createNewIdx();
                protocolList.add(protocol);
                //System.out.println("borrowProtocol code = " + protocol.getCode() +"  idx = " + protocol.idx);
                return protocol;
            }

            return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void restoreProtocol(Protocol _protocol){
        if(_protocol == null){
            return;
        }

        _protocol.release();
        _protocol.using = false;
        //System.out.println("restoreProtocol code = " + protocol.getCode() +"  idx = " + protocol.idx);

    }

    public int createNewIdx(){
        return ++startIndex;
    }

    public void scanProtocolPool(){
//        Set<Integer> set = protocolPool.keySet();
//        if(set.size() > 0){
//            StringBuffer sb = new StringBuffer();
//            sb.append("ProtocolPool[");
//            for(int key : set){
//                int usingCount = 0;
//                int nullCount = 0;
//                List<Protocol> list = protocolPool.get(key);
//                for(Protocol protocol : list){
//                    if(protocol == null){
//                        nullCount ++ ;
//                    }else{
//                        if(protocol.using){
//                            usingCount ++ ;
//                        }
//                    }
//                }
//                sb.append("{code="+key+",size="+list.size()+",using="+usingCount+",null="+nullCount+"},");
//            }
//            sb.append("]");
//            System.out.println(sb.toString());
//        }
    }

}
