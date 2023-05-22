package com.knucapstone.rudoori.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    
    // media 관련 추가해야 함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String content;
    private String writer;
    private int likeCount;
    private int dislikeCount;
    private int scrap;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDt;

    @UpdateTimestamp
    private LocalDateTime modifiedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo userId;

    @OneToMany(mappedBy = "post", orphanRemoval = true)   //orphanRemoval : 게시글 삭제시, 댓글 자동삭제
    private List<Reply> replies = new ArrayList<>();
}
