package com.nt.dto;

public class RecordDTO {
private Integer recordId;//添加好友記錄ID
private String friendName;//添加好友名字
private String message;//驗證信息
private Integer tag;
private String categoryName;

public Integer getTag() {
	return tag;
}
public void setTag(Integer tag) {
	this.tag = tag;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
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
