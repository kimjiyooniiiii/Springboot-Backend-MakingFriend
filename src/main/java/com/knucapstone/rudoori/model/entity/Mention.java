package com.knucapstone.rudoori.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mention")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "mentionId")
public class Mention {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "mention_id")
    private Long mentionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userId;

    @Column(length = 50)
    private String content;

}
