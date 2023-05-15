package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {

}
