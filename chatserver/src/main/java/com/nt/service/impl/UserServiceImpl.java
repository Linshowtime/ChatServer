package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.User;
import com.nt.mapper.UserMapper;
import com.nt.service.IUserService;
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper usermapper;

	@Override
	public User findByName(String name) {

		User user = usermapper.selectByName(name);
		return user;
	}

	@Override
	public User findByEmail(String email) {
		User user = usermapper.selectByEmail(email);
		return user;
	}

	@Override
	public int saveUser(User user) {
		
		return usermapper.insertSelective(user);
	}

	@Override
	public User findById(Integer id) {

		return usermapper.selectByPrimaryKey(id);
	}

	@Override
	public List<User> findLikeName(String name) {
		
		return usermapper.selectLikeName(name);
	}

	@Override
	public  int  modifyUserInfo(User user) {
	return usermapper.updateByPrimaryKeySelective(user);
	}

}
