package com.icomfortableworld.domain.feed.service;

import java.util.List;

import com.icomfortableworld.domain.feed.dto.requestDto.FeedRequestDto;
import com.icomfortableworld.domain.feed.dto.responseDto.CommentFeedResponseDto;
import com.icomfortableworld.domain.feed.dto.responseDto.FeedResponseDto;
import com.icomfortableworld.domain.member.entity.MemberRoleEnum;

public interface FeedService {
	void createFeed(FeedRequestDto requestDto, Long memberId);

	FeedResponseDto updateFeed(Long feedId, FeedRequestDto requestDto, Long memberId, MemberRoleEnum memberRole);

	List<FeedResponseDto> getFeedList(String nickname);

	List<FeedResponseDto>  getFeedListWithPage(int page, int size);

	CommentFeedResponseDto getFeed(Long feedId,Long memberId);

	List<FeedResponseDto> getSearchResultFeeds(String type, String q, Long memberId);

	void deleteFeed(Long feedId, Long memberId, MemberRoleEnum authority);
}
