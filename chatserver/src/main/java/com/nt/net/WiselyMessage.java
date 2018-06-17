package com.nt.net;

public class WiselyMessage {
private String Content;//信息内容
private String senderName;
private String receiverName;//为群聊信息时，为群名称
private int type;//0表示公告，1表示群聊信息，2表示添加好友信息,3表示一对一信息
public WiselyMessage(String content, String senderName, String receiverName, int type) {
	super();
	Content = content;
	this.senderName = senderName;
	this.receiverName = receiverName;
	this.type = type;
}
public String getContent() {
	return Content;
}
public void setContent(String content) {
	Content = content;
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
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}

}
