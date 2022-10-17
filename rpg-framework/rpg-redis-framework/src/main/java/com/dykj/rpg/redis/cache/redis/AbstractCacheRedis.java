package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: jyb
 * Date: 2018-11-26 22:42
 */
public abstract class AbstractCacheRedis<V> extends AbstractCacheRedisLock implements ICacheSerialize<V> {

    protected Class<V> clazz;

    @Resource(name = "redisPoolMap")
    public Map<String, RedisTemplate> redisPoolMap;

    private  final static List<String> keyList = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    public AbstractCacheRedis() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<V>) actualTypeArguments[0];
        } else {
            this.clazz = (Class<V>) genericSuperclass;
        }
    }


//    @PostConstruct
//    public void  verification(){
//        if(keyList.contains(path())){
//            logger.error("AbstractCacheRedis  keyPath {}   is  exist ,clazz {} ：",path(),keyPath());
//            System.exit(-1);
//        }else {
//            logger.info("AbstractCacheRedis  keyPath {} ：",path());
//            keyList.add(path());
//        }
//    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisPoolMap.get(getRedisDataKey());
    }

    protected String getRedisDataKey() {
        return RedisDataSourceType.DEFAULT_DATASOURCE;
    }

    public List<V> string2Entity(Collection<String> ss) {
        if (null == ss) return null;
        return ss.stream().map(s -> decode(s)).collect(Collectors.toList());
    }

    @Override
    public String encode(V v) {
        return JsonUtil.toJsonString(v);
    }

    @Override
    public V decode(String s) {
        if (null == s) return null;
        return JsonUtil.toInstance(s, clazz);
    }

    public String path() {
        return CachePathUtil.cachePath2String(keyPath());
    }

    /**
     * 具体路径
     *
     * @return
     */
    public String keyPath() {
        return clazz.getSimpleName();
    }
}
