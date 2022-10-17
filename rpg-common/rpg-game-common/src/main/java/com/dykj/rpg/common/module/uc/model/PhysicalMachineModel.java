package com.dykj.rpg.common.module.uc.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author jyb
 * @date 2020/12/29 19:40
 * @Description
 */
public class PhysicalMachineModel extends BaseModel {

    /**
     * 物理机id
     */
    @Column(primaryKey = PrimaryKey.INCREMENT)
    private int machineId;

    /**
     * 内网ip
     */
    private  String IntranetIp;


    /**
     * 外网ip
     */
    private String InternetIp;



    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getIntranetIp() {
        return IntranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        IntranetIp = intranetIp;
    }

    public String getInternetIp() {
        return InternetIp;
    }

    public void setInternetIp(String internetIp) {
        InternetIp = internetIp;
    }
}