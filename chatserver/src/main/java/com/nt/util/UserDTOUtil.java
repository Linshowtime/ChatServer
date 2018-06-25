package com.nt.util;

import com.nt.dto.UserDTO;
import com.nt.entity.User;

public class UserDTOUtil {
public static UserDTO userToUserDto(User user){
	UserDTO userdto = new UserDTO();
	userdto.setUsername(user.getUsername());
	userdto.setEmail(user.getEmail());
	if (user.getGender() != null) {
		userdto.setGender(user.getGender() == null ? -1 : user.getGender());
	}
	userdto.setHeadUrl(user.getHeadUrl());
	userdto.setPhone(user.getPhone());
	userdto.setRegion(user.getRegion());
	userdto.setStatus(user.getStatus());
	userdto.setNickname(user.getNickname());
	return userdto;
}
}
