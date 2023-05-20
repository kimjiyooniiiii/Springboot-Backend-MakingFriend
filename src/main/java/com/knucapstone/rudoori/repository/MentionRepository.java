package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentionRepository extends JpaRepository<Mention, Long> {
    List<Mention> findAllByUserId(UserInfo userId);

}
