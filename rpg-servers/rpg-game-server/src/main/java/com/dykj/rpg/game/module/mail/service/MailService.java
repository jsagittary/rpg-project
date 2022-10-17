package com.dykj.rpg.game.module.mail.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dykj.rpg.common.config.dao.MailBasicDao;
import com.dykj.rpg.common.config.model.MailBasicModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerMailDao;
import com.dykj.rpg.common.data.model.PlayerMailModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerMailCache;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.mail.GetAllMailAwardRq;
import com.dykj.rpg.protocol.mail.GetAllMailAwardRs;
import com.dykj.rpg.protocol.mail.GetMailAwardRq;
import com.dykj.rpg.protocol.mail.GetMailAwardRs;
import com.dykj.rpg.protocol.mail.MailRs;
import com.dykj.rpg.protocol.mail.PlayerMailRs;
import com.dykj.rpg.protocol.mail.ReadMailRq;
import com.dykj.rpg.protocol.mail.ReadMailRs;
import com.dykj.rpg.protocol.mail.RemoveAllMailRq;
import com.dykj.rpg.protocol.mail.RemoveAllMailRs;
import com.dykj.rpg.protocol.mail.RemoveMailRq;
import com.dykj.rpg.protocol.mail.RemoveMailRs;
import com.dykj.rpg.util.StringUtil;

/**
 * @author CaoBing
 * @date 2021年4月19日
 * @Description:
 */
@Service
public class MailService extends GmStrategy {
    @Resource
    private PlayerMailDao playerMailDao;

    @Resource
    private MailBasicDao mailBasicDao;

    @Resource
    private ItemService itemService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String serviceName() {
        return "mail";
    }

    public void add() {
        Collection<MailBasicModel> configs = mailBasicDao.getConfigs();
        for (MailBasicModel config : configs) {
            int mailId = config.getId();

            List<ItemJoinModel> award = null;
            if (config.getAnnex() == 1) {
                award = new ArrayList<>();
                award.add(new ItemJoinModel(71000, 3000, 7));
            }
            if (mailId == 1) {
                addMail(player, 1, award, null);
            } else if (mailId == 2) {
                addMail(player, 2, award, null);
            } else if (mailId == 3) {
                addMail(player, 3, award, null);
            }
        }
    }

    /**
     * 新增邮件
     *
     * @param player
     * @param mailId
     * @param params
     * @return
     */
    public void addMail(Player player, int mailId, List<ItemJoinModel> award, String... params) {
        MailBasicModel mailBasicModel = mailBasicDao.getConfigByKey(mailId);
        if (mailBasicModel == null) {
            logger.error("mail config mailId = {} not exist ", mailId);
            return;
        }

        int titleNumber = 0;
        int contentNumber = 0;
        String title = mailBasicModel.getTitle();
        if (null != title) {
            int left = StringUtil.queryStringOccurrenceNumber(title, "{");
            int right = StringUtil.queryStringOccurrenceNumber(title, "}");
            if (left != right) {
                logger.error("mail config mailId = {} 邮件title动态占位符配置错误", mailId);
                return;
            }
            titleNumber = left;
        }

        String mailContent = mailBasicModel.getMailContent();
        if (null != mailContent) {
            int left = StringUtil.queryStringOccurrenceNumber(mailContent, "{");
            int right = StringUtil.queryStringOccurrenceNumber(mailContent, "}");
            if (left != right) {
                logger.error("mail config mailId = {} 邮件content动态占位符配置错误", mailId);
                return;
            }
            contentNumber = left;
        }

        if (null != params && titleNumber + contentNumber != params.length) {
            logger.error("mailParams configParams != arugParams error {}", mailId);
            return;
        }

        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (playerMailCache == null) {
            playerMailCache = new PlayerMailCache();
            playerMailCache.calculateSequence();
            player.cache().setPlayerMailCache(playerMailCache);
        }

        PlayerMailModel playerMailModel = new PlayerMailModel();
        playerMailModel.setMailId(mailId);
        playerMailModel.setPlayerId(player.getPlayerId());
        playerMailModel.setCreateTime(new Date());

        if (null != params && params.length > 0) {
            List<String> paramList = Arrays.asList(params);
            playerMailModel.setTitleParam(JSON.toJSONString(paramList.subList(0, titleNumber)));
            playerMailModel.setContentParam(JSON.toJSONString(paramList.subList(titleNumber, params.length)));
        }

        playerMailModel.setInstanceId(playerMailCache.generateInstanceId(player.getPlayerId()));
        int annex = mailBasicModel.getAnnex();
        if (annex == 1) {
            playerMailModel.setIsAward(1);
        }

        playerMailCache.getPlayerMailModelMap().put(playerMailModel.getInstanceId(), playerMailModel);
        playerMailDao.queueInsert(playerMailModel);

        if (playerMailModel.getIsAward() == 1 && award.size() > 0) {
            addMailAward(player, playerMailModel, award);
        }

        CmdUtil.sendMsg(player, mailRs(playerMailModel));
    }

