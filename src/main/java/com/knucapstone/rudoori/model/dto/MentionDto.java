package com.knucapstone.rudoori.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MentionDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MentionRequest {

        //{
        //  "content" : "사람이 착해요~~
        //}

        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MentionResponse {
        private String opponentNickName;
        private String content;

    }
}
