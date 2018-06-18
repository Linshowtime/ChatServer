package com.nt.entity;

public class CategoryMember {
    private Integer id;

    private Integer categoryId;

    private Integer ownerId;

    private Integer memberId;
 
    private String aliaName;
    public String getAliaName() {
		return aliaName;
	}

	public void setAliaName(String aliaName) {
		this.aliaName = aliaName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}