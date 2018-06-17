package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.Category;
import com.nt.mapper.CategoryMapper;
import com.nt.service.ICategoryService;
@Service
public class CategoryServiceImpl implements ICategoryService {
@Autowired
 private CategoryMapper mapper;
	@Override
	public int saveCategory(Category category) {
	return mapper.insert(category);
	}
	@Override
	public Category findCategory(int ownerid, String name) {
		return mapper.selectCategory(ownerid, name);
	}
	@Override
	public int updateCategory(Category category) {
		
		return mapper.updateByPrimaryKey(category);
	}
	@Override
	public int deleteCategory(int categoryId) {
		
		return mapper.deleteByPrimaryKey(categoryId);
	}
	@Override
	public List<Category> findCategoryLikeName(int ownerid, String name) {
		
		return mapper.selectCategoryLikeName(ownerid, name);
	}
	@Override
	public List<Category> findCategoryByOwnerId(int ownerid) {
		return mapper.selectCategoryByOwnerId(ownerid);
	}



}
