package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.ChatMessage;

public interface ChatMessageMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(ChatMessage record);

	int insertSelective(ChatMessage record);

	ChatMessage selectByPrimaryKey(Integer id);

	List<ChatMessage> selectMessages(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

	List<ChatMessage> selectMessagesLikeContent(@Param("senderId") Integer senderId,
			@Param("receiverId") Integer receiverId, @Param("content") String content);

	int updateByPrimaryKeySelective(ChatMessage record);

	int updateByPrimaryKey(ChatMessage record);
}