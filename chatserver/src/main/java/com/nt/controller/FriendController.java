package com.nt.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.RecordDTO;
import com.nt.dto.ReqFriend;
import com.nt.dto.UserDTO;
import com.nt.entity.Category;
import com.nt.entity.CategoryMember;
import com.nt.entity.ContactInvation;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.ICategoryMemberService;
import com.nt.service.ICategoryService;
import com.nt.service.IContactInvationService;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;
import com.nt.util.UserDTOUtil;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class FriendController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ICategoryMemberService categoryMemberService;
	@Autowired
	private IContactInvationService contactInvationService;

	/**
	 * 发起添加好友请求
	 * 
	 * @author showtime
	 * @param useid
	 *            发起添加好友请求者的id
	 * @param username
	 *            对方的账号
	 * @param name
	 *            分组名称，默认值为“我的好友”
	 * @param message
	 *            驗證信息
	 * @return
	 * 
	 */
	@RequestMapping("/addFriend")
	public Result addFriend(ServletRequest request, @RequestBody ReqFriend friend) {
		User u = userService.findByName(friend.getUsername());
		if (u == null) {
			return ResultUtil.error(1, "不存在该账号用户");
		} else {
			try {
				int memberid = u.getId();
				if (memberid == Integer.valueOf(request.getAttribute("userid").toString())) {
					return ResultUtil.error(1, "不能添加自己");
				}
				String categoryName = (friend.getCategoryName() == null) ? "我的好友" : friend.getCategoryName();
				String message = (friend.getMessage() == null) ? "您好！" : friend.getMessage();
				Category category = categoryService
						.findCategory(Integer.valueOf(request.getAttribute("userid").toString()), categoryName);
				int categoryId;
				if (category == null) {
					Category c = new Category();
					c.setName(categoryName);
					c.setOwnerId(Integer.valueOf(request.getAttribute("userid").toString()));
					categoryService.saveCategory(c);
					categoryId = c.getId();
				} else {
					categoryId = category.getId();
				}
				if (categoryMemberService.findMember(Integer.valueOf(request.getAttribute("userid").toString()),
						memberid) != null) {
					return ResultUtil.error(1, "已存在该好友");
				} else {
					ContactInvation invation = new ContactInvation();
					invation.setCategoryId(categoryId);
					invation.setContactUserId(memberid);
					invation.setUserId(Integer.valueOf(request.getAttribute("userid").toString()));
					invation.setMessage(message);
					invation.setTag(0);
					contactInvationService.saveInvation(invation);
					List<String> users = new ArrayList<String> ();
					users.add(friend.getUsername());
					return ResultUtil.success(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加好友异常");
			}

		}
	}

	/**
	 * 查看别人添加自己记录
	 * 
	 * @author showtime
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchcontactinvation")
	public Result searchContactInvation(ServletRequest request) {
		try {
			List<ContactInvation> invations = contactInvationService
					.findContacts(Integer.valueOf(request.getAttribute("userid").toString()));
			List<RecordDTO> records = new ArrayList<RecordDTO>();
			if (invations != null) {
				for (ContactInvation invation : invations) {
					RecordDTO d = new RecordDTO();
					d.setRecordId(invation.getId());
					d.setMessage(invation.getMessage());
					d.setFriendName(userService.findById(invation.getUserId()).getUsername());
					d.setTag(invation.getTag());
					d.setCategoryName(categoryService.findCategoryById(invation.getCategoryId()).getName());
					records.add(d);
					invation.setTag(1);
					contactInvationService.updateInvation(invation);
				}
				return ResultUtil.success(records);
			} else {
				return ResultUtil.error(1, "没有添加新好友记录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看添加新好友记录异常");
		}
	}

	/**
	 * 查看自己添加别人记录
	 * 
	 * @author showtime
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchContactInvationToOthers")
	public Result searchContactInvationToOthers(ServletRequest request) {
		try {
			List<ContactInvation> invations = contactInvationService
					.findContactsToOthers(Integer.valueOf(request.getAttribute("userid").toString()));
			List<RecordDTO> records = new ArrayList<RecordDTO>();
			if (invations != null) {
				for (ContactInvation invation : invations) {
					RecordDTO d = new RecordDTO();
					d.setFriendName(userService.findById(invation.getContactUserId()).getUsername());
					d.setCategoryName(categoryService.findCategoryById(invation.getCategoryId()).getName());
					d.setTag(invation.getTag());
					d.setMessage(invation.getMessage());
					d.setRecordId(invation.getId());
					records.add(d);
				}
				return ResultUtil.success(records);
			} else {
				return ResultUtil.error(1, "没有添加新好友记录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看添加新好友记录异常");
		}
	}

	/**
	 * 確認好友添加申請
	 * 
	 * @author ShowTime
	 * @param recordId
	 *            新增好友記錄Id
	 * @param categoryName
	 *            分組名稱
	 * @return
	 */
	@RequestMapping("/confirmcontactinvation")
	public Result confirmcontactinvation(@RequestParam(value = "recordId") Integer recordId,
			 String categoryName, @RequestParam(value = "type") Integer type) {
		try {
			if (recordId == null) {
				return ResultUtil.error(1, "添加好友记录不能为空");
			}
			ContactInvation invation = contactInvationService.findById(recordId);
			if (invation == null) {
				return ResultUtil.error(1, "不存在该好友添加记录");
			}
			if (type == 0) {// type为0表示确认添加，1表示拒绝添加
				CategoryMember member = new CategoryMember();
				member.setCategoryId(invation.getCategoryId());
				member.setMemberId(invation.getContactUserId());
				member.setOwnerId(invation.getUserId());
				CategoryMember member1 = new CategoryMember();
				categoryName = (categoryName == null) ? "我的好友" : categoryName;
				Category category = categoryService.findCategory(Integer.valueOf(invation.getContactUserId()),
						categoryName);
				int categoryId;
				if (category == null) {
					Category c = new Category();
					c.setName(categoryName);
					c.setOwnerId(invation.getContactUserId());
					categoryId = categoryService.saveCategory(c);
				} else {
					categoryId = category.getId();
				}
				member1.setCategoryId(categoryId);
				member1.setMemberId(invation.getUserId());
				member1.setOwnerId(invation.getContactUserId());
				invation.setTag(2);
				contactInvationService.updateInvation(invation);
				if (categoryMemberService.findMember(member.getOwnerId(), member.getMemberId()) == null) {
					categoryMemberService.saveMember(member);
				}
				if (categoryMemberService.findMember(member1.getOwnerId(), member1.getMemberId()) == null) {
					categoryMemberService.saveMember(member1);
				}
			} else {
				invation.setTag(3);
			}
			return ResultUtil.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "添加新好友异常");
		}
	}

	/**
	 * 修改好友昵称
	 * 
	 * @param request
	 * @param aliaName
	 * @param friendName
	 * @return
	 */
	@RequestMapping("/modifyAliaName")
	public Result modifyAliaName(ServletRequest request, @RequestParam(value = "aliaName") String aliaName,
			@RequestParam(value = "friendName") String friendName) {
		try {
			if (friendName == null || aliaName == null) {
				return ResultUtil.error(1, "friendName,aliaName不能为空");
			}
			CategoryMember categoryMember = categoryMemberService.findMember(
					Integer.valueOf(request.getAttribute("userid").toString()),
					userService.findByName(friendName).getId());
			if (categoryMember == null) {
				ResultUtil.error(1, "该好友不存在");
			}
			categoryMember.setAliaName(aliaName);
			categoryMemberService.modifyMember(categoryMember);
			return ResultUtil.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "修改好友昵称异常");
		}
	}

	/**
	 * 刪除好友
	 * 
	 * @author ShowTime
	 * @param request
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/deleteFriend")
	public Result deleteFriend(ServletRequest request, String friendName) {
		if (friendName == null) {
			return ResultUtil.error(1, "删除对方账号不能为空");
		} else {
			try {
				categoryMemberService.deleteMember(Integer.valueOf(request.getAttribute("userid").toString()),
						userService.findByName(friendName).getId());
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "删除好友异常");
			}
		}
	}
	/**
	 * 根據好友名字模糊查詢
	 * @author ShowTime
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/findFriend")
	public Result findFriend(ServletRequest request, String friendName) {
		if (friendName == null) {
			return ResultUtil.error(1, "查詢條件不能為空");
		} else {
			List<User> list = new ArrayList<User>();
			List<UserDTO> userInfos = new ArrayList<UserDTO>();
			try {
				list = userService.findLikeName(friendName);
				if (list.size() == 0) {
					return ResultUtil.error(1, "不存在該用戶");
				} else {
					for (User user : list) {
						if (categoryMemberService.findMember(Integer.valueOf(request.getAttribute("userid").toString()),
								user.getId()) != null) {
							UserDTO userdto = UserDTOUtil.userToUserDto(user);
							
							userdto.setAliaName(categoryMemberService.findMember(Integer.valueOf(request.getAttribute("userid").toString()),
								user.getId()).getAliaName());
							
							userInfos.add(userdto);
						}
					}
					if (userInfos.size() == 0) {
						return ResultUtil.error(1, "不存在該好友");

					}
					return ResultUtil.success(userInfos);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "查找好友异常");
			}
		}
	}
}
