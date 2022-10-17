package com.dykj.rpg.redis.cache.core.prefix;

import java.util.Set;

/**
 * 接口和 ICacheZSet 保护一致
 * @author jyb
 * @date 2018/12/15 15:08
 */
public interface ICacheZSetPrefix extends ICacheCollectionPrefix {

    /**
     * 增加元素
     * @param key 键，如玩家 id
     * @param score 分数
     */
    void add(Object prefixKey, Object key, double score);

    /**
     * 分数在 min 和 max 之间的元素个数
     * @param min
     * @param max
     * @return
     */
    long count(Object prefixKey, double min, double max);

    /**
     * 为排行榜中的 v 元素增加分数
     * @param key
     * @param delta
     * @return
     */
    double incrementScore(Object prefixKey, Object key, double delta);

    /**
     * 得到排名 rank 元素
     * @param rank
     * @return
     */
    String range(Object prefixKey, long rank);

    /**
     * 得到排名在 start 和 end 之间的元素有序集合
     * @param start 从 0 开始算
     * @param end 如果是 -1，标识最后一个
     * @return
     */
    Set<String> range(Object prefixKey, long start, long end);

    /**
     * 得到排名 rank 元素
     * @param rank
     * @return
     */
    String reverseRange(Object prefixKey, long rank);

    Set<String> reverseRange(Object prefixKey, long start, long end);

    Set<String> all(Object prefixKey);

    /**
     * 得到分数在 min 和 max 之间的元素有序集合
     * @param min
     * @param max
     * @return
     */
    Set<String> rangeByScore(Object prefixKey, double min, double max);

    Set<String> reverseRangeByScore(Object prefixKey, double min, double max);

    /**
     *得到元素排名，第一名为 0
     * @param key
     * @return
     */
    long rank(Object prefixKey, Object key);

    long reverseRank(Object prefixKey, Object key);

    void removeRange(Object prefixKey, long start, long end);

    void removeRangeByScore(Object prefixKey, double min, double max);

    /**
     * 得到元素的得分
     * @param key
     * @return
     */
    double score(Object prefixKey, Object key);
}
