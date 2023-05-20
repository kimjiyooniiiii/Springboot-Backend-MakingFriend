package com.knucapstone.rudoori.repository;


import com.knucapstone.rudoori.model.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByOpponentId(String opponentId);

    long countByOpponentId(String columnId);
}
