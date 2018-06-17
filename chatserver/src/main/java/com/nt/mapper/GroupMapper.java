package com.nt.mapper;

import java.util.List;

import com.nt.entity.Group;

public interface GroupMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Group record);

	int insertSelective(Group record);

	Group selectByPrimaryKey(Integer id);

	Group selectByName(String name);

	int updateByPrimaryKeySelective(Group record);

	int updateByPrimaryKey(Group record);
	
	List<Group> selectLikeName(String name);
}