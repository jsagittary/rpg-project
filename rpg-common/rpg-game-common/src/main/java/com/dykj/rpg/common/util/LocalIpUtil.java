package com.dykj.rpg.common.util;

import com.dykj.rpg.common.module.uc.model.PhysicalMachineModel;
import com.dykj.rpg.util.IPUtil;

import java.util.List;
import java.util.Set;

/**
 * @author jyb
 * @date 2020/12/29 20:18
 * @Description
 */
public class LocalIpUtil {
    /**
     * 拿到本机的ip
     *
     * @return
     */
    public static PhysicalMachineModel getLocalIP(List<PhysicalMachineModel> physicalMachineModelList) {
        Set<String> ips = IPUtil.getIpAddress();
        for (String ip : ips) {
            for (PhysicalMachineModel physicalMachineModel : physicalMachineModelList) {
                if (ip.equals(physicalMachineModel.getInternetIp()) || ip.equals(physicalMachineModel.getIntranetIp())) {
                    return physicalMachineModel;
                }
            }
        }
        return null;
    }
}