package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.GroupUser;
import com.nt.mapper.GroupUserMapper;
import com.nt.service.IGroupUserService;
@Service
public class GroupUserServiceImpl implements IGroupUserService {
	@Autowired
	private GroupUserMapper mapper;
	@Override
	public int saveGroupUser(GroupUser user) {
		
		return mapper.insertSelective(user);
	}
	@Override
	public GroupUser findGroupUser(Integer groupId, Integer userId) {
		
		return mapper.selectGroupUser(groupId, userId);
	}
	@Override
	public int deleteGroupUser(Integer groupId) {
		
		return mapper.deleteByGroupId(groupId);
	}
	@Override
	public List<GroupUser> findGroupUserByGroupId(Integer groupId) {
		return mapper.selectGroupUserByGroupId(groupId);
	}
	@Override
	public List<GroupUser> findGroupUserByUserId(Integer userId) {
		return mapper.selectGroupUserByUserId(userId);
	}
	@Override
	public int deleteUser(Integer groupId, Integer userId) {
		return mapper.deleteUser(groupId, userId);
	}
	@Override
	public int modifyGroupUser(GroupUser user) {
		return mapper.updateSelective(user);
	}

}
