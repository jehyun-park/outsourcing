package com.icomfortableworld.domain.follow.service;


import com.icomfortableworld.domain.follow.dto.response.FollowResponseDto;

import java.util.List;

public interface FollowService {
    void followMember(Long fromId, Long toId);
    void unfollowMember(Long followId, Long fromId);

    List<FollowResponseDto> getFollowers(Long memberId);
}
