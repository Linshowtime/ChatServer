package com.nt.service;

import java.util.List;

import com.nt.entity.ChatMessage;

public interface IChatMessageService {
	int saveMessage(ChatMessage message);

	List<ChatMessage> findMessages(Integer senderId, Integer receiverId);

	List<ChatMessage> findUnreadMessages(Integer senderId, Integer receiverId);

	List<ChatMessage> findMessagesLikeContent(Integer senderId, Integer receiverId, String content);
	
	int modifyMessage(ChatMessage message);
}
