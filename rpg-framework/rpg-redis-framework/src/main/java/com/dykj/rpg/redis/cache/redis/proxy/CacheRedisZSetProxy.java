package com.dykj.rpg.redis.cache.redis.proxy;

import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.Set;

/**
 * @author jyb
 * @date 2018/12/15 11:49
 */
public class CacheRedisZSetProxy extends AbstractRedisProxy {

    /**
     * 系统第一名从第 0 开始
     */
    private final static int SYSTEM_FIRST_INDEX = 0;

    /**
     * 业务逻辑，第一名从第几开始，可设置
     */
    private int logicFirstIndex;

    public CacheRedisZSetProxy(Class clazz, ICacheSerialize serialize, int logicFirstIndex) {
        super(clazz, serialize);
        this.logicFirstIndex = logicFirstIndex;
    }

    public void add(BoundZSetOperations operations, Object key, double score) {
        operations.add(key2String(key), score);
    }

    public long count(BoundZSetOperations operations, double min, double max) {
        return operations.count(min, max);
    }

    public double incrementScore(BoundZSetOperations operations, Object key, double delta) {
        return operations.incrementScore(key2String(key), delta);
    }

    public String range(BoundZSetOperations operations, long rank) {
        rank = adjustRankParam(rank);
        Set<String> set = range(operations, rank, rank);
        if (null != set && 0 < set.size()) {
            return set.iterator().next();
        }
        return null;
    }

    public Set<String> range(BoundZSetOperations operations, long start, long end) {
        start = adjustRankParam(start);
        end = adjustRankParam(end);
        return operations.range(start, end);
    }

    public Set<String> all(BoundZSetOperations operations) {
        return operations.range(SYSTEM_FIRST_INDEX, -1);
    }

    public String reverseRange(BoundZSetOperations operations, long rank) {
        rank = adjustRankParam(rank);
        Set<String> set = reverseRange(operations, rank, rank);
        if (null != set && 0 < set.size()) {
            return set.iterator().next();
        }
        return null;
    }

    public Set<String> reverseRange(BoundZSetOperations operations, long start, long end) {
        start = adjustRankParam(start);
        end = adjustRankParam(end);
        return operations.reverseRange(start, end);
    }

    public Set<String> rangeByScore(BoundZSetOperations operations, double min, double max) {
        return operations.rangeByScore(min, max);
    }

    public Set<String> reverseRangeByScore(BoundZSetOperations operations, double min, double max) {
        return operations.reverseRangeByScore(min, max);
    }

    public long rank(BoundZSetOperations operations, Object key) {
        Long rank = operations.rank(key2String(key));
        if (null != rank) {
            return adjustRankResult(rank);
        }
        return -1;
    }

    public long reverseRank(BoundZSetOperations operations, Object key) {
        Long rank = operations.reverseRank(key2String(key));
        if (null != rank) {
            return adjustRankResult(rank);
        }
        return -1;
    }

    public long remove(BoundZSetOperations operations, Object... vs) {
        if (null != vs && 0 < vs.length) {
            if (vs[0].getClass() != String.class) {
                String[] ss = new String[vs.length];
                for (int i=0; i<vs.length; i++) {
                    ss[i] = key2String(vs[i]);
                }
                return operations.remove(ss);
            }
            return operations.remove(vs);
        }
        return 0;
    }

    public void removeRange(BoundZSetOperations operations, long start, long end) {
        start = adjustRankParam(start);
        end = adjustRankParam(end);
        operations.removeRange(start, end);
    }

    public void removeRangeByScore(BoundZSetOperations operations, double min, double max) {
        operations.removeRangeByScore(min, max);
    }

    public double score(BoundZSetOperations operations, Object key) {
        Double s = operations.score(key2String(key));
        if (null == s) {
            return 0;
        }
        return s;
    }

    public long size(BoundZSetOperations operations) {
        return operations.size();
    }

    /**
     * 调整排名参数
     * @param rank
     * @return
     */
    private long adjustRankParam(long rank) {
        if (SYSTEM_FIRST_INDEX == logicFirstIndex) {
            return rank;
        }
        return rank - (logicFirstIndex - SYSTEM_FIRST_INDEX);
    }

    /**
     * 调整返回排名
     * @param rank
     * @return
     */
    private long adjustRankResult(long rank) {
        if (SYSTEM_FIRST_INDEX == logicFirstIndex) {
            return rank;
        }
        return rank + (logicFirstIndex - SYSTEM_FIRST_INDEX);
    }
}
