package com.icomfortableworld.jwt.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.icomfortableworld.domain.member.entity.Member;
import com.icomfortableworld.domain.member.entity.MemberRoleEnum;

import lombok.Getter;

@Getter
public class MemberDetailsImpl implements UserDetails {

	private final Member member;

	public MemberDetailsImpl(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		MemberRoleEnum memberRoleEnum = member.getMemberRoleEnum();
		String authority = memberRoleEnum.getAuthority();

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}