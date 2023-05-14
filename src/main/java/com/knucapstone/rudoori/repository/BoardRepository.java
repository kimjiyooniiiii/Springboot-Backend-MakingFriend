package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Posts, Long> {
    Optional<Posts> findById(Long postId);

}
