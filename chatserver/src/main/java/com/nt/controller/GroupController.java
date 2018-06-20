package com.nt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.UserDTO;
import com.nt.entity.Group;
import com.nt.entity.GroupUser;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.IGroupService;
import com.nt.service.IGroupUserService;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api/auth")
public class GroupController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;

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
					groupuser.setMaxMessageId(0);
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
	public Result addGroupMember(@RequestParam(value = "groupId") Integer groupId,
			@RequestParam(value = "userName") String userName) {
		if (groupId == null || userName == null) {
			return ResultUtil.error(1, "groupId,userName不能爲空");
		} else {
			try {
				User user = userService.findByName(userName);
				if (user == null) {
					return ResultUtil.error(1, "不存在該用戶");
				}
				Group group = groupService.findGroupById(groupId);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				}
				if (groupUserService.findGroupUser(group.getId(), user.getId()) != null) {
					return ResultUtil.error(1, "用戶已在該群");
				}
				GroupUser groupuser = new GroupUser();
				groupuser.setGroupId(group.getId());
				groupuser.setUserId(user.getId());
				groupuser.setMaxMessageId(0);
				groupUserService.saveGroupUser(groupuser);
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "添加群成員异常");
			}
		}
	}

	/**
	 * 加群
	 * 
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/joinGroup")
	public Result joinGroup(ServletRequest request, @RequestParam(value = "groupId") Integer groupId) {
		if (groupId == null) {
			return ResultUtil.error(1, "groupId不能爲空");
		} else {
			try {
				User user = userService.findById(Integer.valueOf(request.getAttribute("userid").toString()));
				if (user == null) {
					return ResultUtil.error(1, "不存在該用戶");
				}
				Group group = groupService.findGroupById(groupId);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				}
				if (groupUserService.findGroupUser(group.getId(), user.getId()) != null) {
					return ResultUtil.error(1, "您已在該群");
				}
				GroupUser groupuser = new GroupUser();
				groupuser.setGroupId(group.getId());
				groupuser.setUserId(user.getId());
				groupuser.setMaxMessageId(0);
				groupUserService.saveGroupUser(groupuser);
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "加群异常");
			}
		}
	}

	/**
	 * 退群
	 * 
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/leaveGroup")
	public Result leaveGroup(ServletRequest request, @RequestParam(value = "groupId") Integer groupId) {
		if (groupId == null) {
			return ResultUtil.error(1, "groupId不能爲空");
		} else {
			try {
				Group group = groupService.findGroupById(groupId);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				}
				if (groupUserService.findGroupUser(group.getId(),
						Integer.valueOf(request.getAttribute("userid").toString())) == null) {
					return ResultUtil.error(1, "您不在該群");
				}
				groupUserService.deleteUser(groupId, Integer.valueOf(request.getAttribute("userid").toString()));
				return ResultUtil.success(null);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "退群异常");
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
	public Result deleteGroup(Integer groupId) {
		if (groupId == null) {
			return ResultUtil.error(1, "群Id不能爲空");
		} else {
			try {
				Group group = groupService.findGroupById(groupId);
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
	public Result modifyGroup(Integer groupId, String newName) {
		if (groupId == null || newName == null) {
			return ResultUtil.error(1, "groupId,newName不能爲空");
		} else {
			try {
				Group group = groupService.findGroupById(groupId);
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
	@RequestMapping("/searchGroupLikeName")
	public Result searchGroupLikeName(ServletRequest request, String groupName) {
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
				return ResultUtil.error(1, "按照群名模糊查詢群异常");
			}
		}
	}
/**
 * 根据群Id查询群信息
 * @param request
 * @param groupId
 * @return
 */
	@RequestMapping("/searchGroup")
	public Result searchGroup(ServletRequest request, Integer groupId) {
		if (groupId == null) {
			return ResultUtil.error(1, "查詢條件不能爲空");
		} else {
			try {
				Group group = groupService.findGroupById(groupId);
				if (group == null) {
					return ResultUtil.error(1, "不存在該群");
				} else {
					List<Map<String, Object>> groupInfos = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("groupName", group.getName());
					GroupUser groupUser = groupUserService.findGroupUser(group.getId(),
							Integer.valueOf(request.getAttribute("userid").toString()));
					List<UserDTO> userdtos = new ArrayList<UserDTO>();
					if (groupUser != null) {
						List<GroupUser> groupUsers = groupUserService.findGroupUserByGroupId(groupUser.getGroupId());
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
					return ResultUtil.success(groupInfos);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.error(1, "按照群Id查詢群异常");
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

}
