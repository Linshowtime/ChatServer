package com.nt.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.IUserService;
import com.nt.util.DESUtils;
import com.nt.util.JwtUtils;
import com.nt.util.ResultUtil;

@RestController
@RequestMapping("/api/service")
public class UserController {
	@Autowired
	private IUserService userService;
	@PostMapping("/register")
	public Result register(@RequestBody User u) {
		User user = userService.findByName(u.getUsername());
		if (user != null) {
			return ResultUtil.error(1, "账号名字已存在");
		} else {
			if(userService.findByEmail(u.getEmail())!=null){
				return ResultUtil.error(1, "该邮箱已注册");
			}
			u.setPassword(DESUtils.encryptBasedDes(u.getPassword()));
			userService.saveUser(u);
			return ResultUtil.success(null);
		}
	}
	@PostMapping("/login")
	public Result login(@RequestBody User u) {
		User user = userService.findByName(u.getUsername());
		if (user != null) {
			if (DESUtils.decryptBasedDes(user.getPassword()).equals(u.getPassword())) {
				String token = JwtUtils.encode(user, 6000 * 1000 * 60 * 2);
				HashMap<String,Object> map = new HashMap<String, Object>();
				map.put("token", token);
				return ResultUtil.success(map);
			} else {
				return ResultUtil.error(1, "密码错误");
			}
		} else {
			return ResultUtil.error(1, "不存在该用户");
		}

	}
}
