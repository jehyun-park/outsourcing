package com.icomfortableworld.domain.follow.repository;

import com.icomfortableworld.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FollowJpaRepository extends JpaRepository<Follow, Long> {
    boolean existsByToIdAndFromId(Long fromId, Long toId);
    Optional<Follow> findByFollowIdAndFromId(Long followId, Long fromId);
    List<Follow> findByFromId(Long memberId);

    List<Follow> getFollowByToId(Long memberId);
}
