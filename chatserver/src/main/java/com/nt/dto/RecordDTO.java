package com.nt.dto;

public class RecordDTO {
private Integer recordId;//添加好友記錄ID
private String friendName;//添加好友名字
private String message;//驗證信息
public Integer getRecordId() {
	return recordId;
}
public void setRecordId(Integer recordId) {
	this.recordId = recordId;
}
public String getFriendName() {
	return friendName;
}
public void setFriendName(String friendName) {
	this.friendName = friendName;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}

}
