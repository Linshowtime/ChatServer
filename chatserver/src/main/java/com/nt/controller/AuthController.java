package com.nt.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.ChatMessageDTO;
import com.nt.dto.GroupMessageDTO;
import com.nt.dto.RecordDTO;
import com.nt.dto.ReqCategory;
import com.nt.dto.ReqFriend;
import com.nt.dto.UserDTO;
import com.nt.entity.Category;
import com.nt.entity.CategoryMember;
import com.nt.entity.ChatMessage;
import com.nt.entity.ContactInvation;
import com.nt.entity.Group;
import com.nt.entity.GroupMessage;
import com.nt.entity.GroupUser;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.net.WiselyMessage;
import com.nt.service.ICategoryMemberService;
import com.nt.service.ICategoryService;
import com.nt.service.IChatMessageService;
import com.nt.service.IContactInvationService;
import com.nt.service.IGroupMessageService;
import com.nt.service.IGroupService;
import com.nt.service.IGroupUserService;
import com.nt.service.IUserService;
import com.nt.service.WebSocketService;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ICategoryMemberService categoryMemberService;
	@Autowired
	private IContactInvationService contactInvationService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IChatMessageService chatMessageService;
	@Autowired
	private IGroupMessageService groupMessageService;
//	@Resource
//	WebSocketService webSocketService;

	/**
	 * 修改个人信息
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/modifyUserInfo")
	public Result modifyUserInfo(ServletRequest request, @RequestBody User user) {
		try {
			user.setId(Integer.valueOf(request.getAttribute("userid").toString()));
			userService.modifyUserInfo(user);
			User u = userService.findById(Integer.valueOf(request.getAttribute("userid").toString()));
			UserDTO userdto = new UserDTO();
			userdto.setEmail(u.getEmail());
			userdto.setId(u.getId());
			userdto.setPhone(u.getPhone());
			userdto.setRegion(u.getRegion());
			userdto.setGender(u.getGender() == 0 ? "男" : "女");
			userdto.setStatus(u.getStatus());
			userdto.setUsername(u.getUsername());
			userdto.setNickname(u.getNickname());
			userdto.setHeadUrl(u.getHeadUrl());
			return ResultUtil.success(userdto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "修改个人信息异常");
		}
	}

	/**
	 * 查看个人信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchUserInfo")
	public Result searchUserInfo(ServletRequest request) {
		try {
			User u = userService.findById(Integer.valueOf(request.getAttribute("userid").toString()));
			UserDTO userdto = new UserDTO();
			userdto.setEmail(u.getEmail());
			userdto.setId(u.getId());
			userdto.setPhone(u.getPhone());
			userdto.setRegion(u.getRegion());
			userdto.setGender(u.getGender() == 0 ? "男" : "女");
			userdto.setStatus(u.getStatus());
			userdto.setUsername(u.getUsername());
			userdto.setNickname(u.getNickname());
			userdto.setHeadUrl(u.getHeadUrl());
			return ResultUtil.success(userdto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看个人信息异常");
		}
	}

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
					contactInvationService.saveInvation(invation);
					User u1 = userService.findById(Integer.valueOf(request.getAttribute("userid").toString()));
					List<String> users = Lists.newArrayList();
					users.add(friend.getUsername());
//					webSocketService.send2Users(users,
//							new WiselyMessage(message, u1.getUsername(), friend.getUsername(), 2));
					return ResultUtil.success(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加好友异常");
			}

		}
	}

	/**
	 * 查看下线后添加好友记录
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
			@RequestParam(value = "categoryName") String categoryName) {
		try {
			if (recordId == null) {
				return ResultUtil.error(1, "添加好友记录不能为空");
			}
			ContactInvation invation = contactInvationService.findById(recordId);
			if (invation == null) {
				return ResultUtil.error(1, "不存在该好友添加记录");
			}
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
			invation.setTag(1);
			contactInvationService.updateInvation(invation);
			if (categoryMemberService.findMember(member.getOwnerId(), member.getMemberId()) == null) {
				categoryMemberService.saveMember(member);
			}
			if (categoryMemberService.findMember(member1.getOwnerId(), member1.getMemberId()) == null) {
				categoryMemberService.saveMember(member1);
			}
			return ResultUtil.success(invation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "添加新好友异常");
		}
	}
	/**
	 * 修改好友昵称
	 * @param request
	 * @param aliaName
	 * @param friendName
	 * @return
	 */
	@RequestMapping("/modifyAliaName")
	public Result modifyAliaName(ServletRequest request,@RequestParam(value = "aliaName") String aliaName,@RequestParam(value = "friendName") String friendName) {
		try {
			if(friendName==null||aliaName==null){
				return ResultUtil.error(1, "friendName,aliaName不能为空");
			}
			CategoryMember  categoryMember=categoryMemberService.findMember(Integer.valueOf(request.getAttribute("userid").toString()), userService.findByName(friendName).getId());
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
	public Result deleteFriend(ServletRequest request, Integer memberId) {
		if (memberId == null) {
			return ResultUtil.error(1, "删除对方Id不能为空");
		} else {
			try {
				categoryMemberService.deleteMember(Integer.valueOf(request.getAttribute("userid").toString()),
						memberId);
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "删除好友异常");
			}
		}
	}

	/**
	 * 根據好友名字模糊查詢
	 * 
	 * @author ShowTime
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/findFriend")
	public Result findFriend(ServletRequest request, String name) {
		if (name == null) {
			return ResultUtil.error(1, "查詢條件不能為空");
		} else {
			List<User> list = new ArrayList<User>();
			List<UserDTO> userInfos = new ArrayList<UserDTO>();
			try {
				list = userService.findLikeName(name);
				if (list.size() == 0) {
					return ResultUtil.error(1, "不存在該用戶");
				} else {
					for (User user : list) {
						if (categoryMemberService.findMember(Integer.valueOf(request.getAttribute("userid").toString()),
								user.getId()) != null) {
							UserDTO userdto = new UserDTO();
							userdto.setEmail(user.getEmail());
							userdto.setId(user.getId());
							userdto.setPhone(user.getPhone());
							userdto.setRegion(user.getRegion());
							if (user.getGender() != null) {
								userdto.setGender(user.getGender() == 0 ? "男" : "女");
							} else {
								userdto.setGender("");
							}
							userdto.setStatus(user.getStatus());
							userdto.setUsername(user.getUsername());
							userdto.setHeadUrl(user.getHeadUrl());
							userInfos.add(userdto);
						}
					}
					if (userInfos.size() == 0) {
						return ResultUtil.error(1, "不存在該好友");

					}
					return ResultUtil.success(userInfos);
				}
			}

			catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "查找好友异常");
			}
		}
	}

	/**
	 * 修改分组
	 * 
	 * @param request
	 * @param category
	 * @return
	 */
	@RequestMapping("/modifyCategory")
	public Result modifyCategory(ServletRequest request, @RequestBody ReqCategory category) {
		if (category.getOldName() == null || category.getOldName() == null) {
			return ResultUtil.error(1, "oldName,newName不能為空");
		} else {
			try {
				Category c = categoryService.findCategory(Integer.valueOf(request.getAttribute("userid").toString()),
						category.getOldName());
				if (c != null) {
					if (categoryService.findCategory(Integer.valueOf(request.getAttribute("userid").toString()),
							category.getNewName()) != null) {
						return ResultUtil.error(1, "已存在該名字分組");
					}
					c.setName(category.getNewName());
					categoryService.updateCategory(c);
					return ResultUtil.success(c);
				}
				return ResultUtil.error(1, "該分組不存在");
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "修改分組名稱异常");
			}
		}
	}

	/**
	 * 增加分组
	 * 
	 * @param request
	 * @param category
	 * @return
	 */
	@RequestMapping("/addCategory")
	public Result addCategory(ServletRequest request, @RequestBody Category category) {
		if (category.getName() == null) {
			return ResultUtil.error(1, "名稱不能為空");
		} else {
			try {
				if (categoryService.findCategory(Integer.valueOf(request.getAttribute("userid").toString()),
						category.getName()) == null) {
					Category c = new Category();
					c.setName(category.getName());
					c.setOwnerId(Integer.valueOf(request.getAttribute("userid").toString()));
					categoryService.saveCategory(c);
					return ResultUtil.success(c);
				} else {
					return ResultUtil.error(1, "该分组已存在");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加分組异常");
			}
		}
	}

	/**
	 * 删除分组
	 * 
	 * @param request
	 * @param categoryName
	 *            分組名稱
	 * @return
	 */
	@RequestMapping("/deleteCategory")
	public Result deleteCategory(ServletRequest request, String categoryName) {
		if (categoryName == null) {
			return ResultUtil.error(1, "分组名称不能为空");
		} else {
			try {
				Category category = categoryService
						.findCategory(Integer.valueOf(request.getAttribute("userid").toString()), categoryName);
				if (category != null) {
					int categoryId = category.getId();
					categoryService.deleteCategory(categoryId);
					categoryMemberService.deleteCategoryMembers(categoryId,
							Integer.valueOf(request.getAttribute("userid").toString()));
					return ResultUtil.success(null);
				} else {
					return ResultUtil.error(1, "该分组不存在");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "删除分組异常");
			}
		}
	}

	/**
	 * 根據分組名字模糊查詢
	 * 
	 * @param request
	 * @param categoryName
	 * @return
	 */
	@RequestMapping("/searchCategory")
	public Result searchCategory(ServletRequest request, String categoryName) {
		if (categoryName == null) {
			return ResultUtil.error(1, "查詢分组名称條件不能为空");
		} else {
			try {
				List<Category> categories = categoryService
						.findCategoryLikeName(Integer.valueOf(request.getAttribute("userid").toString()), categoryName);
				List<Map<String, Object>> categoryInfos = new ArrayList<Map<String, Object>>();
				for (Category catetory : categories) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("catetoryName", catetory.getName());
					List<CategoryMember> members = categoryMemberService.searchCategoryMembers(catetory.getId(),
							Integer.valueOf(request.getAttribute("userid").toString()));
					List<UserDTO> userdtos = new ArrayList<UserDTO>();
					for (CategoryMember member : members) {
						User user = userService.findById(member.getMemberId());
						UserDTO userdto = new UserDTO();
						userdto.setEmail(user.getEmail());
						userdto.setId(user.getId());
						userdto.setPhone(user.getPhone());
						userdto.setRegion(user.getRegion());
						if (user.getGender() != null) {
							userdto.setGender(user.getGender() == 0 ? "男" : "女");
						}
						userdto.setStatus(user.getStatus());
						userdto.setUsername(user.getUsername());
						userdto.setHeadUrl(user.getHeadUrl());
						userdtos.add(userdto);
					}
					map.put("categoryMemberInfos", userdtos);
					categoryInfos.add(map);
				}
				return ResultUtil.success(categoryInfos);

			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "查詢分組异常");
			}
		}
	}

	/**
	 * 查看所有分組
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchAllCategorys")
	public Result searchAllCategorys(ServletRequest request) {
		try {
			List<Category> categories = categoryService
					.findCategoryByOwnerId(Integer.valueOf(request.getAttribute("userid").toString()));
			List<Map<String, Object>> categoryInfos = new ArrayList<Map<String, Object>>();
			for (Category catetory : categories) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("catetoryName", catetory.getName());
				List<CategoryMember> members = categoryMemberService.searchCategoryMembers(catetory.getId(),
						Integer.valueOf(request.getAttribute("userid").toString()));
				List<UserDTO> userdtos = new ArrayList<UserDTO>();
				for (CategoryMember member : members) {
					User user = userService.findById(member.getMemberId());
					UserDTO userdto = new UserDTO();
					userdto.setUsername(user.getUsername());
					userdto.setEmail(user.getEmail());
					if (user.getGender() != null) {
						userdto.setGender(user.getGender() == 0 ? "男" : "女");
					}
					userdto.setHeadUrl(user.getHeadUrl());
					userdto.setPhone(user.getPhone());
					userdto.setRegion(user.getRegion());
					userdto.setStatus(user.getStatus());
					userdto.setAliaName(member.getAliaName());
					userdtos.add(userdto);
				}
				map.put("categoryMemberInfos", userdtos);
				categoryInfos.add(map);
			}
			return ResultUtil.success(categoryInfos);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看所有分組异常");
		}
	}

	/**
	 * 添加群組
	 * 
	 * @param request
	 * @param category
	 * @return
	 */
	@RequestMapping("/addGroup")
	public Result addGroup(String groupName, ServletRequest request) {
		if (groupName == null) {
			return ResultUtil.error(1, "群名稱不能爲空");
		} else {
			try {
				Group group = groupService.findGroupByName(groupName);
				if (group == null) {
					Group g = new Group();
					g.setName(groupName);
					groupService.saveGroup(g);
					GroupUser groupuser = new GroupUser();
					groupuser.setGroupId(g.getId());
					groupuser.setUserId(Integer.valueOf(request.getAttribute("userid").toString()));
					groupUserService.saveGroupUser(groupuser);
					return ResultUtil.success(g);
				} else {
					return ResultUtil.error(1, "已有该群組存在");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加群組异常");
			}
		}
	}

	/**
	 * 添加群成員
	 * 
	 * @param groupName
	 * @param userName
	 * @return
	 */
	@RequestMapping("/addGroupMember")
	public Result addGroupMember(@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "userName") String userName) {
		if (groupName == null || userName == null) {
			return ResultUtil.error(1, "groupName,userName不能爲空");
		} else {
			try {
				User user = userService.findByName(userName);
				if (user == null) {
					return ResultUtil.error(1, "不存在該用戶");
				}
				Group group = groupService.findGroupByName(groupName);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				}
				if (groupUserService.findGroupUser(group.getId(), user.getId()) != null) {
					return ResultUtil.error(1, "用戶已在該群");
				}
				GroupUser groupuser = new GroupUser();
				groupuser.setGroupId(group.getId());
				groupuser.setUserId(user.getId());
				groupUserService.saveGroupUser(groupuser);
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加群成員异常");
			}
		}
	}

	/**
	 * 解散群組
	 * 
	 * @param request
	 * @param category
	 * @return
	 */
	@RequestMapping("/deleteGroup")
	public Result deleteGroup(String groupName) {
		if (groupName == null) {
			return ResultUtil.error(1, "群名稱不能爲空");
		} else {
			try {
				Group group = groupService.findGroupByName(groupName);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				} else {
					groupService.deleteGroup(group.getId());
					groupUserService.deleteGroupUser(group.getId());
					return ResultUtil.success(null);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "刪除群組异常");
			}
		}
	}

	/**
	 * 修該群名稱
	 * 
	 * @param groupName
	 * @param newName
	 * @return
	 */
	@RequestMapping("/modifyGroup")
	public Result modifyGroup(String oldName, String newName) {
		if (oldName == null || newName == null) {
			return ResultUtil.error(1, "groupName,newName不能爲空");
		} else {
			try {
				Group group = groupService.findGroupByName(oldName);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				} else {
					if (groupService.findGroupByName(newName) != null) {
						return ResultUtil.error(1, "已存在相同名字的群");
					}
					group.setName(newName);
					groupService.updateGroup(group);
					return ResultUtil.success(group);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "修改群名稱异常");
			}
		}
	}

	/**
	 * 按照群名模糊查詢已加入的所有群
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/searchGroup")
	public Result searchGroup(ServletRequest request, String groupName) {
		if (groupName == null) {
			return ResultUtil.error(1, "查詢條件不能爲空");
		} else {
			try {
				List<Group> groups = groupService.findGroupLikeName(groupName);
				if (groups.size() == 0) {
					return ResultUtil.error(1, "不存在該群");
				} else {
					List<Map<String, Object>> groupInfos = new ArrayList<Map<String, Object>>();
					for (Group group : groups) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("groupName", group.getName());
						GroupUser groupUser = groupUserService.findGroupUser(group.getId(),
								Integer.valueOf(request.getAttribute("userid").toString()));
						List<UserDTO> userdtos = new ArrayList<UserDTO>();
						if (groupUser != null) {
							List<GroupUser> groupUsers = groupUserService
									.findGroupUserByGroupId(groupUser.getGroupId());
							for (GroupUser guser : groupUsers) {
								User user = userService.findById(guser.getUserId());
								UserDTO userdto = new UserDTO();
								userdto.setUsername(user.getUsername());
								userdto.setEmail(user.getEmail());
								userdto.setGender(user.getGender() == 0 ? "男" : "女");
								userdto.setHeadUrl(user.getHeadUrl());
								userdto.setPhone(user.getPhone());
								userdto.setRegion(user.getRegion());
								userdto.setStatus(user.getStatus());
								userdtos.add(userdto);
							}
						}
						map.put("groupMemberInfos", userdtos);
						groupInfos.add(map);
					}
					return ResultUtil.success(groupInfos);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "修改群名稱异常");
			}
		}
	}

	/**
	 * 检出已加入的所有群
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchAllGroups")
	public Result searchAllGroups(ServletRequest request) {
		try {
			List<GroupUser> groupUsers = groupUserService
					.findGroupUserByUserId(Integer.valueOf(request.getAttribute("userid").toString()));
			if (groupUsers.size() == 0) {
				return ResultUtil.error(1, "沒有加入任何該群");
			} else {
				List<Map<String, Object>> groupInfos = new ArrayList<Map<String, Object>>();
				for (GroupUser groupUser : groupUsers) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("groupName", groupService.findGroupById(groupUser.getGroupId()).getName());
					List<UserDTO> userdtos = new ArrayList<UserDTO>();
					List<GroupUser> users = groupUserService.findGroupUserByGroupId(groupUser.getGroupId());
					for (GroupUser u : users) {
						User user = userService.findById(u.getUserId());
						UserDTO userdto = new UserDTO();
						userdto.setUsername(user.getUsername());
						userdto.setEmail(user.getEmail());
						userdto.setGender(user.getGender() == 0 ? "男" : "女");
						userdto.setHeadUrl(user.getHeadUrl());
						userdto.setPhone(user.getPhone());
						userdto.setRegion(user.getRegion());
						userdto.setStatus(user.getStatus());
						userdtos.add(userdto);

					}
					map.put("groupMemberInfos", userdtos);
					groupInfos.add(map);
				}
				return ResultUtil.success(groupInfos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "检出已加入的所有群异常");
		}
	}

	/**
	 * 显示与某个人所有聊天内容
	 * 
	 * @param request
	 * @param userName
	 * @return
	 */
	@RequestMapping("/searchChatMessages")
	public Result searchChatMessages(ServletRequest request, String userName) {
		try {
			List<ChatMessage> messages = chatMessageService.findMessages(
					Integer.valueOf(request.getAttribute("userid").toString()),
					userService.findByName(userName).getId());
			List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (ChatMessage message : messages) {
				ChatMessageDTO dto = new ChatMessageDTO(
						userService.findById(Integer.valueOf(request.getAttribute("userid").toString())).getUsername(),
						userName, sdf.format(message.getTime()), message.getContent());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);

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
	public Result searchChatMessagesLikeContent(ServletRequest request, String userName,String content) {
		try {
			List<ChatMessage> messages = chatMessageService.findMessagesLikeContent(
					Integer.valueOf(request.getAttribute("userid").toString()),
					userService.findByName(userName).getId(),content);
			List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (ChatMessage message : messages) {
				ChatMessageDTO dto = new ChatMessageDTO(
						userService.findById(Integer.valueOf(request.getAttribute("userid").toString())).getUsername(),
						userName, sdf.format(message.getTime()), message.getContent());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "显示与某个人所有历史信息有异常");
		}
	}
	/**
	 * 显示某个群所有历史消息
	 * @param request
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/searchGroupMessages")
	public Result searchGroupMessages(ServletRequest request, String groupName) {
		try {
			List<GroupMessage> messages = groupMessageService.findMessagesByGroupId(
					groupService.findGroupByName(groupName).getId());
			List<GroupMessageDTO> dtos = new ArrayList<GroupMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (GroupMessage message : messages) {
				GroupMessageDTO dto = new GroupMessageDTO(
						groupName,userService.findById(message.getSenderId()).getUsername(), sdf.format(message.getTime()), message.getContent());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 显示某个群所有历史消息有异常");
		}
	}
	/**
	 * 显示某个群所有历史消息
	 * @param request
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/searchGroupMessagesLikeContent")
	public Result searchGroupMessagesLikeContent(ServletRequest request, String groupName,String content) {
		try {
			List<GroupMessage> messages = groupMessageService.findMessagesLikeContent(
					groupService.findGroupByName(groupName).getId(),content);
			List<GroupMessageDTO> dtos = new ArrayList<GroupMessageDTO>();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (GroupMessage message : messages) {
				GroupMessageDTO dto = new GroupMessageDTO(
						groupName,userService.findById(message.getSenderId()).getUsername(), sdf.format(message.getTime()), message.getContent());
				dtos.add(dto);
			}
			return ResultUtil.success(dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, " 显示某个群所有历史消息有异常");
		}
	}
}
