package com.knucapstone.rudoori.repository;

import com.knucapstone.rudoori.model.entity.Posts;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardJpaRepository {
    private final EntityManager entityManager;

    public List<Posts> findPage(int size, int page){
        return entityManager.createQuery("select p from Posts p", Posts.class)
                .setFirstResult(page)
                .setMaxResults(size)
                .getResultList();
    }

}
