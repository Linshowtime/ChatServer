package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.CategoryMember;

public interface CategoryMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryMember record);

    int insertSelective(CategoryMember record);

    CategoryMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryMember record);

    int updateByPrimaryKey(CategoryMember record);
    
    CategoryMember selectMember(@Param("ownerid") int ownerid, @Param("memberid") int memberid);
    
    int deleteMember(@Param("ownerid") int ownerid, @Param("memberid") int memberid);
     
    int deleteCategoryMembers(@Param("categoryid") int categoryid,@Param("ownerid") int ownerid);
    
    List<CategoryMember> selectCategoryMembers(@Param("categoryid") int categoryid,@Param("ownerid") int ownerid);
}