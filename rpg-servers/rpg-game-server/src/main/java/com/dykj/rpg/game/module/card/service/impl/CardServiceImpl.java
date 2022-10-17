package com.dykj.rpg.game.module.card.service.impl;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.dykj.rpg.game.module.cache.PlayerCardCache;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dykj.rpg.common.config.dao.CardBasicDao;
import com.dykj.rpg.common.config.dao.CardButtonDao;
import com.dykj.rpg.common.config.dao.CardConditionDao;
import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.CardBasicModel;
import com.dykj.rpg.common.config.model.CardButtonModel;
import com.dykj.rpg.common.config.model.CardConditionModel;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerCardRecordDao;
import com.dykj.rpg.common.data.dao.PlayerCardResultDao;
import com.dykj.rpg.common.data.model.PlayerCardRecordModel;
import com.dykj.rpg.common.data.model.PlayerCardResultModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.card.consts.CardConditionParentEnum;
import com.dykj.rpg.game.module.card.consts.CardConditionTypeEnum;
import com.dykj.rpg.game.module.card.consts.CardConditionsEnum;
import com.dykj.rpg.game.module.card.consts.CardPoolEnum;
import com.dykj.rpg.game.module.card.service.CardService;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.RandomItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.card.CardListRq;
import com.dykj.rpg.protocol.card.CardListRs;
import com.dykj.rpg.protocol.card.CardRq;
import com.dykj.rpg.protocol.card.CardRs;
import com.dykj.rpg.protocol.card.UpdateCardRs;
import com.dykj.rpg.util.date.DateUtils;

/**
 * @Description 抽卡系统接口实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/29
 */
