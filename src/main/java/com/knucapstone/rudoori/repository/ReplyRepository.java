package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository  extends JpaRepository<Reply, Long> {

    List<Reply> findAllByPost(Posts post);
}
