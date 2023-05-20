package com.knucapstone.rudoori.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity(name = "block_users")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockedId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserInfo userId;
    private String blockedUser;

}
