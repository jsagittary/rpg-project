package com.dykj.rpg.game.module.gm.service;

import com.dykj.rpg.common.config.dao.CardButtonDao;
import com.dykj.rpg.common.config.model.CardButtonModel;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.dao.PlayerCardRecordDao;
import com.dykj.rpg.common.data.model.PlayerCardRecordModel;
import com.dykj.rpg.game.module.cache.PlayerCardCache;
import com.dykj.rpg.game.module.card.service.CardService;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.protocol.card.CardRs;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description gm指令卡池实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
@Service
public class CardGmService extends GmStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CardButtonDao cardButtonDao;

    @Resource
    private PlayerCardRecordDao playerCardModel;

    @Resource
    private CardService cardService;

    @Override
    public String serviceName()
    {
        return "card";
    }

    /**
     * 开启/关闭指定多个卡池ID(格式: @card(类名称)_switch(方法名) 卡池id:开关状态(0-关闭,1-开启),卡池id:开关状态(0-关闭,1-开启) (多个以逗号隔开))
     *
     * @param paramsMap 参数map(卡池id:开关状态(0-关闭,1-开启))
     */
    public void switchStatus(Map<String, String> paramsMap)
    {
        List<CardRs> cardList = new ArrayList<>();//协议
        List<PlayerCardRecordModel> updCardModelList = new ArrayList<>();//更新
        PlayerCardCache playerCardCache = player.cache().getPlayerCardCache();
        if (null != playerCardCache)
        {
            for (Map.Entry<String, String> entry : paramsMap.entrySet())
            {
                int cardId = Integer.parseInt(entry.getKey());
                int status = 0;
                try
                {
                    status = Integer.parseInt(entry.getValue());
                    if (status != 0 && status != 1)
                    {
                        logger.error("玩家id:{}, 执行\"开启/关闭指定多个卡池\"GM指令时参数取值范围错误!", player.getPlayerId());
                        return;
                    }
                }
                catch (Exception e)
                {
                    logger.error("玩家id:{}, 执行\"开启/关闭指定多个卡池\"GM指令时参数类型异常!", player.getPlayerId(), e);
                    return;
                }
                Date date = new Date();//当前时间
                Pattern pattern = Pattern.compile(CommonConsts.NUMBER_TYPE);//判断刷新时间是否为无限制
                for (PlayerCardRecordModel playerCardRecordModel : playerCardCache.getAllCardRecordList())
                {
                    if (playerCardRecordModel.getCardId() == cardId)
                    {
                        //设置卡池状态
                        playerCardRecordModel.setCardStatus(status);
                        //组装协议
                        CardRs cardRs = new CardRs();
                        cardRs.setCardId(playerCardRecordModel.getCardId());
                        cardRs.setButtonId(playerCardRecordModel.getButtonId());
                        CardButtonModel cardButtonModel = cardButtonDao.getConfigByKey(playerCardRecordModel.getButtonId());
                        if (null == cardButtonModel || null == cardButtonModel.getRefreshTime())
                        {
                            logger.error("玩家id:{}, 卡池id:{}, 按钮id:{}, 获取对应卡池按钮基础信息为空!", player.getPlayerId(), playerCardRecordModel.getCardId(), playerCardRecordModel.getButtonId());
                            return;
                        }
                        //解析卡池按钮的刷新时间
                        long buttonCutoffTime = 0;
                        try
                        {
                            //如果不是-1则解析corn表达式
                            if (!pattern.matcher(cardButtonModel.getRefreshTime()).matches())
                            {
                                CronExpression expression = new CronExpression(cardButtonModel.getRefreshTime());
                                Date refreshDate = expression.getNextValidTimeAfter(date);//拿到刷新时间
                                buttonCutoffTime = refreshDate.getTime();
                            }
                        }
                        catch (Exception e)
                        {
                            logger.error("玩家id:{}, 按钮id:{}, refreshTime:{}, 配置无效!", player.getPlayerId(), playerCardRecordModel.getButtonId(), cardButtonModel.getRefreshTime(), e);
                            return;
                        }
                        cardRs.setButtonCutoffTime(buttonCutoffTime);//按钮冷却截止时间
                        cardList.add(cardRs);
                        updCardModelList.add(playerCardRecordModel);
                    }
                }
            }
        }
        if (!updCardModelList.isEmpty() && !cardList.isEmpty())
        {
            //更新缓存
            updCardModelList.forEach(e -> {
                playerCardCache.updateCache(e);
                playerCardModel.queueUpdate(e);
            });
            logger.debug("玩家id:{}, GM指令触发开关卡池操作完毕!", player.getPlayerId());
        }
    }
}