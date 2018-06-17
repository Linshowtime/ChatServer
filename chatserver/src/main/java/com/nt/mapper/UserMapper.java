package com.nt.mapper;

import java.util.List;

import com.nt.entity.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	User selectByName(String username);

	User selectByEmail(String email);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
	
	List<User> selectLikeName(String username);
}