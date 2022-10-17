[["java:package:com.dykj.rpg.battle.ice"]]  //父包结构
//BattleCache为ice服务器 BattleServer为ice客户端
module service{

    sequence<int> Array;
    sequence<byte> Data;

    interface Battle2BattleCacheService{ //接口名  不能用Ice作为接口名的开头
        bool pingBattleCache(int serverId);
        //组队成功后战斗服主动推送进入战斗信息
        bool enterToBattleServerSuccess(Array playerIds,Data data);
        //战斗结束后战斗服返回给游戏服战斗信息
        bool battleFinishResult(Data data);
    };

    interface CallbackBattleServer{
        //推送玩家角色信息到具体的战斗服
        bool enterToBattle(Data data);
        //获取战斗服上正在运行的战斗数量
        int getRunningBattleCount();
    };

};