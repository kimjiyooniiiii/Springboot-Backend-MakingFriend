package com.knucapstone.rudoori.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapResponse {

    private Long postId;
    private String userId;

    private String title;
    private String content;
    private String writer;
}
