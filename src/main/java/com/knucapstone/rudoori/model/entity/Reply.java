package com.knucapstone.rudoori.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reply")
public class Reply {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    Long replyId;

    // 부모댓글이 없으면 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Reply parent;

    // 내가 가지고 있는 자식 댓글들
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    List<Reply> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserInfo userId;

    //댓글 내용
    @Column(nullable = false)
    String content;

    @CreationTimestamp
    @Column(name = "created_dt", updatable = false)
    LocalDateTime createdDt;

    @UpdateTimestamp
    @Column(name = "modified_dt")
    LocalDateTime modifiedDt;

    // 자식 댓글 생기면 추가
    public void addChild(Reply child) {
        this.children.add(child);
    }

}