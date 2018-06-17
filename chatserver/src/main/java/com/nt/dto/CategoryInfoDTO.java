package com.nt.dto;

import java.util.List;

public class CategoryInfoDTO {
private String categoryName;
private List<UserDTO> uselist;
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public List<UserDTO> getUselist() {
	return uselist;
}
public void setUselist(List<UserDTO> uselist) {
	this.uselist = uselist;
}

}
