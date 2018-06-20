package com.nt.service;

import java.util.List;

import com.nt.entity.Category;

public interface ICategoryService {
int saveCategory(Category category);
Category findCategory(int ownerid,String name);
Category findCategoryById(int categoryId);
int updateCategory(Category category);
int deleteCategory(int categoryId);
List<Category> findCategoryLikeName(int ownerid,String name);
List<Category> findCategoryByOwnerId(int ownerid);
}
