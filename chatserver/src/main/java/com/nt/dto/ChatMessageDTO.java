package com.nt.dto;


public class ChatMessageDTO {
    private String senderName;

    private String receiverName;

    private String time;

    public ChatMessageDTO(String senderName, String receiverName, String time, String content) {
		super();
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.time = time;
		this.content = content;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	private String content;
}
