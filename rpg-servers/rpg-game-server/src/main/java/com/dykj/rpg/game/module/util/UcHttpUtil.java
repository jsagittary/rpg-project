package com.dykj.rpg.game.module.util;

import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.util.HttpUtil;
import com.dykj.rpg.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jyb
 * @Date: 2020/9/4 13:58
 * @Description:
 */
@Component
public class UcHttpUtil {

    @Value("${uc.url}")
    private String ucUrl;

    /**
     * 通知登录服登录
     * @param account
     * @param channel
     * @return
     */
    public UcMsg loinUc(String account, String channel) {
        String url = ucUrl + "/loginUc";
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("channel", channel);
        String msg = HttpUtil.sendHttpPost(url, map);
        return JsonUtil.toInstance(msg, UcMsg.class);
    }
}
