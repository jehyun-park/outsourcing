package com.icomfortableworld.domain.feed.dto.responseDto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedResponseDto {
	private Long feedId;
	private String nickname;
	private String content;

	private List<String> tagNameList = new ArrayList<>();

	public FeedResponseDto(String content, List<String> tagList){
		this.content=content;
		tagNameList.addAll(tagList);
	}
	public  FeedResponseDto(Long feedId, String nickname, String content, List<String> tagList){
		this.feedId=feedId;
		this.nickname=nickname;
		this.content=content;
		tagNameList.addAll(tagList);
	}

}
