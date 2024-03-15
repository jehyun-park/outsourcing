package com.icomfortableworld.domain.member.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.icomfortableworld.common.entity.Timestamped;
import com.icomfortableworld.domain.member.dto.request.SignupRequestDto;
import com.icomfortableworld.domain.member.model.MemberModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@SQLDelete(sql = "update members set deleted_date = NOW() where id = ?")
@SQLRestriction(value = "deleted_date is NULL")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false, length = 10, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Column(length = 40)
	private String introduction;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private MemberRoleEnum memberRoleEnum;


    public static Member of(SignupRequestDto signupRequestDto, String password, MemberRoleEnum memberRoleEnum) {
		return Member.builder()
			.username(signupRequestDto.getUsername())
			.email(signupRequestDto.getEmail())
			.nickname(signupRequestDto.getNickname())
			.password(password)
			.introduction(signupRequestDto.getIntroduction())
			.memberRoleEnum(memberRoleEnum)
			.build();
	}

	public MemberModel toModel() {
		return MemberModel.builder()
			.memberId(memberId)
			.username(username)
			.email(email)
			.nickname(nickname)
			.password(password)
			.introduction(introduction)
			.memberRoleEnum(memberRoleEnum)
			.createdDate(this.getCreatedDate())
			.build();
	}

	public void updateNickname(String newNickname) {
		this.nickname = newNickname;
	}

	public void updateIntroduction(String newIntroduction) {
		this.introduction = newIntroduction;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}
}