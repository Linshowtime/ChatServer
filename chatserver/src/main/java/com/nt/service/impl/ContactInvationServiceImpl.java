package com.nt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.ContactInvation;
import com.nt.mapper.ContactInvationMapper;
import com.nt.service.IContactInvationService;
@Service
public class ContactInvationServiceImpl implements IContactInvationService{
   @Autowired
	private ContactInvationMapper mapper;
	@Override
	public int saveInvation(ContactInvation invation) {
		
		return mapper.insertSelective(invation);
	}
	@Override
	public List<ContactInvation> findContacts(int userId) {
		
		return mapper.findInvationRecords(userId);
	}
	@Override
	public ContactInvation findById(int recordId) {
		
		return mapper.selectByPrimaryKey(recordId);
	}
	@Override
	public int updateInvation(ContactInvation invation) {
		
		return mapper.updateByPrimaryKey(invation);
	}

}
