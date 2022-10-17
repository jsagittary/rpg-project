package com.dykj.rpg.uc.nacos.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.NacosConfigService;
import com.dykj.rpg.common.consts.NacosEnum;
import com.dykj.rpg.common.module.uc.model.PhysicalMachineModel;
import com.dykj.rpg.uc.dao.PhysicalMachineDao;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/29 19:49
 * @Description
 */
@Service
@Order(1)
public class ConfigClientService {

    @NacosInjected
    private ConfigService configService;

    @Resource
    private PhysicalMachineDao physicalMachineDao;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @PostConstruct
    public void publishPhysicalMachineConfig() {
        List<PhysicalMachineModel> physicalMachineModelList = physicalMachineDao.queryAll();
        try {
            if (physicalMachineModelList == null || physicalMachineModelList.size() < 1) {
                logger.error("NacosClientConfigService publishPhysicalMachineConfig error:  physicalMachineModelList is null");
                return;
            }
            configService.publishConfig(NacosEnum.PHYSICAL_MACHINE.getDataId(), NacosEnum.PHYSICAL_MACHINE.getGroup(), JsonUtil.toJsonString(physicalMachineModelList));
        } catch (NacosException e) {
            logger.error("NacosClientConfigService publishPhysicalMachineConfig error",e);
            System.exit(-1);
        }
    }

}