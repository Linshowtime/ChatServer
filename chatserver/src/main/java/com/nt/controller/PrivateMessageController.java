package com.nt.controller;

import com.nt.dto.ChatMessageDTO;
import com.nt.dto.ReqPrivateMessage;
import com.nt.entity.Category;
import com.nt.entity.CategoryMember;
import com.nt.entity.ChatMessage;
import com.nt.net.Result;
import com.nt.service.ICategoryMemberService;
import com.nt.service.ICategoryService;
import com.nt.service.IChatMessageService;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class PrivateMessageController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ICategoryMemberService categoryMemberService;
	@Autowired
	private IChatMessageService chatMessageService;

	/**
	 * 私聊
	 *
	 * @param request
	 * @param message
	 * @return
	 */
	@RequestMapping("/sendPrivateMessage")
	public Result sendPrivateMessage(ServletRequest request, @RequestBody ReqPrivateMessage message) {
		try {
			if (message.getUserName() == null || message.getContent() == null) {
				return ResultUtil.error(1, "username,content不能為空");
			}
			ChatMessage m = new ChatMessage();
			m.setReceiverId(userService.findByName(message.getUserName()).getId());
			m.setSenderId(Integer.valueOf(request.getAttribute("userid").toString()));
			m.setContent(message.getContent());
			m.setIsRead(0);
			chatMessageService.saveMessage(m);
			return ResultUtil.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 私聊消息有异常");
		}
	}

	/**
	 * 显示与所有好友聊天内容
	 *
	 * @param request
	 * @param userName
	 * @return
	 */
	@RequestMapping("/searchChatMessages")
	public Result searchChatMessages(ServletRequest request) {
		Map<String, List<ChatMessageDTO>> result = new HashMap<>();
		try {
			List<Category> categories = categoryService
							.findCategoryByOwnerId(Integer.valueOf(request.getAttribute("userid").toString()));
			for (Category catetory : categories) {
				List<CategoryMember> members = categoryMemberService.searchCategoryMembers(catetory.getId(),
								Integer.valueOf(request.getAttribute("userid").toString()));
				for (CategoryMember member : members) {
					int memberId = member.getMemberId();
					String memberName = userService.findById(memberId).getUsername();
					List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();
					List<ChatMessage> messages = chatMessageService.findMessages(
									Integer.valueOf(request.getAttribute("userid").toString()), member.getMemberId());
					DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					for (ChatMessage message : messages) {
						ChatMessageDTO dto = new ChatMessageDTO(
										userService.findById(message.getSenderId()).getUsername(),
										userService.findById(message.getReceiverId()).getUsername(),
										sdf.format(message.getTime()), message.getContent());
						dtos.add(dto);
					}

					result.put(memberName, dtos);
				}
			}
			return ResultUtil.success(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "显示与某个人所有历史信息有异常");
		}
	}
	/**
	 * 显示与所有未读聊天内容
	 *
	 * @param request
	 * @param userName
	 * @return
	 */
	@RequestMapping("/searchUnreadChatMessages")
	public Result searchUnreadChatMessages(ServletRequest request) {
		Map<String, List<ChatMessageDTO>> result = new HashMap<>();
		try {
			List<Category> categories = categoryService
							.findCategoryByOwnerId(Integer.valueOf(request.getAttribute("userid").toString()));
			for (Category catetory : categories) {
				List<CategoryMember> members = categoryMemberService.searchCategoryMembers(catetory.getId(),
								Integer.valueOf(request.getAttribute("userid").toString()));
				for (CategoryMember member : members) {
					int memberId = member.getMemberId();
					String memberName = userService.findById(memberId).getUsername();
					List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();
					List<ChatMessage> messages = chatMessageService.findUnreadMessages(
									Integer.valueOf(request.getAttribute("userid").toString()), member.getMemberId());
					DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					for (ChatMessage message : messages) {
						ChatMessageDTO dto = new ChatMessageDTO(
										userService.findById(message.getSenderId()).getUsername(),
										userService.findById(message.getReceiverId()).getUsername(),
										sdf.format(message.getTime()), message.getContent());
						dtos.add(dto);
						if(!message.getSenderId().equals(Integer.valueOf(request.getAttribute("userid").toString()))){
							message.setIsRead(1);
						}
						chatMessageService.modifyMessage(message);
					}
					result.put(memberName, dtos);
				}
			}
			return ResultUtil.success(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "显示与某个人所有历史信息有异常");
		}
	}

	/**
	 * 输入查询条件内容模糊查询与某个人聊天记录
	 *
	 * @param request
	 * @param userName
	 * @return
	 */
	@RequestMapping("/searchChatMessagesLikeContent")
	public Result searchChatMessagesLikeContent(ServletRequest request, @RequestBody ReqPrivateMessage m) {
		try {
			List<ChatMessage> messages = chatMessageService.findMessagesLikeContent(
							Integer.valueOf(request.getAttribute("userid").toString()),
							userService.findByName(m.getUserName()).getId(), m.getContent());
			List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (ChatMessage message : messages) {
				ChatMessageDTO dto = new ChatMessageDTO(
								userService.findById(Integer.valueOf(request.getAttribute("userid").toString())).getUsername(),
								m.getUserName(), sdf.format(message.getTime()), message.getContent());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "显示与某个人所有历史信息有异常");
		}
	}
}
