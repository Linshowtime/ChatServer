package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.GroupUser;

public interface GroupUserMapper {
    int insert(GroupUser record);

    int insertSelective(GroupUser record);
    
    int updateSelective(GroupUser record);
    
    GroupUser selectGroupUser(@Param("groupId") Integer groupId,@Param("userId") Integer userId);
    
    int deleteByGroupId(Integer groupId);
    
    int deleteUser(@Param("groupId") Integer groupId,@Param("userId")Integer userId);
    
    List<GroupUser> selectGroupUserByGroupId(@Param("groupId") Integer groupId);
    
    List<GroupUser> selectGroupUserByUserId(@Param("userId")Integer userId);
}