package com.nt.service;

import java.util.List;

import com.nt.entity.User;

public interface IUserService {
	User findByName(String name);
	User findByEmail(String email);
	int  saveUser(User user);
	User findById(Integer id);
	List<User> findLikeName(String name);
	int modifyUserInfo(User user);
}