    /**
     * 添加附件到邮件
     *
     * @param playerMailModel
     * @param award
     */
    public void addMailAward(Player player, PlayerMailModel playerMailModel, List<ItemJoinModel> award) {
        if (playerMailModel == null || award == null || award.isEmpty()) {
            return;
        }
        playerMailModel.setAwards(JSON.toJSONString(award));
        player.cache().getPlayerMailCache().getPlayerMailModelMap().put(playerMailModel.getInstanceId(), playerMailModel);
        playerMailDao.queueUpdate(playerMailModel);
    }

    public PlayerMailRs playerMailRs(PlayerMailCache playerMailCache) {
        PlayerMailRs playerMailRs = new PlayerMailRs();
        if (null == playerMailCache) {
            return playerMailRs;
        }
        for (PlayerMailModel playerMailModel : playerMailCache.getPlayerMailModelMap().values()) {
            playerMailRs.getMails().add(mailRs(playerMailModel));
        }
        return playerMailRs;
    }

    public MailRs mailRs(PlayerMailModel playerMailModel) {
        MailRs mailRs = new MailRs();
        mailRs.setInstId((playerMailModel.getInstanceId()));
        mailRs.setMailId(playerMailModel.getMailId());
        mailRs.setIsAward(playerMailModel.getIsAward());
        mailRs.setIsRead(playerMailModel.getIsRead());
        mailRs.setIsReceive(playerMailModel.getIsReceive());

        List<String> titleParamLists = playerMailModel.getTitleParamLists();
        if (null != titleParamLists) {
            mailRs.setTitleParam(titleParamLists);
        }
        List<String> contentParamLists = playerMailModel.getContentParamLists();
        if (null != contentParamLists) {
            mailRs.setContentParam(contentParamLists);
        }

        String awards = playerMailModel.getAwards();
        if (null != awards && !awards.equals("")) {
            List<ItemJoinModel> itemJoinModels = JSON.parseArray(awards, ItemJoinModel.class);
            List<ItemRs> itemRs = new ArrayList<>();
            for (ItemJoinModel itemJoinModel : itemJoinModels) {
                itemRs.add(itemJoinModel.covertItemRs());
            }
            mailRs.setItems(itemRs);
        }

        mailRs.setCreateTime(playerMailModel.getCreateTime().getTime());
        return mailRs;
    }

