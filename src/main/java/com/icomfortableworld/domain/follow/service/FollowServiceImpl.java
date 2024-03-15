package com.icomfortableworld.domain.follow.service;

import com.icomfortableworld.domain.follow.dto.response.FollowResponseDto;
import com.icomfortableworld.domain.follow.entity.Follow;
import com.icomfortableworld.domain.follow.repository.FollowRepository;
import com.icomfortableworld.domain.member.model.MemberModel;
import com.icomfortableworld.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Override
    public void followMember(Long fromId, Long toId) {
        MemberModel follower = memberRepository.findByIdOrElseThrow(fromId);
        MemberModel following = memberRepository.findByIdOrElseThrow(toId);
        if (followRepository.existsByToIdAndFromId(toId, fromId)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }
        Follow follow = new Follow();
        follow.setToId(toId);
        follow.setFromId(fromId);
        follow.setCreatedDate(LocalDateTime.now());
        followRepository.save(follow);
    }


    @Override
    public void unfollowMember(Long followId, Long fromId) {
        MemberModel follower = memberRepository.findByIdOrElseThrow(fromId);
        Follow follow = followRepository.findByFollowIdAndFromId(followId, fromId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팔로워를 찾을 수 없습니다."));
        if (!follow.getFromId().equals(fromId)) {
            throw new IllegalArgumentException("자신의 팔로우만 취소할 수 있습니다.");
        }
        followRepository.delete(follow);
    }

    @Override
    public List<FollowResponseDto> getFollowers(Long memberId) {
        List<Follow> followers = followRepository.getFollowByToId(memberId);
        return followers.stream()
                .map(follow -> FollowResponseDto.builder()
                        .followerId(follow.getFromId())
                        .build())
                .collect(Collectors.toList());
    }
}