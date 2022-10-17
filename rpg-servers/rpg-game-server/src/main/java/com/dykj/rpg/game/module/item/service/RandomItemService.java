package com.dykj.rpg.game.module.item.service;

import com.dykj.rpg.common.config.dao.LootDropteamDao;
import com.dykj.rpg.common.config.model.LootDropteamModel;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.module.item.consts.RandomConsts;
import com.dykj.rpg.game.module.item.consts.RandomTypeConsts;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.util.random.ItemRandomUtil;
import com.dykj.rpg.util.random.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/22 14:21
 * @Description
 */
@Service
public class RandomItemService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private LootDropteamDao lootDropteamDao;

    /**
     * 随机物品
     *
     * @param drops
     * @return
     */
    public List<ItemJoinModel> randomItem(List<List<Integer>> drops, int character) {
        if (drops == null || drops.size() < 1) {
            return null;
        }
        List<Integer> drop = drops.get(0);
        if (drop.get(0) == RandomConsts.NORMAL_PROBABILITY) {
            return normalProbabilityRandom(drops);
        } else if (drop.get(0) == RandomConsts.GROUP_PROBABILITY) {
            List<Integer> groupProbability = groupProbabilityRandom(drops);
            return groupRandomResult(groupProbability, character);
        }
        return null;

    }

    /**
     * List<List<Integer>> drops
     * <p>
     * 子List<Integer> 必须是 5个参数   0 RandomConsts 枚举 1 物品type 2物品id 3物品数量 4 物品概率
     *
     * @param drops
     * @return
     */
    private List<ItemJoinModel> normalProbabilityRandom(List<List<Integer>> drops) {
        List<ItemJoinModel> itemJoinModels = new ArrayList<>();
        for (List<Integer> drop : drops) {
            if (drop.size() != 5) {
                logger.error("RandomItemService normalProbabilityRandom  pram  error {} ", Arrays.toString(drop.toArray()));
                return itemJoinModels;
            }
            int randomResult = RandomUtil.getRandomNumber(CommonConsts.THOUSAND_VALUE);
            if (randomResult <= drop.get(4)) {
                itemJoinModels.add(new ItemJoinModel(drop.get(2), drop.get(3), drop.get(1)));
            }
        }
        return itemJoinModels;
    }

    /**
     * List<List<Integer>> drops
     * <p>
     * 子List<Integer> 必须是 3个参数   0 RandomConsts 枚举 ; 1 掉落组id   2 掉落组概率
     *
     * @param drops
     * @return List<Integer> 掉落组的id
     */

    private List<Integer> groupProbabilityRandom(List<List<Integer>> drops) {
        List<Integer> groups = new ArrayList<>();
        for (List<Integer> drop : drops) {
            if (drop.size() != 3) {
                logger.error("RandomItemService normalProbabilityRandom  pram  error {} ", Arrays.toString(drop.toArray()));
                return groups;
            }
            int randomResult = RandomUtil.getRandomNumber(CommonConsts.THOUSAND_VALUE);
            if (randomResult <= drop.get(2)) {
                groups.add(drop.get(1));
            }
        }
        return groups;
    }


    private List<ItemJoinModel> groupRandomResult(List<Integer> groups, int dropteam_character) {
        List<ItemJoinModel> result = new ArrayList<>();
        for (int group : groups) {
            LootDropteamModel lootDropteamModel = lootDropteamDao.getLootDropteamModel(group, dropteam_character);
            if (lootDropteamModel == null) {
                logger.error("LootDropteamModel is not exist : group {}  dropteam_character {}   ", group, dropteam_character);
                return null;
            }
            List<ItemJoinModel> randomResult = getRandomItemJoinModel(lootDropteamModel);
            if (randomResult != null && randomResult.size() > 0) {
                addItemJoinModel(result, randomResult);
            }
        }
        return result;
    }

    /**
     * @param lootDropteamModel
     * @return
     */
    private List<ItemJoinModel> getRandomItemJoinModel(LootDropteamModel lootDropteamModel) {
        int randomNum = RandomUtil.randomBetween(lootDropteamModel.getDropteamNumber().get(0), lootDropteamModel.getDropteamNumber().get(1));
        if (randomNum < 1) {
            logger.error("getRandomItemJoinModel  error  randomNum <1  DropteamNumber {}", Arrays.toString(lootDropteamModel.getDropteamNumber().toArray()));
            return null;
        }

        List<ItemJoinModel> itemJoinModels = new ArrayList<>();
        //o type(1物品 ，2 小物件)  1 物品type    2物品id 3数量  4 权重
        for (List<Integer> drop : lootDropteamModel.getDropThing()) {
            if (drop.size() != 5) {
                logger.error("getRandomItemJoinModel drop size !=5  {} ", Arrays.toString(drop.toArray()));
                return null;
            }
            //该方法不支持小物件,战斗服需要支持此功能
            if (drop.get(0) == 2) {
                logger.error("getRandomItemJoinModel type error  {} ", 2);
                return null;
            }
            //Integer itemId, Integer itemNum, Integer itemType, int probability
            ItemJoinModel itemJoinModel = new ItemJoinModel(drop.get(2), drop.get(3), drop.get(1), drop.get(4));
            itemJoinModels.add(itemJoinModel);
        }
        List<ItemJoinModel> result = new ArrayList<>();
        for (int i = 0; i < randomNum; i++) {
            if (itemJoinModels.size() < 1) {
                break;
            }
            ItemJoinModel itemJoinModel = ItemRandomUtil.getBoxItem(itemJoinModels);
            addItemJoinModel(result, itemJoinModel.copy());
            //独立不放回随机，随机到一个条目移除掉
            if (lootDropteamModel.getDropType() == RandomTypeConsts.RANDOM_TWO) {
                itemJoinModels.remove(itemJoinModel);
            }
        }
        return result;
    }

    /**
     * 合并物品
     *
     * @param result
     * @param itemJoinModel
     */
    private void addItemJoinModel(List<ItemJoinModel> result, ItemJoinModel itemJoinModel) {
        boolean flag = false;
        for (ItemJoinModel item : result) {
            if (item.getItemId().equals(itemJoinModel.getItemId()) && item.getItemType().equals(itemJoinModel.getItemType())) {
                item.setItemNum(item.getItemNum() + itemJoinModel.getItemNum());
                flag = true;
                break;
            }
        }
        if (!flag) {
            result.add(itemJoinModel);
        }
    }

    /**
     * 合并所有物品
     *
     * @param result
     * @param itemJoinModels
     */
    public void addItemJoinModel(List<ItemJoinModel> result, List<ItemJoinModel> itemJoinModels) {
        for (ItemJoinModel item : itemJoinModels) {
            addItemJoinModel(result, item);
        }
    }


    /**
     * 合并物品
     *
     * @param result
     * @param itemRs
     */
    private void addItemRsModel(List<ItemJoinModel> result, ItemRs itemRs) {
        ItemJoinModel itemJoinModel = new ItemJoinModel(itemRs);
        addItemJoinModel(result, itemJoinModel);
    }

    /**
     * 合并所有物品
     *
     * @param result
     * @param itemRsList
     */
    public void addItemModel(List<ItemJoinModel> result, List<ItemRs> itemRsList) {
        for (ItemRs item : itemRsList) {
            addItemRsModel(result, item);
        }
    }
}