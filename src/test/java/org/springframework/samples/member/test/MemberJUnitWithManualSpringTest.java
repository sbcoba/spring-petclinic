package org.springframework.samples.member.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.member.MemberConfig;
import org.springframework.samples.member.MemberService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MemberConfig.class)
public class MemberJUnitWithManualSpringTest {
	@Autowired
	public MemberService memberService;

	@Test
	public void test() throws Exception {
		assertEquals(memberService.getMember(1).getName(), "이수홍");
		assertEquals(memberService.getMember(2).getName(), "박용권");
		assertEquals(memberService.getMember(3).getName(), "김영한");
	}
}
