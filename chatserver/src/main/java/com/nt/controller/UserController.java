package com.nt.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.UserDTO;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class UserController {
	@Autowired
	private IUserService userService;
	/**
	 * 查看用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/searchUserInfo")
	public Result searchUserInfo(String username) {
		try {
			User u = userService.findByName(username);
			UserDTO userdto = new UserDTO();
			userdto.setEmail(u.getEmail());
			userdto.setId(u.getId());
			userdto.setPhone(u.getPhone());
			userdto.setRegion(u.getRegion());
			userdto.setGender(u.getGender()==null ? -1 : u.getGender());
			userdto.setStatus(u.getStatus());
			userdto.setUsername(u.getUsername());
			userdto.setNickname(u.getNickname());
			userdto.setHeadUrl(u.getHeadUrl());
			return ResultUtil.success(userdto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看好友信息异常");
		}
	}
}
