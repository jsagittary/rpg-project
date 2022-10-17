package com.dykj.rpg.db.dao;

import com.dykj.rpg.db.consts.PropertyConstants;
import com.dykj.rpg.db.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: jyb
 * @Date: 2018/3/17 16:34
 * @Description:
 */
public abstract class AbstractConfigDao<K, T extends BaseConfig<K>> implements IConfigDao<K, T>
{
    private final Logger logger = LoggerFactory.getLogger("game");

    @Autowired
    private JdbcTemplate configJdbcTemplate;

    /**
     * T.class
     */
    private Class<T> clazz;

    /**
     * 源数据Map
     */
    private final Map<K, T> configs = new ConcurrentHashMap<>();

    /**
     * 初始化T.class
     */
    private void initClazz()
    {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[1];
        }
        else
        {
            this.clazz = (Class<T>) genericSuperclass;
        }
    }

    @Override
    public T getConfigByKey(K key)
    {
        return configs.get(key);
    }

    @Override
    public Collection<T> getConfigs()
    {
        return configs.values();
    }

    @Override
    public void clear()
    {
        configs.clear();
    }

    @Override
    public void load()
    {
        initClazz();
        List<T> values = configJdbcTemplate.query(SqlUtil.getQueryAllSql(clazz), rowMapper());
        for (T t : values)
        {
            configs.put(t.getKey(), t);
        }
        logger.info("加载{}表数据完毕, 数据总数为: {} !", clazz.getSimpleName(), values.size());
    }

    @Override
    public RowMapper<T> rowMapper()
    {
        RowMapper<T> rowMapper = (resultSet, i) -> {
            String regStart = PropertyConstants.BACKSLASH.concat(PropertyConstants.BRACKET_START);//首字符正则
            MessageFormat messageFormat = new MessageFormat(PropertyConstants.MATCH_LAST_CHARACTER);
            String regEnd = messageFormat.format(new Object[]{PropertyConstants.BRACKET_END});//尾字符正则

            T bean = null;
            try
            {
                bean = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields)
                {
                    StringBuilder fieldNameBuil = new StringBuilder();
                    for (int j = 0; j < field.getName().length(); j++)
                    {
                        String str = String.valueOf(field.getName().charAt(j));
                        if (str.equals(str.toUpperCase()))
                        {
                            fieldNameBuil.append(PropertyConstants.UNDERSCORE).append(str.toLowerCase());
                            continue;
                        }
                        fieldNameBuil.append(str);
                    }
                    Object value = resultSet.getObject(fieldNameBuil.toString());
                    logger.debug("根据 {} 属性获取 {} 字段对应值: {}", field.getName(), fieldNameBuil.toString(), value);
                    if (null != value)
                    {
                        Type genericType = field.getGenericType();
                        //判断为list后根据字段类型对数据格式解析后进行填充
                        if (genericType instanceof ParameterizedType)
                        {
                            Stream<String> valueList = Arrays.stream(String.valueOf(value).replaceFirst(regStart, "").replaceFirst(regEnd, "").split(PropertyConstants.COMMA));
                            ParameterizedType parameterizedType = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                            //判断是否为List<List>
                            if (actualTypeArguments[0].getTypeName().contains(List.class.getSimpleName()))
                            {
                                List finalList = new ArrayList();
                                valueList.forEach(e -> {
                                    List valList = new ArrayList();
                                    Arrays.stream(e.split(PropertyConstants.COLON)).forEach(k -> {
                                        if (actualTypeArguments[0].getTypeName().contains(Integer.class.getSimpleName()))
                                        {
                                            valList.add(Integer.parseInt(k));
                                        }
                                        else if (actualTypeArguments[0].getTypeName().contains(Float.class.getSimpleName()))
                                        {
                                            valList.add(Float.parseFloat(k));
                                        }
                                        else
                                        {
                                            valList.add(k);
                                        }
                                    });
                                    finalList.add(valList);
                                });
                                value = finalList;
                            }
                            //判断是否为Map
                            else if (genericType.getTypeName().contains(Map.class.getSimpleName()))
                            {
                                Map resultMap = new HashMap();
                                valueList.forEach(e -> {
                                    //根据map的key和value转换为具体类型
                                    Object mapKey = null;//处理key的值类型
                                    String[] arr = e.split(PropertyConstants.COLON);
                                    if (actualTypeArguments[0].getTypeName().contains(Integer.class.getSimpleName()))
                                        mapKey = Integer.parseInt(arr[0]);
                                    else if (actualTypeArguments[0].getTypeName().contains(Float.class.getSimpleName()))
                                    {
                                        mapKey = Float.parseFloat(arr[0]);
                                    }
                                    else
                                        mapKey = arr[0];

                                    Object mapValue = null;//处理value的值类型
                                    if (actualTypeArguments[1].getTypeName().contains(Integer.class.getSimpleName()))
                                        mapValue = Integer.parseInt(arr[1]);
                                    else if (actualTypeArguments[1].getTypeName().contains(Float.class.getSimpleName()))
                                    {
                                        mapKey = Float.parseFloat(arr[1]);
                                    }
                                    else
                                        mapValue = arr[1];

                                    resultMap.put(mapKey, mapValue);//添加进map
                                });
                                value = resultMap;
                            }
                            else if (actualTypeArguments[0].getTypeName().contains(Integer.class.getSimpleName()))
                                value = valueList.map(Integer::parseInt).collect(Collectors.toList());
                            else if (actualTypeArguments[0].getTypeName().contains(Float.class.getSimpleName()))
                                value = valueList.map(Float::parseFloat).collect(Collectors.toList());
                            else
                                value = valueList.collect(Collectors.toList());
                        }
                        else if (genericType.getTypeName().contains(Float.class.getSimpleName()))
                        {
                            value = Float.parseFloat(String.valueOf(value));
                        }

                        String methodName = "set".concat(field.getName().substring(0, 1).toUpperCase().concat(field.getName().substring(1)));
                        Method method = clazz.getDeclaredMethod(methodName, field.getType());
                        logger.debug("根据解析处理后的值: {} 方法名: {} 参数类型: {} 执行set操作!", value, methodName, field.getType());
                        method.invoke(bean, value);
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("映射到model对象: {} 具体属性值异常!", clazz.getSimpleName(), e);
                System.exit(-1);
            }
            return bean;
        };
        return rowMapper;
    }
}
