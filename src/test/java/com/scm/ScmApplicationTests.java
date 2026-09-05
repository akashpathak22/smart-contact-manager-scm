package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.service.EmailService;

@SpringBootTest
class ScmApplicationTests {



	@Autowired
	EmailService service;



	@Test
	void contextLoads() {
	}



	@Test
	void sendEmailTest(){
		service.sendEmail("a1.creators01@gmail.com",
		 "Testing email Sender", 
		 "this is testing email from scm !");
	}








}
