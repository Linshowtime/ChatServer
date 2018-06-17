package com.nt.dto;

public class GroupMessageDTO {
	private String groupName;

	private String senderName;

	private String time;
	
	private String content;

	public GroupMessageDTO(String groupName, String senderName, String time, String content) {
		super();
		this.groupName = groupName;
		this.senderName = senderName;
		this.time = time;
		this.content = content;
	}

	public String getgroupName() {
		return groupName;
	}

	public void setgroupName(String groupName) {
		this.groupName = groupName;
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

}
