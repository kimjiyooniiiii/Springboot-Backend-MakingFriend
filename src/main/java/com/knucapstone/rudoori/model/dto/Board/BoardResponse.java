package com.knucapstone.rudoori.model.dto.Board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    // 댓글, media 추가해야 함

    private Long postId;
    private String title;
    private String content;
    private String writer;
    private int likeCount;
    private int dislikeCount;
    private int scrap;
    private LocalDateTime createdDt;
//    private String media;
    
}
