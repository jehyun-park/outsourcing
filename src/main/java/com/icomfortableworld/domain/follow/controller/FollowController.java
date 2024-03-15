package com.icomfortableworld.domain.follow.controller;

import com.icomfortableworld.common.dto.CommonResponseDto;
import com.icomfortableworld.domain.follow.dto.request.FollowRequestDto;
import com.icomfortableworld.domain.follow.dto.request.UnfollowRequestDto;
import com.icomfortableworld.domain.follow.dto.response.FollowResponseDto;
import com.icomfortableworld.domain.follow.service.FollowService;
import com.icomfortableworld.jwt.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/version-1/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<Void>> followMember(
            @Valid @RequestBody FollowRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Long fromId = memberDetails.getMember().getMemberId();
        followService.followMember(fromId, requestDto.getToId());
        return CommonResponseDto.of(HttpStatus.OK, "팔로우하였습니다.", null);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponseDto<Void>> unfollowMember(
            @Valid @RequestBody UnfollowRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Long fromId = memberDetails.getMember().getMemberId();
        followService.unfollowMember(requestDto.getFollowId(), fromId);
        return CommonResponseDto.of(HttpStatus.OK, "팔로우를 취소하였습니다.", null);
    }
    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(
            @RequestParam Long memberId) {
        List<FollowResponseDto> followers = followService.getFollowers(memberId);
        return ResponseEntity.ok(followers);
    }
}
