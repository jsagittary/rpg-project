package com.dykj.rpg.battle.logic;

import java.util.Map;

public class MapTileData {
    //地图块id
    public int id;
    //地图块的序列号，从0开始
    public byte index;
    //地图块出口 1=上， 2=左， 3=下， 4=右
    public byte exit;
    //怪物统计记录
    public Map<Integer,Integer> npcRecordInfoMap;
}
