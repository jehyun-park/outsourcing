package com.icomfortableworld.domain.feed.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.icomfortableworld.domain.comment.model.CommentModel;
import com.icomfortableworld.domain.feed.entity.Feed;
import com.icomfortableworld.domain.feed.model.FeedModel;
import com.icomfortableworld.domain.member.entity.MemberRoleEnum;

public interface FeedRepository {
	Feed save(Feed feed);

	FeedModel findByIdOrElseThrow(Long feedId);

	Optional<FeedModel> findById(Long feedId);

	FeedModel update(Long feedId, Long memberId, String content, MemberRoleEnum memberRole);

	List<FeedModel> findAll();

	List<FeedModel> findAllById(Long feedId);

	void deleteById(Long feedId, Long memberId, MemberRoleEnum authority);

	List<FeedModel> findByMemberId(Long toId);

	List<Feed> getFeedList(String nickname);

	List<Feed> getFeedListWithPage(long offset, int pageSize);

}
