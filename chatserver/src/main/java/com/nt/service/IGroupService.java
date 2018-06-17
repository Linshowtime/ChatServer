package com.nt.service;

import java.util.List;

import com.nt.entity.Group;
public interface IGroupService {
int saveGroup(Group group);
Group findGroupByName(String name);
Group findGroupById(int id);
int updateGroup(Group group);
int deleteGroup(int id);
List<Group> findGroupLikeName(String name);
}
