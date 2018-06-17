package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.GroupMessage;

public interface GroupMessageMapper {
	int insert(GroupMessage record);

	int insertSelective(GroupMessage record);

	List<GroupMessage> selectMessages(@Param("groupId") Integer groupId, @Param("senderId") Integer senderId);

	List<GroupMessage> selectMessagesByGroupId(@Param("groupId") Integer groupId);
	
	List<GroupMessage> selectMessagesLikeContent(@Param("groupId") Integer groupId,@Param("content") String content);
}