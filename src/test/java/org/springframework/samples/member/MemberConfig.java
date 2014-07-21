package org.springframework.samples.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfig {
	@Bean
	public MemberService memberService() {
		MemberService memberService = new MemberServiceImpl();
		memberService.addMember(new Member(1, "이수홍"));
		memberService.addMember(new Member(2, "박용권"));
		memberService.addMember(new Member(3, "김영한"));
		return memberService;
	}
}
