[["java:package:com.dykj.rpg.battle.ice"]]  //父包结构
module service{     //包名        具体生成的包为: 父包结构 + 包名

    sequence<byte> Data;

    interface BattleCacheService{ //接口名  不能用Ice作为接口名的开头
        //当registerState不是删除时，data为对应的java对象序列化字节数组
        //当registerState待遇删除标签时，data为对应的dataType+modelId
        //bool updateBattleData(int battleId,byte registerState,Data data);

        bool updateBattleCache(int battleId,Data data);

        bool askBattleCacheState(int battleId,Data data);

        bool removeBattleCache(int battleId);

        Data getBattleCache(int battleId);

    };

};