package com.knucapstone.rudoori.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentionRequest {

    //{
    //  "content" : "사람이 착해요~~
    //}

    private String content;
}