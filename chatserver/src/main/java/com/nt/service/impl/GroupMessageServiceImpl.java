package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.GroupMessage;
import com.nt.mapper.GroupMessageMapper;
import com.nt.service.IGroupMessageService;
@Service
public class GroupMessageServiceImpl implements IGroupMessageService {
	@Autowired
	 private GroupMessageMapper mapper;
	@Override
	public int savaGroupMessage(GroupMessage message) {
	
		return mapper.insertSelective(message);
	}
	@Override
	public List<GroupMessage> findMessages(Integer groupId, Integer senderId) {
		
		return mapper.selectMessages(groupId, senderId);
	}
	@Override
	public List<GroupMessage> findMessagesByGroupId(Integer groupId) {
		return mapper.selectMessagesByGroupId(groupId);
	}
	@Override
	public List<GroupMessage> findMessagesLikeContent(Integer groupId, String content) {
		return mapper.selectMessagesLikeContent(groupId, content);
	}
	@Override
	public List<GroupMessage> findUnreadMessages(Integer groupId, Integer maxId) {
		
		return null;
	}
}
