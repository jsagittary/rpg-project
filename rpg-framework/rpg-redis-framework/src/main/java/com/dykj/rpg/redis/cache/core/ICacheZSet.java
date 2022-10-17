package com.dykj.rpg.redis.cache.core;

import java.util.Set;

/**
 * 接口和 ICacheZSetPrefix 保护一致
 * @author jyb
 * @date 2018/12/15 11:48
 */
public interface ICacheZSet extends ICacheCollection {
    /**
     * 增加元素
     * @param key 键，如玩家 id
     * @param score 分数
     */
    void add(Object key, double score);

    /**
     * 分数在 min 和 max 之间的元素个数
     * @param min
     * @param max
     * @return
     */
    long count(double min, double max);

    /**
     * 为排行榜中的 v 元素增加分数
     * @param key
     * @param delta
     * @return
     */
    double incrementScore(Object key, double delta);

    /**
     * 得到排名 rank 元素
     * @param rank
     * @return
     */
    String range(long rank);

    /**
     * 得到排名在 start 和 end 之间的元素有序集合
     * @param start 从 0 开始算
     * @param end 如果是 -1，标识最后一个
     * @return
     */
    Set<String> range(long start, long end);

    /**
     * 得到排名 rank 元素
     * @param rank
     * @return
     */
    String reverseRange(long rank);

    Set<String> reverseRange(long start, long end);

    Set<String> all();

    /**
     * 得到分数在 min 和 max 之间的元素有序集合
     * @param min
     * @param max
     * @return
     */
    Set<String> rangeByScore(double min, double max);

    Set<String> reverseRangeByScore(double min, double max);

    /**
     *得到元素排名，第一名为 0
     * @param key
     * @return
     */
    long rank(Object key);

    long reverseRank(Object key);

    void removeRange(long start, long end);

    void removeRangeByScore(double min, double max);

    /**
     * 得到元素的得分
     * @param key
     * @return
     */
    double score(Object key);
}
