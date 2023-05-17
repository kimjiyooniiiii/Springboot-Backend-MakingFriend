package com.knucapstone.rudoori.token;

import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value= """
select t from Token t inner join UserInfo u on t.user = u.userId
where u.userId = :id and (t.expired = false or t.revoked = false)
""")
        List<Token> findAllValidTokenByUser(@Param("id") String id);
        Optional<Token> findByToken(String token);
}
