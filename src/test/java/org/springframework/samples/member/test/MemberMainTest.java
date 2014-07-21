package org.springframework.samples.member.test;

import org.springframework.samples.member.Member;
import org.springframework.samples.member.MemberService;
import org.springframework.samples.member.MemberServiceImpl;


public class MemberMainTest {
	public static void main(String[] args) {
		MemberService memberService = new MemberServiceImpl();
		memberService.addMember(new Member(1, "박용권"));
		memberService.addMember(new Member(2, "이수홍"));
		memberService.addMember(new Member(3, "김영한"));

		System.out.println(memberService.getMember(1));
		System.out.println(memberService.getMember(2));
		System.out.println(memberService.getMember(3));
	}
}
