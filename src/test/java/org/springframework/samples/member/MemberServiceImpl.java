package org.springframework.samples.member;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
	private Set<Member> members = new HashSet<Member>();

	@Override
	public Member getMember(int id) {
		for (Member member : members) {
			if (id == member.getId()) {
				return member;
			}
		}
		return null;
	}

	@Override
	public Member addMember(Member member) {
		memberNotFoundException(member);
		members.add(member);
		return member;
	}

	@Override
	public Member deleteMember(int id) {
		Member member = getMember(id);
		memberNotFoundException(member);
		return deleteMember(member);
	}

	@Override
	public Member deleteMember(Member member) {
		memberNotFoundException(member);
		members.remove(member);
		return member;
	}

	@Override
	public Member updateMember(Member member) {
		memberNotFoundException(member);
		if (members.contains(member)) {
			deleteMember(member);
		}
		return addMember(member);
	}

	private void memberNotFoundException(Member member) {
		if (member == null) {
			throw new NullPointerException("member (" + member + ")의 사용자 정보를 찾을 수 없습니다.");
		}
	}
}
