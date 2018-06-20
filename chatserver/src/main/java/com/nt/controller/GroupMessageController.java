package com.nt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dto.GroupMessageDTO;
import com.nt.dto.ReqGroupMessage;
import com.nt.entity.GroupMessage;
import com.nt.entity.GroupUser;
import com.nt.net.Result;
import com.nt.service.IGroupMessageService;
import com.nt.service.IGroupUserService;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class GroupMessageController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IGroupMessageService groupMessageService;

	/**
	 * 发送群消息
	 * 
	 * @param request
	 * @param message
	 * @return
	 */
	@RequestMapping("/sendGroupMessage")
	public Result sendGroupMessage(ServletRequest request, @RequestBody ReqGroupMessage message) {
		try {
			if (message.getGroupId() == null || message.getContent() == null) {
				return ResultUtil.error(1, "groupId,content不能為空");
			}
			GroupMessage m = new GroupMessage();
			m.setGroupId(message.getGroupId());
			m.setSenderId(Integer.valueOf(request.getAttribute("userid").toString()));
			m.setContent(message.getContent());
			m.setType(message.getType() == null ? 0 : message.getType());
			groupMessageService.savaGroupMessage(m);
			return ResultUtil.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 发送群消息有异常");
		}
	}

	/**
	 * 显示群所有历史消息
	 * 
	 * @param request
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/searchGroupMessages")
	public Result searchGroupMessages(ServletRequest request) {
		try {
			List<GroupUser> groupUsers = groupUserService
					.findGroupUserByUserId(Integer.valueOf(request.getAttribute("userid").toString()));
			List<Map<String, Object>> groupMessages = new ArrayList<Map<String, Object>>();
			for (GroupUser g : groupUsers) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("groupId", g.getGroupId());
				List<GroupMessage> messages = groupMessageService.findMessagesByGroupId(g.getGroupId());
				List<GroupMessageDTO> dtos = new ArrayList<GroupMessageDTO>();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				for (GroupMessage message : messages) {
					GroupMessageDTO dto = new GroupMessageDTO(g.getGroupId(),
							userService.findById(message.getSenderId()).getUsername(), sdf.format(message.getTime()),
							message.getContent(), message.getType());
					dtos.add(dto);
				}
				map.put("messages", dtos);
				groupMessages.add(map);
			}
			return ResultUtil.success(groupMessages);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 显示所有群历史消息有异常");
		}
	}
	/**
	 * 显示未读群消息
	 * 
	 * @param request
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/searchUnreadGroupMessages")
	public Result searchUnreadGroupMessages(ServletRequest request) {
		try {
			List<GroupUser> groupUsers = groupUserService
					.findGroupUserByUserId(Integer.valueOf(request.getAttribute("userid").toString()));
			List<Map<String, Object>> groupMessages = new ArrayList<Map<String, Object>>();
			for (GroupUser g : groupUsers) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("groupId", g.getGroupId());
				List<GroupMessage> messages = groupMessageService.findUnreadMessages(g.getGroupId(),g.getMaxMessageId());
				List<GroupMessageDTO> dtos = new ArrayList<GroupMessageDTO>();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				for (GroupMessage message : messages) {
					GroupMessageDTO dto = new GroupMessageDTO(g.getGroupId(),
							userService.findById(message.getSenderId()).getUsername(), sdf.format(message.getTime()),
							message.getContent(), message.getType());
					        g.setMaxMessageId(message.getId());
					dtos.add(dto);
				}
				groupUserService.modifyGroupUser(g);
				map.put("messages", dtos);
				groupMessages.add(map);
			}
			return ResultUtil.success(groupMessages);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 显示所有群历史消息有异常");
		}
	}
	/**
	 * 输入查询条件内容模糊查询某个群消息
	 * 
	 * @param request
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/searchGroupMessagesLikeContent")
	public Result searchGroupMessagesLikeContent(ServletRequest request,@RequestBody ReqGroupMessage m) {
		try {
			List<GroupMessage> messages = groupMessageService.findMessagesLikeContent(m.getGroupId(), m.getContent());
			List<GroupMessageDTO> dtos = new ArrayList<GroupMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (GroupMessage message : messages) {
				GroupMessageDTO dto = new GroupMessageDTO(m.getGroupId(),
						userService.findById(message.getSenderId()).getUsername(), sdf.format(message.getTime()),
						message.getContent(), message.getType());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 显示某个群所有历史消息有异常");
		}
	}

}
