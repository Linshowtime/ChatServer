package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.ChatMessage;
import com.nt.mapper.ChatMessageMapper;
import com.nt.service.IChatMessageService;
@Service
public class ChatMessageServiceImpl implements IChatMessageService {
	@Autowired
	 private ChatMessageMapper mapper;
	@Override
	public int saveMessage(ChatMessage message) {
		return mapper.insertSelective(message);
	}
	@Override
	public List<ChatMessage> findMessages(Integer senderId, Integer receiverId) {
		
		return mapper.selectMessages(senderId, receiverId);
	}
	@Override
	public List<ChatMessage> findMessagesLikeContent(Integer senderId, Integer receiverId, String content) {
		
		return mapper.selectMessagesLikeContent(senderId, receiverId, content);
	}
	@Override
	public List<ChatMessage> findUnreadMessages(Integer senderId, Integer receiverId) {
		
		return mapper.selectUnreadMessages(senderId, receiverId);
	}
	@Override
	public int modifyMessage(ChatMessage message) {
		
		return mapper.updateByPrimaryKeySelective(message);
	}

}
