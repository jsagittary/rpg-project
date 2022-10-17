package com.dykj.rpg.game.module.event.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/5 15:04
 * @Description:
 */
public abstract class AbstractEventManager<T extends Event> implements EventManager<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<T> events = new ArrayList<>();

    @Override
    public void addEvent(T t) {
        events.add(t);
    }

    @Override
    public void doEvents(Object... prams) {
        long now = 0;
        Iterator<T> it = events.iterator();
        T t;
        while (it.hasNext()) {
            now = System.currentTimeMillis();
            t = it.next();
            try {
                t.doEvent(prams);
            } catch (Exception e) {
                logger.error("event {} do  error {} ", t.getClass().getSimpleName(),e);
            } finally {
                long time = System.currentTimeMillis() - now;
                if (time >= 50) {
                    logger.error("AbstractEventManager do event {}  error  time {} ", t.getClass().getSimpleName(), time);
                }
            }
        }
    }

    @PostConstruct
    public abstract void registerEvent();
}
