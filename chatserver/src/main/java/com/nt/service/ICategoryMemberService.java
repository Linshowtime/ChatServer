package com.nt.service;

import java.util.List;

import com.nt.entity.CategoryMember;

public interface ICategoryMemberService {
	int saveMember(CategoryMember member);

	CategoryMember findMember(int ownerid, int memberid);

	int deleteMember(int ownerid, int memberid);
	
	int deleteCategoryMembers(int categoryid,int ownerid);
	
	List<CategoryMember>  searchCategoryMembers(int categoryid,int ownerid);
}
