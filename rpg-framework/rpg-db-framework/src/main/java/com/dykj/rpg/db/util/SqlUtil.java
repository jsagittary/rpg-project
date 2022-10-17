package com.dykj.rpg.db.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.annotation.Table;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: jyb
 * @Date: 2018/3/10 15:20
 * @Description:
 */
public class SqlUtil {

    public static String getInsertSql(Class<?> clazz) {
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer insertSql = new StringBuffer();
        List<String> insertParas = new ArrayList<String>();
        List<String> insertParaNames = new ArrayList<String>();
        insertSql.append("insert into ").append(tableName).append("(");
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Column column = field.getAnnotation(Column.class);
                JSONField jsonField = field.getAnnotation(JSONField.class);
                if(jsonField!=null){
                    continue;
                }
                if (column != null) {
                    if (column.primaryKey() != PrimaryKey.INCREMENT) {
                        if (!column.column().equals("")) {
                            insertParaNames.add(column.column());
                        } else {
                            String name = getString(null, field.getName(), false);
                            insertParaNames.add(name);
                        }
                        insertParas.add(":" + field.getName());
                    }
                } else {
                    insertParaNames.add(getString(null, field.getName(), false));
                    insertParas.add(":" + field.getName());
                }
            }
        } catch (Exception e) {
            new RuntimeException("get insert sql is exception:" + e);
        }
        for (int i = 0; i < insertParaNames.size(); i++) {
            insertSql.append(insertParaNames.get(i));
            if (i != insertParaNames.size() - 1)
                insertSql.append(",");
        }
        insertSql.append(")").append(" values(");
        for (int i = 0; i < insertParas.size(); i++) {
            insertSql.append(insertParas.get(i));
            if (i != insertParas.size() - 1)
                insertSql.append(",");
        }
        insertSql.append(")");
        return insertSql.toString();

    }

    public static String getTableName(Class<?> beanClass) {
        boolean isTable = beanClass.isAnnotationPresent(Table.class);
        if (isTable) {
            Table table = beanClass.getAnnotation(Table.class);
            return table.tableName();
        } else {
            return getString(null, beanClass.getSimpleName(), true);
        }
    }

    private static String getString(String first, String nameStr, boolean isTable) {
        List<String> ss = new ArrayList<>();
        int index = 0;
        int len = nameStr.length();
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(nameStr.charAt(i))) {
                String res = nameStr.substring(index, i).toLowerCase();
                ss.add(res);
                index = i;
            }
        }
        ss.add(nameStr.substring(index, len).toLowerCase());
        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isEmpty(first)) {
            sb.append(first);
        }
        for (int i = 0; i < ss.size(); i++) {
            if (StringUtils.isEmpty(ss.get(i))) {
                continue;
            }
            sb.append(ss.get(i));
            if (i < ss.size() - 1) {
                sb.append("_");
            }
        }
        String result = sb.toString();
        if (isTable) {
            String[] results = result.split("_");
            if (results[results.length - 1].equals("model")) {
                result = result.substring(0, result.length() - 6);
            }
        }
        return result;
    }


    public static String getUpdateSql(Class<?> beanClass) {
        String tableName = getTableName(beanClass);
        Field[] fields = beanClass.getDeclaredFields();
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("update ").append(tableName).append(" set ");
        Map<String, String> primaryKeys = new HashMap<String, String>();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (column.column().equals("")) {
                        String str = getString(null, field.getName(), false);
                        primaryKeys.put(str, field.getName());
                    } else {
                        primaryKeys.put(column.column(), field.getName());
                    }
                    continue;
                }
                JSONField jsonField = field.getAnnotation(JSONField.class);
                if(jsonField!=null){
                    continue;
                }
                String columnName = "";
                if (column != null && !column.column().equals("")) {
                    columnName = column.column();
                } else {
                    columnName = getString(null, field.getName(), false);
                }
                updateSql.append(columnName).append("=").append(":").append(field.getName());
                if (i != fields.length - 1) {
                    updateSql.append(",");
                }
            }
            if (primaryKeys.size() < 1) {
                throw new Exception("table has no primaryKey");
            }
        } catch (Exception e) {
            new RuntimeException("get update sql is exceptoin:" + e);
        }
        updateSql.append(" where ");
        addEnding(updateSql, primaryKeys);
        return updateSql.toString();
    }


    public static String getDeleteSql(Class<?> beanClass) {
        String tableName = getTableName(beanClass);
        Field[] fields = beanClass.getDeclaredFields();
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from ").append(tableName).append(" where ");
        Map<String, String> primaryKeys = new HashMap<String, String>();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (column.primaryKey() != PrimaryKey.NO_KEY) {
                        if (column.column().equals("")) {
                            String str = getString(null, field.getName(), false);
                            primaryKeys.put(str, field.getName());
                        } else {
                            primaryKeys.put(column.column(), field.getName());
                        }
                        continue;
                    }
                }
            }
            int i = 0;
            for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
                deleteSql.append(entry.getKey() + " =:" + entry.getValue());
                if (i < primaryKeys.size() - 1) {
                    deleteSql.append(" and ");
                }
                i++;
            }
        } catch (Exception e) {
            new RuntimeException("get delete sql is exceptoin:" + e);
        }
        return deleteSql.toString();
    }

    public static String getQuerySql(Class<?> beanClass) {
        String tableName = getTableName(beanClass);
        Field[] fields = beanClass.getDeclaredFields();
        StringBuffer selectSql = new StringBuffer();
        List<String> selectParaNames = new ArrayList<String>();
        Map<String, String> primaryKeys = new HashMap<String, String>();
        selectSql.append("select ");
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (column.primaryKey() != PrimaryKey.NO_KEY) {
                        if (column.column().equals("")) {
                            String str = getString(null, field.getName(), false);
                            primaryKeys.put(str, field.getName());
                        } else {
                            primaryKeys.put(column.column(), field.getName());
                        }
                    }
                }
                String columnName = "";
                if (column != null && !column.column().equals("")) {
                    columnName = column.column();
                } else {
                    columnName = getString(null, field.getName(), false);
                }
                selectParaNames.add(columnName);
            }
        } catch (Exception e) {
            new RuntimeException("get select sql is exception:" + e);
        }
        int j = 0;
        for (String columnName : selectParaNames) {
            selectSql.append(columnName);
            if (j != fields.length - 1) {
                selectSql.append(",");
            }
            j++;
        }
        selectSql.append(" from ").append(tableName).append(" where ");
        addSelectEnding(selectSql, primaryKeys);
        return selectSql.toString();
    }


    private static void addEnding(StringBuffer buffer, Map<String, String> primaryKeys) {
        int i = 0;
        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            buffer.append(entry.getKey() + "=:" + entry.getValue());
            if (i < primaryKeys.size() - 1) {
                buffer.append(" and ");
            }
            i++;
        }
    }


    private static void addSelectEnding(StringBuffer buffer, Map<String, String> primaryKeys) {
        int i = 0;
        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            buffer.append(entry.getKey() + "= ?");
            if (i < primaryKeys.size() - 1) {
                buffer.append(" and ");
            }
            i++;
        }
    }

    public static String getQueryAllSql(Class<?> bean) {
        return "select  * from " + getTableName(bean);
    }
}
