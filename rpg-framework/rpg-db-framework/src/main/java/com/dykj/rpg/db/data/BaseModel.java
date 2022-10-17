package com.dykj.rpg.db.data;

import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.annotation.Column;

import java.io.Serializable;
import java.lang.reflect.Field;


/**
 * @author: jyb
 * @version: 2018年6月12日 上午10:39:52
 * @describe:
 */
public abstract class BaseModel implements Serializable {
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Field[] fields = getClass().getDeclaredFields();
        Field[] fields1 = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            field.setAccessible(true);
            if (column == null || column.primaryKey() == PrimaryKey.NO_KEY) {
                continue;
            }
            boolean flag = false;
            for (Field field1 : fields1) {
                Column column1 = field1.getAnnotation(Column.class);
                if (column1 == null || column1.primaryKey() == PrimaryKey.NO_KEY || !field.getName().equals(field1.getName())) {
                    continue;
                }
                field1.setAccessible(true);
                try {
                    Object object = field.get(this);
                    Object object1 = field1.get(obj);
                    if (object.toString().equals(object1.toString())) {
                        flag = true;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    public int KeyNum() {
        int i = 0;
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            field.setAccessible(true);
            if (column != null && column.primaryKey() != PrimaryKey.NO_KEY) {
                i++;
            }
        }
        return i;
    }
}
