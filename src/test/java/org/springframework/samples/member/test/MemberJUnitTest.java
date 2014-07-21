package org.springframework.samples.member.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.samples.member.Member;
import org.springframework.samples.member.MemberService;
import org.springframework.samples.member.MemberServiceImpl;

public class MemberJUnitTest {

	@Test
	public void test() throws Exception {
		MemberService memberService = new MemberServiceImpl();
		memberService.addMember(new Member(1, "이수홍"));
		memberService.addMember(new Member(2, "박용권"));
		memberService.addMember(new Member(3, "김영한"));

		assertEquals(memberService.getMember(1).getName(), "이수홍");
		assertEquals(memberService.getMember(2).getName(), "박용권");
		assertEquals(memberService.getMember(3).getName(), "김영한");
	}
}
