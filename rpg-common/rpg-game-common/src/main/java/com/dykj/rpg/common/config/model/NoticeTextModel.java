package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 公告信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class NoticeTextModel extends BaseConfig<Integer>
{
    private Integer noticeId;//序号
    private String noticeEdition;//版本号
    private String noticeContent;//更新内容

    public Integer getNoticeId()
    {
        return this.noticeId;
    }

    public void setNoticeId(Integer noticeId)
    {
        this.noticeId = noticeId;
    }

    public String getNoticeEdition()
    {
        return this.noticeEdition;
    }

    public void setNoticeEdition(String noticeEdition)
    {
        this.noticeEdition = noticeEdition;
    }

    public String getNoticeContent()
    {
        return this.noticeContent;
    }

    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    @Override
    public Integer getKey()
    {
        return this.noticeId;
    }
}