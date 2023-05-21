package com.knucapstone.rudoori.model.dto.Board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {

    private String title;
    private String content;
//    private String media;
}