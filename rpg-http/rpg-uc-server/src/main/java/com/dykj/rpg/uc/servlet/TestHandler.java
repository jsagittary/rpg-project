package com.dykj.rpg.uc.servlet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHandler {

    @RequestMapping(value = "test.do", method = RequestMethod.GET)
    public String doTest(){
        return  "success";
    }
}
