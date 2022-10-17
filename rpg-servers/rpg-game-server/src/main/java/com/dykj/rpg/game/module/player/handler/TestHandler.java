package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.test.TestRequest;
import com.dykj.rpg.protocol.test.TestResponse1;
import com.dykj.rpg.protocol.test.TestResponse2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:59
 * @Description:
 */
public class TestHandler extends AbstractClientHandler<TestRequest> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private int count = 1;

    @Override
    protected void doHandler(TestRequest testRequest, ISession session) {
        System.out.println(testRequest.toString());
        //test1(session);
        test2(session);
    }

    private void test1(ISession session){
        for(int i=0;i<count;i++){
            TestResponse1 testResponse1 = new TestResponse1();
            testResponse1.setTime(System.currentTimeMillis());
            System.out.println(testResponse1.toString());
            sendMsg(session, testResponse1);
        }
    }

    private void test2(ISession session){
        Byte[] bytes = new Byte[30000];
        bytes[0] = 1;
        bytes[29999] = 1;
        for(int i=0;i<count;i++){
            TestResponse2 testResponse2 = new TestResponse2();
            testResponse2.setData(Arrays.asList(bytes));
            sendMsg(session, testResponse2);
        }
    }
}
