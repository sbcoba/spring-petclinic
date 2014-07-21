package org.springframework.samples.member.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.samples.member.MemberConfig;
import org.springframework.samples.member.MemberService;

public class MemberMainWithSpringTest {
	public static void main(String[] args) {

		AnnotationConfigApplicationContext applicationContext =
				new AnnotationConfigApplicationContext(MemberConfig.class);
		MemberService memberService = applicationContext.getBean(MemberService.class);

		System.out.println(memberService.getMember(1));
		System.out.println(memberService.getMember(2));
		System.out.println(memberService.getMember(3));

		applicationContext.close();
	}
}
