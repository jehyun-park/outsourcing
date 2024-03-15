package com.icomfortableworld.domain.feed.repository;

import java.util.List;
import java.util.Optional;

import com.icomfortableworld.domain.feed.entity.QFeed;
import com.icomfortableworld.domain.member.entity.QMember;
import com.icomfortableworld.domain.tag.entity.QTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import com.icomfortableworld.domain.comment.repository.CommentRepository;
import com.icomfortableworld.domain.feed.entity.Feed;
import com.icomfortableworld.domain.feed.model.FeedModel;
import com.icomfortableworld.domain.member.entity.MemberRoleEnum;
import com.icomfortableworld.domain.feed.exception.CustomFeedException;
import com.icomfortableworld.domain.feed.exception.FeedErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class FeedRepositoryImpl implements FeedRepository {
	private final FeedJpaRepository feedJpaRepository;
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public Feed save(Feed feed) {
		return feedJpaRepository.save(feed);
	}

	@Override
	public FeedModel findByIdOrElseThrow(Long feedId) {
		return findById(feedId).orElseThrow(
			() -> new CustomFeedException(FeedErrorCode.FEED_ERROR_CODE_NOT_FOUND)
		);
	}

	@Override
	public Optional<FeedModel> findById(Long feedId) {
		return feedJpaRepository.findById(feedId).map(Feed::toModel);
	}

	@Override
	public FeedModel update(Long feedId, Long memberId, String content, MemberRoleEnum authority) {
		Feed feed = feedJpaRepository.findById(feedId).orElseThrow(
			() -> new CustomFeedException(FeedErrorCode.FEED_ERROR_CODE_NOT_FOUND)
		);
		if (!feed.getMemberId().equals(memberId) && authority!=MemberRoleEnum.ADMIN) {
			throw new CustomFeedException(FeedErrorCode.FEED_ERROR_CODE_ID_MISMATCH);
		}
		feed.update(content);
		return feed.toModel();
	}

	@Override
	public List<FeedModel> findAll() {
		return feedJpaRepository.findAll().stream().map(Feed::toModel).toList();
	}

	@Override
	public List<FeedModel> findAllById(Long feedId) {
		return feedJpaRepository.findById(feedId).stream().map(Feed::toModel).toList();
	}


	@Override
	public void deleteById(Long feedId, Long memberId, MemberRoleEnum authority) {
		Feed feed = feedJpaRepository.findById(feedId).orElseThrow(
			() -> new CustomFeedException(FeedErrorCode.FEED_ERROR_CODE_NOT_FOUND)
		);
		if (!feed.getMemberId().equals(memberId) && authority!=MemberRoleEnum.ADMIN) {
			throw new CustomFeedException(FeedErrorCode.FEED_ERROR_CODE_ID_MISMATCH);
		}
		feedJpaRepository.deleteById(feedId);
	}

	@Override
	public List<FeedModel> findByMemberId(Long toId) {
		return feedJpaRepository.findAllByMemberId(toId).stream().map(Feed::toModel).toList();
	}

	@Override
	public List<Feed> getFeedList(String nickname) {
		QFeed feed = QFeed.feed;
		QMember member = QMember.member;
		return jpaQueryFactory.select(feed)
				.from(feed)
				.join(member)
				.on(feed.memberId.eq(member.memberId))
				.where(member.username.eq(nickname))
				.fetch();
	}

	@Override
	public List<Feed> getFeedListWithPage(long offset, int pageSize) {
		QFeed feed = QFeed.feed;

		return jpaQueryFactory.select(feed)
				.from(feed)
				.offset(offset)
				.limit(pageSize)
				.fetch();
	}

}
