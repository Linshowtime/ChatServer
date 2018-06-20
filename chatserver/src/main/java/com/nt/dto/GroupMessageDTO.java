package com.nt.dto;

public class GroupMessageDTO {
	private Integer groupId;

	private String senderName;

	private String time;
	
	private String content;
    
	private int type;
	public GroupMessageDTO(Integer groupId, String senderName, String time, String content, int type) {
		super();
		this.groupId = groupId;
		this.senderName = senderName;
		this.time = time;
		this.content = content;
		this.type=type;
	}

	public Integer getgroupId() {
		return groupId;
	}
	public void setgroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getsenderName() {
		return senderName;
	}
	public void setsenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
