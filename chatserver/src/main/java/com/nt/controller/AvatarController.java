package com.nt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AvatarController {
	@Autowired
	private IUserService userService;
	private static final Logger logger = LoggerFactory.getLogger(AvatarController.class);

	/**
	 * 头像上传
	 * 
	 * @param file
	 * @param request
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public Result uploadImage(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request)
			throws RuntimeException {
		if (file.isEmpty()) {
			return ResultUtil.error(1, "文件不能为空");
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		logger.info("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		logger.info("上传的后缀名为：" + suffixName);
		// 文件上传后的路径
		String filePath = request.getSession().getServletContext().getRealPath("//resources//Image//");

		fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + fileName);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
			logger.info("上传成功后的文件路径:" + filePath + fileName);
			User u = new User();
			u.setId(Integer.valueOf(request.getAttribute("userid").toString()));
			u.setHeadUrl(filePath + fileName);
			userService.modifyUserInfo(u);
			Map<String, String> map = new HashMap<String, String>();
			map.put("fileUrl", filePath + fileName);
			return ResultUtil.success(map);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResultUtil.error(1, "文件上传失败");
	}

	/**
	 * 头像下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadImage", method = RequestMethod.GET)
	public void downloadImage(HttpServletRequest request, HttpServletResponse response,String username) {
		User user = userService.findByName(username);
		String fileUrl = user.getHeadUrl();
		if (fileUrl != null) {
			File file = new File(fileUrl);
			if (file.exists()) {
				byte[] buffer = new byte[2048];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					System.out.println("success");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}