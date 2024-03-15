package com.icomfortableworld.domain.follow.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private Long followerId;

    public FollowResponseDto(Long followerId) {
        this.followerId = followerId;
    }
}