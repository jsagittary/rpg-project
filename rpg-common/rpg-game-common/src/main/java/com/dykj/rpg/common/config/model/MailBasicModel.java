package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @author CaoBing
 * @date 2021/4/21 11:03
 * @Description:邮件的配置类
 */
public class MailBasicModel extends BaseConfig<Integer> {
    /**
     * 序号
     */
    private int id;
    /**
     * 邮件类型
     */
    private int mailType;
    /**
     *
     */
    private int requireId;//功能编号
    /**
     * 邮件标题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String mailContent;

    private String sender;
    /**
     * 邮件发送类型
     */
    private int timeType;
    /**
     * 发送时间
     */
    private String sendTime;
    /**
     * 附件类型
     */
    private int annex;
    /**
     * 附件（静态配置）
     */
    private String annexProp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public int getRequireId() {
        return requireId;
    }

    public void setRequireId(int requireId) {
        this.requireId = requireId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getAnnex() {
        return annex;
    }

    public void setAnnex(int annex) {
        this.annex = annex;
    }

    public String getAnnexProp() {
        return annexProp;
    }

    public void setAnnexProp(String annexProp) {
        this.annexProp = annexProp;
    }

    @Override
	public String toString() {
		return "MailBasicModel [id=" + id + ", mailType=" + mailType + ", requireId=" + requireId + ", title=" + title
				+ ", mailContent=" + mailContent + ", sender=" + sender + ", timeType=" + timeType + ", sendTime="
				+ sendTime + ", annex=" + annex + ", annexProp=" + annexProp + "]";
	}

	@Override
    public Integer getKey() {
        return this.id;
    }
}
