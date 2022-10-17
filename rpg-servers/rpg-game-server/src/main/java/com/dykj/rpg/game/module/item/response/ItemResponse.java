package com.dykj.rpg.game.module.item.response;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.item.UpdateItemListRs;
import com.dykj.rpg.protocol.player.RoleUpgradeRs;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;

import java.util.List;

/**
 * @Description 背包处理响应
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/4
 */
public class ItemResponse
{
    private ErrorCodeEnum codeEnum;//响应码
    private UpdateItemListRs updateItemListRs;//道具变更协议
    private List<ItemRs> itemRsList;//道具列表
    private UpdPlayerInfoRs updPlayerInfoRs;//玩家信息变更协议
    private RoleUpgradeRs roleUpgradeRs;//触发角色升级协议

    public ItemResponse()
    {
    }

    public ItemResponse(ErrorCodeEnum codeEnum)
    {
        this.codeEnum = codeEnum;
    }

    public ErrorCodeEnum getCodeEnum()
    {
        return codeEnum;
    }

    public void setCodeEnum(ErrorCodeEnum codeEnum)
    {
        this.codeEnum = codeEnum;
    }

    public UpdateItemListRs getUpdateItemListRs()
    {
        return updateItemListRs;
    }

    public void setUpdateItemListRs(UpdateItemListRs updateItemListRs)
    {
        this.updateItemListRs = updateItemListRs;
    }

    public List<ItemRs> getItemRsList()
    {
        return itemRsList;
    }

    public void setItemRsList(List<ItemRs> itemRsList)
    {
        this.itemRsList = itemRsList;
    }

    public UpdPlayerInfoRs getUpdPlayerInfoRs()
    {
        return updPlayerInfoRs;
    }

    public void setUpdPlayerInfoRs(UpdPlayerInfoRs updPlayerInfoRs)
    {
        this.updPlayerInfoRs = updPlayerInfoRs;
    }

    public RoleUpgradeRs getRoleUpgradeRs()
    {
        return roleUpgradeRs;
    }

    public void setRoleUpgradeRs(RoleUpgradeRs roleUpgradeRs)
    {
        this.roleUpgradeRs = roleUpgradeRs;
    }

    @Override
    public String toString()
    {
        return "ItemResponse{" + "codeEnum=" + codeEnum + ", updateItemListRs=" + updateItemListRs + ", itemRsList=" + itemRsList + ", updPlayerInfoRs=" + updPlayerInfoRs + ", roleUpgradeRs=" + roleUpgradeRs + '}';
    }
}