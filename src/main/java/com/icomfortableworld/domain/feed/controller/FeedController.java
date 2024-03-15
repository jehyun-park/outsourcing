package com.icomfortableworld.domain.feed.controller;

import java.util.List;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icomfortableworld.common.dto.CommonResponseDto;
import com.icomfortableworld.domain.feed.dto.requestDto.FeedRequestDto;
import com.icomfortableworld.domain.feed.dto.responseDto.CommentFeedResponseDto;
import com.icomfortableworld.domain.feed.dto.responseDto.FeedResponseDto;
import com.icomfortableworld.domain.feed.service.FeedService;
import com.icomfortableworld.jwt.security.MemberDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version-1/feeds")
public class FeedController {
	private final FeedService feedService;

	@PostMapping
	public ResponseEntity<CommonResponseDto<Void>> createFeed(@Valid @RequestBody FeedRequestDto requestDto,
		@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
		feedService.createFeed(requestDto, memberDetails.getMember().getMemberId());
		return CommonResponseDto.of(HttpStatus.OK, "피드 작성 성공", null);
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<CommonResponseDto<FeedResponseDto>> updateFeed(@Valid @PathVariable Long feedId,
		@RequestBody FeedRequestDto requestDto,
		@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
		FeedResponseDto responseDto = feedService.updateFeed(feedId, requestDto,
			memberDetails.getMember().getMemberId(), memberDetails.getMember().getMemberRoleEnum());
		return CommonResponseDto.of(HttpStatus.OK, "피드 수정 성공", responseDto);
	}

	@GetMapping
	public List<FeedResponseDto> getBoardLists(
			@Nullable @RequestParam(required = false) String username,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		if (username != null) {
			return feedService.getFeedList(username);
		} else {
			return feedService.getFeedListWithPage(page, size);
		}
	}

	@GetMapping("/{feedId}")
	public ResponseEntity<CommonResponseDto<CommentFeedResponseDto>> getFeed(@PathVariable Long feedId,
		@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
		CommentFeedResponseDto responseDto = feedService.getFeed(feedId, memberDetails.getMember().getMemberId());
		return CommonResponseDto.of(HttpStatus.OK, "피드 조회 성공", responseDto);
	}

	@GetMapping("/search/{type}")
	public ResponseEntity<CommonResponseDto<List<FeedResponseDto>>> getSearchResultFeeds(
		@PathVariable String type, @RequestParam String q,
		@AuthenticationPrincipal MemberDetailsImpl memberDetails){
		List<FeedResponseDto> responseDto = feedService.getSearchResultFeeds(type, q,
			memberDetails.getMember().getMemberId());
		return CommonResponseDto.of(HttpStatus.OK, 	q+"에 대한 검색 결과", responseDto);
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<CommonResponseDto<Void>> deleteFeed(@PathVariable Long feedId,
		@AuthenticationPrincipal MemberDetailsImpl memberDetails){
		feedService.deleteFeed(feedId, memberDetails.getMember().getMemberId(), memberDetails.getMember().getMemberRoleEnum());
		return CommonResponseDto.of(HttpStatus.OK, "피드 삭제 성공", null);
	}
}
