package com.dykj.rpg.uc.balance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/15 10:18
 * @Description:
 */
public class UcLoadBalance extends AbstractLoadBalance {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
       /* String serverStr = invocation.getArguments()[0].toString();
        int serverId = Integer.valueOf(serverStr);
        ServerCacheMgr serverCacheMgr = BeanFactory.getBean(ServerCacheMgr.class);
        ServerModel serverInfoModel = serverCacheMgr.get(serverId);
        Invoker<T> invoker = null;
        try {
            serverCacheMgr.lock(serverId);
            if (StringUtils.isNotBlank(serverInfoModel.getAddress())) {
                String[] result = serverInfoModel.getAddress().split(":");
                String address = result[0];
                int port = Integer.valueOf(result[1]);
                for (Invoker<T> iv : invokers) {
                    if (iv.getUrl().getHost().equals(address) && iv.getUrl().getPort() == port) {
                        invoker = iv;
                        return invoker;
                    }
                }
            }
            String addressStr = serverInfoModel.getAddressList();
            String[] addressList = addressStr.split(";");
            for (String address : addressList) {
                String[] result = address.split(":");
                String host = result[0];
                int port = Integer.valueOf(result[1]);
                for (Invoker<T> iv : invokers) {
                    if (iv.getUrl().getHost().equals(host) && iv.getUrl().getPort() == port) {
                        invoker = iv;
                        return invoker;
                    }
                }
            }
            if (invoker == null) {
                logger.error("UcLoadBalance select invoker error  serverId {} ", serverId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverCacheMgr.unlock(serverId);
        }*/

        return null;
    }


}
