package com.icomfortableworld.domain.follow.repository;

import com.icomfortableworld.domain.follow.entity.Follow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.icomfortableworld.domain.follow.entity.QFollow.follow;

@RequiredArgsConstructor
@Repository
public class FollowRepositoryImpl implements FollowRepository {
    private final FollowJpaRepository followJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Follow> findByFollowIdAndFromId(Long followId, Long fromId) {
        return followJpaRepository.findByFollowIdAndFromId(followId, fromId);
    }

    @Override
    public boolean existsByToIdAndFromId(Long fromId, Long toId) {
        return followJpaRepository.existsByToIdAndFromId(fromId, toId);
    }

    @Override
    public Follow save(Follow follow) {
        return followJpaRepository.save(follow);
    }

    @Override
    public void delete(Follow follow) {
        followJpaRepository.delete(follow);
    }

    @Override
    public List<Follow> findByFromId(Long memberId) {
        return followJpaRepository.findByFromId(memberId);
    }

    @Override
    public List<Follow> getFollowByToId(Long memberId) {
        return jpaQueryFactory.selectFrom(follow)
                .where(follow.toId.eq(memberId))
                .fetch();
    }


}