    /**
     * 读取邮件
     *
     * @param player
     * @param readMailRq
     * @return
     */
    public ErrorCodeEnum readMail(Player player, ReadMailRq readMailRq) {
        ReadMailRs readMailRs = new ReadMailRs();
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (null == playerMailCache) {
            logger.error("readMail error ： playerMailCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        Long instId = readMailRq.getInstId();
        PlayerMailModel playerMailModel = playerMailCache.getPlayerMailModelMap().get(instId);
        if (null == playerMailModel) {
            logger.error("readMail error ： playerMailModel not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }
        playerMailModel.setIsRead(1);
        readMailRs.setMail(mailRs(playerMailModel));
        CmdUtil.sendMsg(player, readMailRs);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 移除单个邮件
     *
     * @param player
     * @return
     */
    public ErrorCodeEnum removeMail(Player player, RemoveMailRq removeMailRq) {
        RemoveMailRs removeMailRs = new RemoveMailRs();
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (null == playerMailCache) {
            logger.error("getMailAward error ： playerMailCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        long instId = removeMailRq.getInstId();
        PlayerMailModel playerMailModel = playerMailCache.getPlayerMailModelMap().get(instId);
        if (null == playerMailModel) {
            logger.error("getMailAward error ： playerMailModel not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        playerMailCache.getPlayerMailModelMap().remove(instId);
        playerMailDao.queueDelete(playerMailModel);

        removeMailRs.setMail(mailRs(playerMailModel));
        CmdUtil.sendMsg(player, removeMailRs);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 领取单个邮件附件奖励
     *
     * @param player
     * @param getMailAwardRq
     * @return
     */
    public ErrorCodeEnum getMailAward(Player player, GetMailAwardRq getMailAwardRq) {
        GetMailAwardRs getMailAwardRs = new GetMailAwardRs();
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (null == playerMailCache) {
            logger.error("getMailAward error ： playerMailCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        long instId = getMailAwardRq.getInstId();
        PlayerMailModel playerMailModel = playerMailCache.getPlayerMailModelMap().get(instId);
        if (null == playerMailModel) {
            logger.error("getMailAward error ： playerMailModel not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        int isAward = playerMailModel.getIsAward();
        if (isAward == 0) {
            logger.error("getMailAward error ： isAward = 0 ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        String awards = playerMailModel.getAwards();
        int isReceive = playerMailModel.getIsReceive();
        if (isReceive == 1) {
            logger.error("getMailAward error ： isReceive == 1", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        List<ItemJoinModel> awardList = JSON.parseArray(awards, ItemJoinModel.class);
        for (ItemJoinModel itemJoinModel : awardList) {
            itemService.updateItemPush(player, itemJoinModel, ItemOperateEnum.GET_MAIL_AWARD, ItemPromp.BULLET_FRAME);
        }

        playerMailModel.setIsReceive(1);
        getMailAwardRs.setMail(mailRs(playerMailModel));
        CmdUtil.sendMsg(player, getMailAwardRs);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 领取所有邮件奖励
     *
     * @param player
     * @param getAllMailAwardRq
     * @return
     */
    public ErrorCodeEnum getAllMailAward(Player player, GetAllMailAwardRq getAllMailAwardRq) {
        GetAllMailAwardRs getMailAwardRs = new GetAllMailAwardRs();
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (null == playerMailCache) {
            logger.error("getMailAward error ： playerMailCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        List<MailRs> mails = new ArrayList<>();
        List<ItemJoinModel> awardLists = new ArrayList<>();
        Iterator<PlayerMailModel> iterator = playerMailCache.getPlayerMailModelMap().values().iterator();
        while (iterator.hasNext()) {
            PlayerMailModel playerMailModel = (PlayerMailModel) iterator.next();
            if (null == playerMailModel) {
                continue;
            }

            int isAward = playerMailModel.getIsAward();
            if (isAward == 0) {
                continue;
            }

            String awards = playerMailModel.getAwards();
            int isReceive = playerMailModel.getIsReceive();
            if (isReceive == 1) {
                mails.add(mailRs(playerMailModel));
                continue;
            }

            List<ItemJoinModel> awardList = JSON.parseArray(awards, ItemJoinModel.class);
            awardLists.addAll(awardList);

            playerMailModel.setIsReceive(1);
            playerMailModel.setIsRead(1);
            mails.add(mailRs(playerMailModel));
        }

        itemService.batchUpdateItemPush(player, awardLists, ItemOperateEnum.GET_MAIL_AWARD, ItemPromp.BULLET_FRAME);
        getMailAwardRs.setMails(mails);

        CmdUtil.sendMsg(player, getMailAwardRs);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 移除所有已经读取且领奖邮件
     *
     * @param player
     * @param removeAllMailRq
     * @return
     */
    public ErrorCodeEnum removeAllMail(Player player, RemoveAllMailRq removeAllMailRq) {
        RemoveAllMailRs removeAllMailRs = new RemoveAllMailRs();
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        if (null == playerMailCache) {
            logger.error("getMailAward error ： playerMailCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        List<MailRs> mails = new ArrayList<>();
        Iterator<PlayerMailModel> iterator = playerMailCache.getPlayerMailModelMap().values().iterator();
        while (iterator.hasNext()) {
            PlayerMailModel playerMailModel = (PlayerMailModel) iterator.next();
            if (null == playerMailModel) {
                continue;
            }

            int isRead = playerMailModel.getIsRead();
            int isAward = playerMailModel.getIsAward();
            int isReceive = playerMailModel.getIsReceive();

            // 未读取的邮件不删除
            if (isRead != 1) {
                continue;
            }
            // 有奖励没有领取不删除
            if (isAward == 1 && isReceive == 0) {
                continue;
            }

            playerMailModel.setIsRead(1);
            playerMailModel.setIsReceive(1);

            playerMailCache.getPlayerMailModelMap().remove(playerMailModel.getInstanceId());
            playerMailDao.queueDelete(playerMailModel);
            mails.add(mailRs(playerMailModel));
        }

        removeAllMailRs.setMails(mails);
        CmdUtil.sendMsg(player, removeAllMailRs);
        return ErrorCodeEnum.SUCCESS;
    }
}
