package com.example.chatserver;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestA {  
    
	@Autowired
    StringEncryptor stringEncryptor;
    @Test
    public void encryptPwd() {
        String result = stringEncryptor.encrypt("2016626abc");
        System.out.println(result); 
    }

}  