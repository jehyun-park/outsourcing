package com.icomfortableworld.domain.member.dto.response;

import com.icomfortableworld.domain.member.entity.MemberRoleEnum;
import com.icomfortableworld.domain.member.model.MemberModel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
	private String username;
	private String email;
	private String nickname;
	private String introduction;
	private MemberRoleEnum memberRoleEnum;
	private Long followingCount;

	public static MemberResponseDto from(MemberModel memberModel, Long followingCount) {
		return MemberResponseDto.builder()
			.username(memberModel.getUsername())
			.email(memberModel.getEmail())
			.nickname(memberModel.getNickname())
			.introduction(memberModel.getIntroduction())
			.followingCount(followingCount)
			.memberRoleEnum(memberModel.getMemberRoleEnum())
			.build();
	}

}
