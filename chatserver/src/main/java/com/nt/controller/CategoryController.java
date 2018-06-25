package com.nt.controller;

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

import com.nt.dto.ReqCategory;
import com.nt.dto.UserDTO;
import com.nt.entity.Category;
import com.nt.entity.CategoryMember;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.service.ICategoryMemberService;
import com.nt.service.ICategoryService;
import com.nt.service.IUserService;
import com.nt.util.ResultUtil;
import com.nt.util.UserDTOUtil;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class CategoryController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ICategoryMemberService categoryMemberService;

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
					return ResultUtil.success(null);
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
					return ResultUtil.success(null);
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
	public Result deleteCategory(ServletRequest request, @RequestBody Category c) {
		if (c.getName() == null) {
			return ResultUtil.error(1, "分组名称不能为空");
		} else {
			try {
				Category category = categoryService
						.findCategory(Integer.valueOf(request.getAttribute("userid").toString()), c.getName());
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
						UserDTO userdto = UserDTOUtil.userToUserDto(user);
						userdto.setAliaName(member.getAliaName());
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
					UserDTO userdto = UserDTOUtil.userToUserDto(user);
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
}
