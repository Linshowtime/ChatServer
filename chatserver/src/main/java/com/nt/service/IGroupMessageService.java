package com.nt.service;

import java.util.List;

import com.nt.entity.GroupMessage;

public interface IGroupMessageService {
	int savaGroupMessage(GroupMessage message);

	List<GroupMessage> findMessages(Integer groupId, Integer senderId);
	List<GroupMessage> findMessagesByGroupId(Integer groupId);
	List<GroupMessage> findMessagesLikeContent(Integer groupId,String content);
}
