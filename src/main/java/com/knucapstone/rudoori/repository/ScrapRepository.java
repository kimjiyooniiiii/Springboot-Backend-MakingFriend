package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.model.entity.UserScraps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<UserScraps, Long> {

    List<UserScraps> findByUserId(UserInfo userId);

    Optional<UserScraps> findByUserIdAndPostId(UserInfo userInfo, Posts postId);

}
