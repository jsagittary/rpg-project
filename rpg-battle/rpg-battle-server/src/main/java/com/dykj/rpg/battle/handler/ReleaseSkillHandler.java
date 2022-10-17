package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleErrorMessage;
import com.dykj.rpg.protocol.battle.ReleaseSkillRq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleaseSkillHandler extends AbstractClientHandler<ReleaseSkillRq> {
    Logger logger = LoggerFactory.getLogger("ReleaseSkillHandler");
    @Override
    protected void doHandler(ReleaseSkillRq releaseSkillRq, ISession session) {
        logger.info(releaseSkillRq.toString());
        int playerId = ((UdpSession)session).playerId;
        int frameNum = releaseSkillRq.getFrameNum();
        int skillId = releaseSkillRq.getSkillId();
        if(skillId != 0){
            BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);
            if(battleContainer != null){
                if(!battleContainer.waitHandlerDataManager.addReleaseSkillToWaitMap(playerId,frameNum,skillId,releaseSkillRq.getPosition())){
                    BattleErrorMessage msg = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
                    msg.setErrorCode(1);
                    msg.setHandlerId(2);
                    msg.setErrMsg("技能释放超时！");

                    sendMsg(session,msg);
                }
            }else{
                BattleErrorMessage msg = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
                msg.setErrorCode(1);
                msg.setHandlerId(2);
                msg.setErrMsg("战斗已结束！");

                sendMsg(session,msg);
            }
        }else{
            BattleErrorMessage msg = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
            msg.setErrorCode(1);
            msg.setHandlerId(2);
            msg.setErrMsg("手动释放技能时上传的技能ID不能为0！");

            sendMsg(session,msg);
        }

    }
}
