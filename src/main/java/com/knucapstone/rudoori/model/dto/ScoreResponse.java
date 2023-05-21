package com.knucapstone.rudoori.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponse {
    private String opponentNickName;
    private double opponentGrade; // 점수를 받은 사람의 최종 grade

}
