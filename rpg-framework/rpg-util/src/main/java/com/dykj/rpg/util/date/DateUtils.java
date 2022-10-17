package com.dykj.rpg.util.date;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jyb
 * @date 2020/12/23 16:17
 * @Description
 */
public class DateUtils {
    /**
     * 判断某个时间到现在有没有今天的几点,做重置用
     * 比如 凌晨四点重置  startTime 定要比当前时间小
     *
     * @param startTime
     * @param hour
     * @return
     */
    public static boolean isPassHour(Date startTime, int hour) {
        Calendar startCalendar = CalendarUtil.calendar(startTime);
        Calendar nowCalendar = CalendarUtil.calendar(new Date());
        if (startTime.getTime() > nowCalendar.getTime().getTime()) {
            return false;
        }
        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        if (DateUtil.isSameDay(startTime, nowCalendar.getTime())) {
            int starHour = startCalendar.get(Calendar.HOUR_OF_DAY);
            if (starHour < hour && hour <= nowHour) {
                return true;
            }
        } else {
            //跨月肯定是直接重置
            if (!DateUtil.isSameMonth(startTime, nowCalendar.getTime())) {
                return true;
            }
            int starDay = startCalendar.get(Calendar.DATE);
            int nowDay = nowCalendar.get(Calendar.DATE);
            //说明跨了两天了 直接重置
            if (nowDay - starDay > 1) {
                return true;
            } else {
                //跨一天的情况，看当前时间有没有过当天的hour点
                if (nowHour >= hour) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据hour 拿到startTime的重置时间
     * 如果hour等于 5  如果startTime <当天的5点  取当天的五点，反之 取第二天的五点
     *
     * @param startTime
     * @param hour
     * @return
     */
    public static Date getRefreshTime(Date startTime, int hour) {
        Calendar startCalendar = CalendarUtil.calendar(startTime);
        Calendar refreshCalendar = CalendarUtil.calendar(startTime);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        if (startHour < hour) {
            refreshCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE));
        } else {
            refreshCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE) + 1);
        }
        refreshCalendar.set(Calendar.HOUR_OF_DAY, hour);
        refreshCalendar.set(Calendar.MINUTE, 0);
        refreshCalendar.set(Calendar.SECOND, 0);
        refreshCalendar.set(Calendar.MILLISECOND, 0);
        return refreshCalendar.getTime();
    }

    public static void main(String[] args) {
        Date date = new Date();
        Calendar startCalendar = CalendarUtil.calendar(date);
        startCalendar.set(Calendar.HOUR_OF_DAY, 12);;
        String s = DateUtil.format(getRefreshTime(startCalendar.getTime(), 13), "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);
        System.out.println(isPass(startCalendar.getTime(), 13));
    }

    /**
     * 判断某个时间到现在有没有今天的几点,做重置用
     * 比如 凌晨四点重置  startTime 定要比当前时间小
     *
     * @param startTime
     * @param hour
     * @return
     */
    public static boolean isPass(Date startTime, int hour) {
        Date date = getRefreshTime(startTime, hour);
        return new Date().getTime() > +date.getTime();
    }

    /**
     * java获取格林威治时间
     * @return
     */
    public static Date getGMTTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970,0,01);
        return  calendar.getTime();
    }

    /**
     * LocalDateTime转Date
     * @param localDateTime
     * @return
     */
    public static Date conversionDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime conversionLocalDateTime(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}