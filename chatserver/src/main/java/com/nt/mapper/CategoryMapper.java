package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.Category;

public interface CategoryMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Category record);

	int insertSelective(Category record);

	Category selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Category record);

	int updateByPrimaryKey(Category record);

	Category selectCategory(@Param("ownerid") Integer ownerid, @Param("name") String name);

	List<Category> selectCategoryLikeName(@Param("ownerid") Integer ownerid, @Param("name") String name);
	List<Category> selectCategoryByOwnerId(@Param("ownerid") Integer ownerid);
}