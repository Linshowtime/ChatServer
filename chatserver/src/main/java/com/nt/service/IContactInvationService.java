package com.nt.service;

import java.util.List;

import com.nt.entity.ContactInvation;

public interface IContactInvationService {

	int saveInvation(ContactInvation invation);

	List<ContactInvation> findContacts(int userId);
	
	ContactInvation findById(int recordId);
	
	int updateInvation(ContactInvation invation);
}
