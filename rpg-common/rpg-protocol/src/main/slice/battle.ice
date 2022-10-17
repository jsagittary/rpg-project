[["java:package:com.dykj.rpg.battle.ice"]]  //父包结构
module service{     //包名        具体生成的包为: 父包结构 + 包名

    sequence<string> Results;

    interface BattleService{ //接口名  不能用Ice作为接口名的开头

        Results registerToBattleServer(int sessionId,int userId);

        void kickOut(int sessionId);

    };

//    interface CallbackGameServer{
//        //返回值 0=查询失败 -1=掉线 1=在线
//        byte verifyClientState(int sessionId);
//    };


};