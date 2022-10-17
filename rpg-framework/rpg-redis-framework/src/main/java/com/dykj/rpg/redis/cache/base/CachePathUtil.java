package com.dykj.rpg.redis.cache.base;

import java.util.List;

/**
 * User: jyb
 * Date: 2018-11-26 22:44
 */
public class CachePathUtil {

    public static String cachePath2String(List paths) {
        return cachePath2String(paths.toArray());
    }

    public static String cachePath2String(Object ... paths) {
        StringBuffer buffer = new StringBuffer();
        int i;
        for (i=0; i<paths.length - 1; i++) {
            buffer.append(paths[i]).append(MarkEn.SEMICOLON.value());
        }
        if (0 < paths.length && i == (paths.length - 1)) {
            buffer.append(paths[i]);
        }
        return buffer.toString();
    }

    public enum  MarkEn {

        SEMICOLON(':'),
        COMMA(',')
        ;
        private char mark;

        MarkEn(char mark) {
            this.mark = mark;
        }

        public char value() {
            return mark;
        }
    }
}
