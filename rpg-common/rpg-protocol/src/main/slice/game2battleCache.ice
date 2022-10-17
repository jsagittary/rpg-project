[["java:package:com.dykj.rpg.battle.ice"]]  //父包结构
//BattleCache为ice服务器 GameServer为ice客户端
module service{

    sequence<byte> Data;

    interface Game2BattleCacheService{ //接口名  不能用Ice作为接口名的开头
        bool pingBattleCache(int serverId);
        //请求进入战斗服
        bool enterToBattleCache(int serverId,int playerId,Data data);

    };

    interface CallbackGameServer{
        //进入战斗服返回，此时只是返回进入组队结果
        void enterToBattleCacheResult(Data data);
        //组队成功后战斗服主动推送进入战斗信息
        void enterToBattleServerSuccess(Data data);
        //战斗结束后战斗服返回给游戏服战斗信息
        void battleFinishResult(Data data);
    };

};