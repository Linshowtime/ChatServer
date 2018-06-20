package com.nt.entity;

public class GroupUser {
    private Integer groupId;

    private Integer userId;
    
    private Integer maxMessageId;
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	public Integer getMaxMessageId() {
		return maxMessageId;
	}

	public void setMaxMessageId(Integer maxMessageId) {
		this.maxMessageId = maxMessageId;
	}
    
}