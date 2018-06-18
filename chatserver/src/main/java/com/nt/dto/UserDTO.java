package com.nt.dto;

public class UserDTO {
	private Integer id;// 好友ID
	
	private String username;// 用戶名

	private String nickname;
	
	private String email;// 郵箱

	private String  status;// 個性簽名

	private String region;// 地區

	private String phone;// 電話號碼

	private String gender;// 性別
	
	private String aliaName;
	
    public String getAliaName() {
		return aliaName;
	}

	public void setAliaName(String aliaName) {
		this.aliaName = aliaName;
	}

	private String headUrl;//頭像URL
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	

}
