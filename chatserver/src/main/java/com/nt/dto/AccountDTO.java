package com.nt.dto;

public class AccountDTO {
	private String username;// 用戶名

	private String nickname;
	
	private String email;// 郵箱

	private String  status;// 個性簽名

	private String region;// 地區

	private String phone;// 電話號碼

	private Integer gender;// 性別
	
	private String password;

	public AccountDTO(String username, String nickname, String email, String status, String region, String phone,
			int gender, String password) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.email = email;
		this.status = status;
		this.region = region;
		this.phone = phone;
		this.gender = gender;
		this.password = password;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