@Service
public class CardServiceImpl implements CardService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CardBasicDao cardBasicDao;

    @Resource
    private CardButtonDao cardButtonDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemService itemService;

    @Resource
    private RandomItemService randomItemService;

    @Resource
    private CardConditionDao cardConditionDao;

    @Resource
    private PlayerCardRecordDao playerCardRecordDao;//抽卡记录

    @Resource
    private PlayerCardResultDao playerCardResultDao;//抽卡结果

    /**
     * 获取玩家所有抽卡记录缓存
     * @param player 玩家信息
     * @return 玩家抽卡记录缓存列表
     */
    @Override
    public PlayerCardCache getCardCache(Player player)
    {
        PlayerCardCache playerCardCache = player.cache().getPlayerCardCache();
        if (null == playerCardCache)
        {
            playerCardCache = new PlayerCardCache();
            List<PlayerCardRecordModel> cardModels = playerCardRecordDao.queryForList(player.getPlayerId());
            //初始化抽卡记录
            if (null != cardModels && !cardModels.isEmpty())
            {
                for (PlayerCardRecordModel playerCardRecordModel : cardModels)
                {
                    playerCardCache.updateCache(playerCardRecordModel);
                }
            }
            //初始化抽卡结果
            List<PlayerCardResultModel> cardResultModels = playerCardResultDao.queryForList(player.getPlayerId());
            if (null != cardResultModels && !cardResultModels.isEmpty())
            {
                for (PlayerCardResultModel playerCardResultModel : cardResultModels)
                {
                    playerCardCache.updateCache(playerCardResultModel);
                }
            }
            player.cache().setPlayerCardCache(playerCardCache);
        }
        return playerCardCache;
    }

    /**
     * 打开抽卡界面加载数据
     * @param player 玩家信息
     * @param cardListRq 协议
     */
    @Override
    public void loadCardData(Player player, CardListRq cardListRq)
    {
        CardListRs cardListRs = new CardListRs();
        PlayerCardCache playerCardCache = this.getCardCache(player);
        if (null != playerCardCache)
        {
            Date date = new Date();//当前时间
            Pattern pattern = Pattern.compile(CommonConsts.NUMBER_TYPE);//判断刷新时间是否为无限制
            List<CardRs> cardList = new ArrayList<>();
            for (PlayerCardRecordModel playerCardRecordModel : playerCardCache.getAllCardRecordList())
            {
                //如果当前卡池为关闭状态则跳过
                if (playerCardRecordModel.getCardStatus() == 0)
                {
                    continue;
                }
                CardRs cardRs = new CardRs();
                cardRs.setCardId(playerCardRecordModel.getCardId());
                cardRs.setButtonId(playerCardRecordModel.getButtonId());
                cardRs.setButtonExtractNumber(playerCardRecordModel.getButtonExtractNumber());
                //计算当前卡池内抽卡按钮的冷却时间
                CardButtonModel cardButtonModel = cardButtonDao.getConfigByKey(playerCardRecordModel.getButtonId());
                if (null == cardButtonModel)
                {
                    logger.error("玩家id:{}, 按钮id:{}, 获取对应卡池按钮配置信息为空!", player.getPlayerId(), playerCardRecordModel.getButtonId());
                    CmdUtil.sendErrorMsg(player.getSession(), cardListRs.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                    return;
                }
                //解析卡池按钮的刷新时间
                long buttonCutoffTime = 0;//按钮冷却截止时间
                try
                {
                    //如果不是-1则解析corn表达式
                    if (!pattern.matcher(cardButtonModel.getRefreshTime()).matches())
                    {
                        CronExpression expression = new CronExpression(cardButtonModel.getRefreshTime());
                        LocalDateTime lastExtractLocalTime = DateUtils.conversionLocalDateTime(playerCardRecordModel.getButtonLastExtractTime());//卡池按钮最后一次抽取时间
                        LocalDateTime refreshLocalDate = DateUtils.conversionLocalDateTime(expression.getNextValidTimeAfter(playerCardRecordModel.getButtonLastExtractTime()));//拿到刷新时间
                        //计算上一次按钮抽取时间和下一次刷新时间的时间差
                        Duration buttonLastduration = Duration.between(lastExtractLocalTime, refreshLocalDate);
                        //计算最后一次按钮抽取时间与当前时间的时间差
                        Duration currentDuration = Duration.between(lastExtractLocalTime, DateUtils.conversionLocalDateTime(date));
                        //如果最后一次按钮抽取时间与当前时间的秒数差大于等于上一次按钮抽取时间和下一次刷新时间的秒数差则表示按钮已冷却
                        if (currentDuration.getSeconds() >= buttonLastduration.getSeconds())
                        {
                            buttonCutoffTime = 0;//表示已经冷却
                        }
                        else
                        {
                            Date refreshDate = expression.getNextValidTimeAfter(playerCardRecordModel.getButtonLastExtractTime());//拿到刷新时间
                            buttonCutoffTime = refreshDate.getTime();//未冷却的按钮截止时间
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error("玩家id:{}, 按钮id:{}, refreshTime:{}, 配置无效!", player.getPlayerId(), playerCardRecordModel.getButtonId(), cardButtonModel.getRefreshTime(), e);
                    CmdUtil.sendErrorMsg(player.getSession(), cardListRs.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                    return;
                }
                cardRs.setButtonCutoffTime(buttonCutoffTime);//按钮冷却截止时间
                cardList.add(cardRs);
            }
            cardListRs.setCardList(cardList);
            logger.debug("初始化玩家:{} 抽卡记录列表数量:{}", player.getPlayerId(), cardList.size());
        }
        CmdUtil.sendMsg(player, cardListRs);
    }

    /**
     * 实现抽卡逻辑
     *
     * @param player 玩家信息
     * @param cardRq 请求协议
     */
    @Override
    public void cardLogic(Player player, CardRq cardRq)
    {
        if (null == cardRq || cardRq.getButtonId() == 0 || cardRq.getCardId() == 0)
        {
            logger.error("玩家id:{}, 协议号:{}, 协议为空!", player.getPlayerId(), cardRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        //玩家账号信息
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();
        if (null == playerInfoModel)
        {
            logger.error("玩家id:{}, 协议号:{}, 获取玩家基本信息为空!", player.getPlayerId(), cardRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        CardBasicModel cardBasicModel = cardBasicDao.getConfigByKey(cardRq.getCardId());
        if (null == cardBasicModel)
        {
            logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 获取卡池基础信息为空!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        List<List<Integer>> conditions = cardBasicModel.getCardConditions();
        if (null == conditions || conditions.isEmpty())
        {
            logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 获取卡池开放条件为空!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        List<Integer> cardButtonIdList = cardBasicModel.getCardButtonId();//关联按钮
        if (null == cardButtonIdList || cardButtonIdList.isEmpty())
        {
            logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 获取卡池关联按钮id集合为空!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        if (cardButtonIdList.stream().noneMatch(e -> e == cardRq.getButtonId()))
        {
            logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 卡池按钮id:{}, 卡池基础表对应按钮组中未匹配到当前按钮id!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId(), cardRq.getButtonId());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        CardButtonModel cardButtonModel = cardButtonDao.getConfigByKey(cardRq.getButtonId());
        if (null == cardButtonModel || null == cardButtonModel.getButtonConsume() || null == cardButtonModel.getRefreshTime() || null == cardButtonModel.getTimes() || null == cardButtonModel.getDroplist())
        {
            logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 按钮id:{}, 获取对应卡池按钮基础信息为空!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId(), cardRq.getButtonId());
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }

        Date date = new Date();//当前时间
        //获取当前玩家抽卡缓存
        PlayerCardCache playerCardCache = this.getCardCache(player);
        //根据卡池id查询当前卡池下所有抽卡记录缓存
        List<PlayerCardRecordModel> cardList = playerCardCache.getCardRecordList(cardRq.getCardId());
        //判断是不是第一次抽卡针对玩家下卡池所有记录
        boolean firstCardFlag = false;
        if (null == cardList || cardList.isEmpty())
        {
            firstCardFlag = true;
        }
        //判断是不是第一次抽卡针对玩家卡池下指定按钮记录
        boolean firstCardRecordFlag = false;
        //根据卡池id、卡池按钮id获取指定抽卡记录信息
        PlayerCardRecordModel playerCardRecordModel = playerCardCache.getCardRecord(cardRq.getCardId(), cardRq.getButtonId());//玩家对应抽卡记录
        if (null == playerCardRecordModel)
        {
            playerCardRecordModel = new PlayerCardRecordModel();
            playerCardRecordModel.setPlayerId(player.getPlayerId());
            playerCardRecordModel.setCardId(cardBasicModel.getCardId());
            playerCardRecordModel.setCardStatus(1);
            playerCardRecordModel.setButtonId(cardButtonModel.getCardButtonId());
            //更新按钮最后一次抽取时间
            playerCardRecordModel.setButtonLastExtractTime(date);
            firstCardRecordFlag = true;
        }
        else
        {
            //判断当前卡池是否已经关闭
            if (playerCardRecordModel.getCardStatus() == 0)
            {
                logger.error("玩家id:{}, 协议号:{}, 卡池id:{}, 当前卡池已关闭!", player.getPlayerId(), cardRq.getCode(), cardRq.getCardId());
                CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CURRENT_CARD_CLOSED);
                return;
            }
            //更新按钮最后一次抽取时间
            playerCardRecordModel.setButtonLastExtractTime(date);
        }

        //判断卡池开放条件
        ErrorCodeEnum errorCodeEnum = this.judgmentOpenCondition(player, firstCardFlag, cardList, conditions, playerInfoModel, cardRq.getCardId());
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
        {
            CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), errorCodeEnum);
            return;
        }

        int buttonExtractNumber = 0;//按钮抽取次数
        List<ItemJoinModel> itemJoinModels = new ArrayList<>();//存储根据掉落组随机出来的物品
        //获取抽卡条件配置表中条件类型为"抽卡次数", 最大限制数
        int cardCount = 0;
        for (CardConditionModel cardConditionModel : cardConditionDao.getConfigs())
        {
            for (List<Integer> conditionTypeList : cardConditionModel.getConditionType())
            {
                //如果当前类型为抽卡次数
                if (conditionTypeList.get(0) == CardConditionTypeEnum.CARD_NUMBER.getType())
                {
                    cardCount = conditionTypeList.get(1) > cardCount ? conditionTypeList.get(1) : cardCount;
                }
            }
        }
        LinkedList<ItemJoinModel> faultLinkedAllList = new LinkedList<>();;//存储待更新玩家所有抽卡道具结果列表
        if (null != playerCardCache.cardResultValues() && !playerCardCache.cardResultValues().isEmpty())
        {
            for (PlayerCardResultModel playerCardResultModel : playerCardCache.cardResultValues())
            {
                faultLinkedAllList.addAll(this.analyzeCardResult(playerCardResultModel));
            }
        }
        LinkedList<ItemJoinModel> faultLinkedList = new LinkedList<>();;//存储待更新玩家当前卡池下抽卡道具结果列表
        PlayerCardResultModel playerCardResultModel = playerCardCache.getCardResult((long) cardRq.getCardId());
        if (null != playerCardResultModel)
        {
            faultLinkedList.addAll(this.analyzeCardResult(playerCardResultModel));
        }

        //遍历抽空次数进行抽卡操作
        for (int cardNum = 0; cardNum < cardButtonModel.getTimes(); cardNum++)
        {
            //记录抽卡次数
            buttonExtractNumber++;
            //掉落组
            List<List<Integer>> resultList = new ArrayList<>();
            //判断修正条件和修正结果不为空且抽卡结果不为空则进行条件判定
            if ((!faultLinkedAllList.isEmpty() && !faultLinkedList.isEmpty())
                    && (null != cardButtonModel.getCorrectConditions() && null != cardButtonModel.getCorrectResult()))
            {
                ErrorCodeEnum error = this.conditionDeter(player, cardButtonModel, faultLinkedAllList, faultLinkedList, resultList);
                if (!error.equals(ErrorCodeEnum.SUCCESS))
                {
                    CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), error);
                    return;
                }
            }
            else
            {
                resultList = cardButtonModel.getDroplist();
            }
            //根据掉落组随机物品
            List<ItemJoinModel> result = randomItemService.randomItem(resultList, playerInfoModel.getProfession());
            if (null == result || result.isEmpty())
            {
                logger.error("玩家id:{}, 协议号:{}, 掉落组:{}, 根据配置的掉落组随机出的物品列表为空!", player.getPlayerId(), cardRq.getCode(), cardButtonModel.getDroplist());
                CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            else
            {
                //根据cardCount限定抽卡结果并更新抽卡结果记录确保每次抽卡结果都与配置的最大抽卡次数相符
                for (ItemJoinModel itemJoinModel : result)
                {
                    ItemJoinModel joinModel = itemJoinModel.copy();
                    //添加抽卡道具结果时每次从头部追加
                    faultLinkedAllList.addFirst(joinModel);
                    //更新玩家所有道具结果集数据
                    this.updResultLogic(cardCount, faultLinkedAllList, joinModel);
                    //添加抽卡道具结果时每次从头部追加
                    faultLinkedList.addFirst(joinModel);
                    //更新玩家当前卡池所有道具结果集数据
                    this.updResultLogic(cardCount, faultLinkedList, joinModel);
                }
                //随机宝箱开出的东西会有重复则需要合并相同道具数量
                randomItemService.addItemJoinModel(itemJoinModels, result);
            }
        }
        logger.debug("玩家id:{}, 卡池id:{}, 按钮id:{}, 抽取出的道具列表:{}", player.getPlayerId(), cardRq.getCardId(), cardRq.getButtonId(), itemJoinModels.toString());

        long buttonCutoffTime = 0;//按钮冷却截止时间
        //判断刷新时间是否为无限制
        Pattern pattern = Pattern.compile(CommonConsts.NUMBER_TYPE);
        //如果不是-1则解析corn表达式
        if (!pattern.matcher(cardButtonModel.getRefreshTime()).matches())
        {
            CronExpression expression = null;
            try
            {
                expression = new CronExpression(cardButtonModel.getRefreshTime());
            }
            catch (ParseException e)
            {
                logger.error("玩家id:{}, 按钮id:{}, 刷新时间配置错误!", player.getPlayerId(), cardRq.getButtonId(), e);
                CmdUtil.sendErrorMsg(player.getSession(), cardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            buttonCutoffTime = expression.getNextValidTimeAfter(playerCardRecordModel.getButtonLastExtractTime()).getTime();//拿到刷新时间
            //如果不是第一次抽卡则判断是否需要消耗道具
            if (!firstCardFlag && !firstCardRecordFlag)
            {
                LocalDateTime lastExtractLocalTime = DateUtils.conversionLocalDateTime(playerCardRecordModel.getButtonLastExtractTime());//卡池按钮最后一次抽取时间
                LocalDateTime refreshLocalDate = DateUtils.conversionLocalDateTime(expression.getNextValidTimeAfter(playerCardRecordModel.getButtonLastExtractTime()));//拿到刷新时间
                //计算上一次按钮抽取时间和下一次刷新时间的时间差
                Duration buttonLastduration = Duration.between(lastExtractLocalTime, refreshLocalDate);
                //计算最后一次按钮抽取时间与当前时间的时间差
                Duration currentDuration = Duration.between(lastExtractLocalTime, DateUtils.conversionLocalDateTime(date));
                //如果最后一次按钮抽取时间与当前时间的秒数差小于上一次按钮抽取时间和下一次刷新时间的秒数差则表示按钮未冷却
                if (currentDuration.getSeconds() < buttonLastduration.getSeconds())
                {
                    //按钮未冷却走道具消耗逻辑
                    this.consumeItemLogic(player, cardRq.getCode(), cardButtonModel, itemJoinModels);
                }
            }
        }
        else
        {
            this.consumeItemLogic(player, cardRq.getCode(), cardButtonModel, itemJoinModels);
        }

        //更新道具
        ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.DRAW_CARD, ItemPromp.GENERIC);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            //按钮抽取次数
            playerCardRecordModel.setButtonExtractNumber(playerCardRecordModel.getButtonExtractNumber() + buttonExtractNumber);
            //更新玩家抽卡记录表
            if (null == playerCardCache.getCardRecord(playerCardRecordModel.getCardId(), playerCardRecordModel.getButtonId()))
            {
                playerCardRecordDao.queueInsert(playerCardRecordModel);
            }
            else
            {
                playerCardRecordDao.queueUpdate(playerCardRecordModel);
            }
            playerCardCache.updateCache(playerCardRecordModel);
            //新增抽卡结果逻辑
            this.insCardResultLogic(faultLinkedAllList, faultLinkedList, playerCardResultModel, player.getPlayerId(), cardRq.getCardId(), playerCardCache);
            //组装协议
            CardRs cardRs = new CardRs();
            cardRs.setCardId(playerCardRecordModel.getCardId());
            cardRs.setButtonId(playerCardRecordModel.getButtonId());
            cardRs.setButtonExtractNumber(playerCardRecordModel.getButtonExtractNumber());
            cardRs.setButtonCutoffTime(buttonCutoffTime);//按钮冷却截止时间
            UpdateCardRs updateCardRs = new UpdateCardRs();
            updateCardRs.setCard(cardRs);
            CmdUtil.sendMsg(player, updateCardRs);
            logger.debug("玩家id:{}, 请求协议:{}, 抽卡操作执行完毕, 返回协议:{}", player.getPlayerId(), cardRq.toString(), updateCardRs.toString());
        }
    }

    /**
     * 判断卡池开放条件
     */
    private ErrorCodeEnum judgmentOpenCondition(Player player, boolean firstCardFlag, List<PlayerCardRecordModel> cardList,
                                                List<List<Integer>> conditions, PlayerInfoModel playerInfoModel, int cardId)
    {
        //卡池内总共抽卡次数
        int extractNumber = firstCardFlag ? 0 : cardList.stream().mapToInt(PlayerCardRecordModel::getButtonExtractNumber).sum();
        List<Boolean> cardTypeArr = new ArrayList<>();//用于判断是否达到上限
        for (List<Integer> list : conditions)
        {
            //职业
            if (list.get(0) == CardConditionsEnum.PROFESSION.getType())
            {
                if (list.get(1) == playerInfoModel.getProfession() || list.get(1) == 0)
                {
                    cardTypeArr.add(true);
                }
                else
                {
                    logger.error("玩家id:{}, 卡池id:{}, 当前职业不符合开放条件!", player.getPlayerId(), cardId);
                    return ErrorCodeEnum.PROFESSION_NOT_CARD_OPEN_CONDITION;
                }
            }
            //抽取次数
            else if (list.get(0) == CardConditionsEnum.NUMBER_DRAWS.getType())
            {
                if (extractNumber >= list.get(1) && list.get(1) > 0)
                {
                    logger.error("玩家id:{}, 卡池id:{}, 该卡池已达到抽取上限!", player.getPlayerId(), cardId);
                    return ErrorCodeEnum.CARD_ACHIEVED_EXTRACT_LIMIT;
                }
                else
                {
                    cardTypeArr.add(true);
                }
            }
        }
        if (cardTypeArr.size() != conditions.size())
        {
            logger.error("玩家id:{}, 卡池id:{}, 不符合卡池开放条件!", player.getPlayerId(), cardId);
            return ErrorCodeEnum.NOT_CARD_OPEN_CONDITIONS;
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 修正条件判定
     */
    private ErrorCodeEnum conditionDeter(Player player, CardButtonModel cardButtonModel, LinkedList<ItemJoinModel> faultLinkedAllList,
                             LinkedList<ItemJoinModel> faultLinkedList, List<List<Integer>> resultList)
    {
        if (cardButtonModel.getCorrectConditions().size() != cardButtonModel.getCorrectResult().size())
        {
            logger.error("玩家id:{}, 修正条件:{}, 修正结果:{}, 数量不匹配!", player.getPlayerId(), cardButtonModel.getCorrectConditions(), cardButtonModel.getCorrectResult());
            return ErrorCodeEnum.CONFIG_ERROR;
        }
        //遍历修正条件
        for (int i = 0; i < cardButtonModel.getCorrectConditions().size(); i++)
        {
            List<Integer> condition_id = cardButtonModel.getCorrectConditions().get(i);
            List<Integer> resultType = new ArrayList<>();//存储修正条件下多条件id是否判断为真
            for (Integer integer : condition_id)
            {
                CardConditionModel cardConditionModel = cardConditionDao.getConfigByKey(integer);
                if (null == cardConditionModel)
                {
                    logger.error("玩家id:{}, 修正条件id:{}, 获取基础修正条件为空!!", player.getPlayerId(), integer);
                    return ErrorCodeEnum.CONFIG_ERROR;
                }
                boolean flag = false;
                //如果是全局卡池
                if (cardConditionModel.getCardPool() == CardPoolEnum.GLOBAL_CARD_POOL.getType())
                {
                    flag = this.conditionDetermination(player, cardConditionModel, faultLinkedAllList);
                }
                //如果是当前卡池
                else if (cardConditionModel.getCardPool() == CardPoolEnum.CURRENT_CARD_POOL.getType())
                {
                    flag = this.conditionDetermination(player, cardConditionModel, faultLinkedList);
                }
                if (flag)
                {
                    resultType.add(integer);
                }
            }
            //如果当前修正条件id集合都满足的情况下进修正结果
            if (resultType.size() == condition_id.size())
            {
                List<Integer> correctResult = cardButtonModel.getCorrectResult().get(i);
                resultList.add(correctResult);
            }
        }
        //resultList为空表示条件判定都没通过则取默认掉落组
        if (resultList.isEmpty())
        {
            resultList.addAll(cardButtonModel.getDroplist());
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 消耗道具逻辑
     * @param player 玩家信息
     * @param protocolNumber  协议号
     * @param cardButtonModel 按钮基础
     * @param itemJoinModels 存储道具集合
     */
    private void consumeItemLogic(Player player, int protocolNumber, CardButtonModel cardButtonModel, List<ItemJoinModel> itemJoinModels)
    {
        //消耗道具组
        List<List<Integer>> buttonConsumeList = cardButtonModel.getButtonConsume();
        for (List<Integer> list : buttonConsumeList)
        {
            ItemJoinModel itemJoinModel = new ItemJoinModel();
            itemJoinModel.setItemType(list.get(0));
            itemJoinModel.setItemId(list.get(1));
            itemJoinModel.setItemNum(-list.get(2));
            itemJoinModels.add(itemJoinModel);

            //如果当前道具类型为货币类型
            ItemModel itemModel = itemDao.getConfigByKey(itemJoinModel.getItemId());//拿到道具基础配置表信息
            if (null == itemModel)
            {
                logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemModel.getItemId());
                CmdUtil.sendErrorMsg(player.getSession(), protocolNumber, ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            //刷新货币类任务进度
            TaskScheduleRefreshUtil.currencySchedule(player, itemModel, itemJoinModel.getItemNum());
        }
    }

    /**
     * 条件判定
     * @param player 玩家信息
     * @param cardConditionModel 按钮修正条件model
     * @param itemJoinModels 抽卡道具结果列表
     * @return true or false
     */
    private boolean conditionDetermination(Player player, CardConditionModel cardConditionModel, List<ItemJoinModel> itemJoinModels)
    {
        //遍历条件类型
        for (List<Integer> conditionTypeList : cardConditionModel.getConditionType())
        {
            //如果当前类型为抽卡次数
            if (conditionTypeList.get(0) == CardConditionTypeEnum.CARD_NUMBER.getType())
            {
                int cardNumber = conditionTypeList.get(1);//抽卡数量
                List<ItemJoinModel> conCardList = new ArrayList<>();//存储对应配置的抽卡道具结果
                //根据配置的抽卡数量取对应历时记录
                if (itemJoinModels.stream().mapToInt(ItemJoinModel::getItemNum).sum() > cardNumber)
                {
                    int count = 0;
                    for (ItemJoinModel itemJoinModel : itemJoinModels)
                    {
                        if (count == cardNumber)
                        {
                            break;
                        }
                        //因数据库存储的抽卡结果次数是累加的则这里做判定条件时需拆分
                        for (int i = 0; i < itemJoinModel.getItemNum(); i++)
                        {
                            ItemJoinModel temItemModel = new ItemJoinModel();
                            temItemModel.setItemId(itemJoinModel.getItemId());
                            temItemModel.setItemNum(1);
                            conCardList.add(temItemModel);
                            count++;
                        }
                    }
                }
                else
                {
                    itemJoinModels.forEach(k ->
                    {
                        for (int i = 0; i < k.getItemNum(); i++)
                        {
                            ItemJoinModel temItemModel = new ItemJoinModel();
                            temItemModel.setItemId(k.getItemId());
                            temItemModel.setItemNum(1);
                            conCardList.add(temItemModel);
                        }
                    });
                }
                //判定大类  如果是稀有度
                if (cardConditionModel.getConditionParentType() == CardConditionParentEnum.RARITY.getType())
                {
                    List<Integer> list = new ArrayList<>();//用于判断是否满足子类判定条件
                    for (ItemJoinModel model : conCardList)
                    {
                        ItemModel itemModel = itemDao.getConfigByKey(model.getItemId());
                        if (null == itemModel)
                        {
                            logger.error("玩家id:{}, 道具id:{}, 获取对应道具基础信息为空!", player.getPlayerId(), model.getItemId());
                            return false;
                        }
                        //遍历子类 判断conCardList每条记录是否满足全部满足则判定通过
                        for (Integer subclassType : cardConditionModel.getConditionSubclass())
                        {
                            if (itemModel.getItemQualityType().equals(subclassType))
                            {
                                list.add(subclassType);
                                break;
                            }
                        }
                    }
                    //如果有符合配置的品质则判定通过
                    if (list.size() == conCardList.size())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更新抽卡结果逻辑
     * @param allList 玩家抽卡所有历史抽卡结果
     * @param list 玩家当前卡池历史抽卡结果
     * @param playerCardResultModel 玩家抽卡记录
     * @param playerId 玩家id
     * @param cardId 卡池id
     * @param playerCardCache 抽卡缓存
     */
    private void insCardResultLogic(LinkedList<ItemJoinModel> allList, LinkedList<ItemJoinModel> list, PlayerCardResultModel playerCardResultModel,
                                    Integer playerId, Integer cardId, PlayerCardCache playerCardCache)
    {
        //过滤相同抽卡结果数据
        List<ItemJoinModel> finalUpdList = new ArrayList<>();
        //如果当前卡池抽卡结果不为空并且所有卡池抽卡记录与当前卡池抽卡记录相匹配则过滤相同数据
        if (null != playerCardResultModel && allList.size() == list.size())
        {
            finalUpdList.addAll(allList);
            //过滤相同数据集
            for (ItemJoinModel model : list)
            {
                if (finalUpdList.stream().anyMatch(e -> e.getItemId().equals(model.getItemId())))
                {
                    continue;
                }
                finalUpdList.add(model);
            }
        }
        else
        {
            //如果当前卡池抽卡结果历史数据为空则finalUpdList添加当前抽卡结果数据
            finalUpdList.addAll(list);
        }
        //合并相同结果集
        List<ItemJoinModel> finalUpdArr = this.mergeItem(finalUpdList);
        //更新抽卡结果
        String cardResult = JSONArray.toJSONString(finalUpdArr.stream().map(e ->
        {
            Map<String, Integer> integerMap = new HashMap<>();
            integerMap.put("itemId", e.getItemId());
            integerMap.put("itemNum", e.getItemNum());
            return integerMap;
        }).collect(Collectors.toList()));
        //异步入库
        if (null == playerCardResultModel)
        {
            playerCardResultModel = new PlayerCardResultModel();
            playerCardResultModel.setPlayerId(playerId);
            playerCardResultModel.setCardId(cardId);
            playerCardResultModel.setCardResult(cardResult);
            playerCardResultDao.queueInsert(playerCardResultModel);
        }
        else
        {
            playerCardResultModel.setCardResult(cardResult);
            playerCardResultDao.queueUpdate(playerCardResultModel);
        }
        playerCardCache.updateCache(playerCardResultModel);
    }

    /**
     * 抽取道具结果列表达到最大抽卡次数阈值逻辑
     * @param cardCount 条件类型: 抽卡次数最大阈值
     * @param sourceList 源抽卡历史道具列表数据
     * @param itemJoinModel 抽取的道具
     */
    private void updResultLogic(int cardCount, LinkedList<ItemJoinModel> sourceList, ItemJoinModel itemJoinModel)
    {
        //如果玩家所有抽卡道具结果集数量大于等于配置项最大抽卡次数阈值则从末尾删除元素
        if (!sourceList.isEmpty() && sourceList.stream().mapToInt(ItemJoinModel::getItemNum).sum() > cardCount)
        {
            //倒序遍历
            ListIterator it = sourceList.listIterator(sourceList.size());
            while (it.hasPrevious())
            {
                ItemJoinModel temModel = (ItemJoinModel) it.previous();
                if (temModel.getItemNum().equals(itemJoinModel.getItemNum()))
                {
                    it.remove();
                }
                else
                {
                    boolean flag = false;
                    for (int i = 0; i < temModel.getItemNum(); i++)
                    {
                        temModel.setItemNum(temModel.getItemNum() - 1);
                        if (temModel.getItemNum() == 0)
                        {
                            it.remove();
                            break;
                        }
                        else
                        {
                            //每次操作后需重新获取最新道具数量如满足最大阈值则退出
                            if (sourceList.stream().mapToInt(ItemJoinModel::getItemNum).sum() <= cardCount)
                            {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag)
                        break;
                }
                //每次操作后需重新获取最新道具数量如满足最大阈值则退出
                if (sourceList.stream().mapToInt(ItemJoinModel::getItemNum).sum() <= cardCount)
                {
                    break;
                }
            }
        }
    }

    /**
     * 解析抽卡结果
     * @param playerCardResultModel 抽卡结果记录
     * @return 抽卡道具结果
     */
    private LinkedList<ItemJoinModel> analyzeCardResult(PlayerCardResultModel playerCardResultModel)
    {
        LinkedList<ItemJoinModel> list = new LinkedList<>();
        JSONArray jsonArray = JSONArray.parseArray(playerCardResultModel.getCardResult());
        for (int i = 0; i < jsonArray.size(); i++)
        {
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.getString(i));
            ItemJoinModel itemJoinModel = new ItemJoinModel();
            itemJoinModel.setItemId(jsonObject.getInteger("itemId"));
            itemJoinModel.setItemNum(jsonObject.getInteger("itemNum"));
            list.add(itemJoinModel);
        }
        return list;
    }

    /**
     * 合并相同结果集
     * @param finalUpdList 源数据
     */
    private List<ItemJoinModel> mergeItem(List<ItemJoinModel> finalUpdList)
    {
        return new ArrayList<>(finalUpdList.stream().collect(Collectors.toMap(ItemJoinModel::getItemId, a -> a, (o1, o2) ->
        {
            o1.setItemNum(o1.getItemNum() + o2.getItemNum());
            return o1;
        })).values());
    }
}