package com.nt.entity;

import java.util.Date;

public class ContactInvation {
    private Integer id;

    private Integer userId;

    private Integer contactUserId;

    private Date createTime;

    private Integer tag;

    private Integer categoryId;

    private String message;//驗證信息
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(Integer contactUserId) {
        this.contactUserId = contactUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}