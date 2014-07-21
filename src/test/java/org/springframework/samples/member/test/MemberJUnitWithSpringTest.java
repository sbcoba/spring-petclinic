package org.springframework.samples.member.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.samples.member.MemberConfig;
import org.springframework.samples.member.MemberService;

public class MemberJUnitWithSpringTest {
	AnnotationConfigApplicationContext applicationContext;
	MemberService memberService;
	@Before
	public void setUp() {
		applicationContext = new AnnotationConfigApplicationContext(MemberConfig.class);
		memberService = applicationContext.getBean(MemberService.class);
	}
	@Test
	public void test() throws Exception {
		assertEquals(memberService.getMember(1).getName(), "이수홍");
		assertEquals(memberService.getMember(2).getName(), "박용권");
		assertEquals(memberService.getMember(3).getName(), "김영한");
	}
	@After
	public void close() {
		applicationContext.close();
	}
}
