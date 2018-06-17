package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.CategoryMember;
import com.nt.mapper.CategoryMemberMapper;
import com.nt.service.ICategoryMemberService;
@Service
public class CategoryMemberServiceImpl implements ICategoryMemberService {
@Autowired
private CategoryMemberMapper mapper;
	@Override
	public int saveMember(CategoryMember member) {
		
		return mapper.insert(member);
	}
	@Override
	public CategoryMember findMember(int ownerid, int memberid) {
		
		return mapper.selectMember(ownerid, memberid);
	}
	@Override
	public int deleteMember(int ownerid, int memberid) {
	
		return mapper.deleteMember(ownerid, memberid);
	}
	@Override
	public int deleteCategoryMembers(int categoryid, int ownerid) {
		return mapper.deleteCategoryMembers(categoryid, ownerid);
	}
	@Override
	public List<CategoryMember> searchCategoryMembers(int categoryid, int ownerid) {
		
		return mapper.selectCategoryMembers(categoryid, ownerid);
	}

}
