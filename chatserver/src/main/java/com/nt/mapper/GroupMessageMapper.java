package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.GroupMessage;

public interface GroupMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupMessage record);

    int insertSelective(GroupMessage record);

    GroupMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupMessage record);

    int updateByPrimaryKey(GroupMessage record);
    
    List<GroupMessage> selectMessages(@Param("groupId") Integer groupId, @Param("senderId") Integer senderId);

	List<GroupMessage> selectMessagesByGroupId(@Param("groupId") Integer groupId);
	
	List<GroupMessage> selectUnreadMessages(@Param("groupId") Integer groupId,@Param("maxId") Integer maxId);
	
	List<GroupMessage> selectMessagesLikeContent(@Param("groupId") Integer groupId,@Param("content") String content);
}