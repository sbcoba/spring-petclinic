package org.springframework.samples.member;

public interface MemberService {
	Member getMember(int id);

	Member addMember(Member member);

	Member deleteMember(int id);

	Member deleteMember(Member member);

	Member updateMember(Member member);
}