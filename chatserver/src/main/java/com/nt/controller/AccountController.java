package com.nt.controller;

import java.util.HashMap;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.AccountDTO;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.IUserService;
import com.nt.util.DESUtils;
import com.nt.util.JwtUtils;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api")
public class AccountController {
	@Autowired
	private IUserService userService;

	@PostMapping("/service/register")
	public Result register(@RequestBody User u) {
		User user = userService.findByName(u.getUsername());
		if (user != null) {
			return ResultUtil.error(1, "账号名字已存在");
		} else {
			if (userService.findByEmail(u.getEmail()) != null) {
				return ResultUtil.error(1, "该邮箱已注册");
			}
			u.setPassword(DESUtils.encryptBasedDes(u.getPassword()));
			userService.saveUser(u);
			return ResultUtil.success(null);
		}
	}

	@PostMapping("/service/login")
	public Result login(@RequestBody User u) {
		User user = userService.findByName(u.getUsername());
		if (user != null) {
			if (DESUtils.decryptBasedDes(user.getPassword()).equals(u.getPassword())) {
				String token = JwtUtils.encode(user, 6000 * 1000 * 60 * 2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("token", token);
				return ResultUtil.success(map);
			} else {
				return ResultUtil.error(1, "密码错误");
			}
		} else {
			return ResultUtil.error(1, "不存在该用户");
		}

	}
	/**
	 * 查看账号个人信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/auth/searchAccoutInfo")
	public Result searchAccoutInfo(ServletRequest request) {
		try {
			User u = userService.findById(Integer.valueOf(request.getAttribute("userid").toString()));
			u.setPassword(DESUtils.decryptBasedDes(u.getPassword()));
			AccountDTO account=new AccountDTO(u.getUsername(),u.getNickname(),u.getEmail(),u.getStatus(),u.getRegion(),u.getPhone(),u.getGender() == null ? -1 : u.getGender(),u.getPassword());
			return ResultUtil.success(account);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "查看个人信息异常");
		}
	}
	/**
	 * 修改账号信息
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/auth/modifyAccountInfo")
	public Result modifyAccountInfo(ServletRequest request, @RequestBody User user) {
		try {
			user.setId(Integer.valueOf(request.getAttribute("userid").toString()));
			if(user.getUsername()!=null){
				return ResultUtil.error(1, "username不能修改");
			}
			user.setPassword(DESUtils.encryptBasedDes(user.getPassword()));
			userService.modifyUserInfo(user);
			return ResultUtil.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(1, "修改账号信息异常");
		}
	}
}
