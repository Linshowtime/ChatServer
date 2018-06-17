package com.nt.service;

import java.util.List;

import com.nt.entity.GroupUser;

public interface IGroupUserService {
	int saveGroupUser(GroupUser user);

	GroupUser findGroupUser(Integer groupId, Integer userId);

	int deleteGroupUser(Integer groupId);

	List<GroupUser> findGroupUserByGroupId(Integer groupId);

	List<GroupUser> findGroupUserByUserId(Integer userId);
	
}
