package com.nt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nt.entity.ContactInvation;

public interface ContactInvationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ContactInvation record);

    int insertSelective(ContactInvation record);

    ContactInvation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContactInvation record);

    int updateByPrimaryKey(ContactInvation record);
    
    List<ContactInvation> findInvationRecords(@Param("contactid") Integer id);
}