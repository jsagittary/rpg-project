package com.dykj.rpg.game.servlet;

import com.dykj.rpg.net.jetty.AbstractHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: jyb
 * @Date: 2018/12/27 11:15
 * @Description:
 */
@RestController
public class TestHandler extends AbstractHandler {
    @Override
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("==============来这边了==========================");

    }

    @ResponseBody
    @RequestMapping(value = "test1.do", method = RequestMethod.GET)
    public User handle1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return new User(1001);

    }

    @ResponseBody
    @RequestMapping(value = "test2.do", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public String handle2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "我日啊";

    }



}
