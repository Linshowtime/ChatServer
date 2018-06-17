package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.Group;
import com.nt.mapper.GroupMapper;
import com.nt.service.IGroupService;

@Service
public class GroupServiceImpl implements IGroupService {
	@Autowired
	private GroupMapper mapper;
	@Override
	public int saveGroup(Group group) {
		return mapper.insertSelective(group);
	}

	@Override
	public Group findGroupByName(String name) {
		return mapper.selectByName(name);
	}

	@Override
	public Group findGroupById(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateGroup(Group group) {
		return mapper.updateByPrimaryKeySelective(group);
	}

	@Override
	public int deleteGroup(int id) {

		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Group> findGroupLikeName(String name) {
		
		return mapper.selectLikeName(name);
	}
}
