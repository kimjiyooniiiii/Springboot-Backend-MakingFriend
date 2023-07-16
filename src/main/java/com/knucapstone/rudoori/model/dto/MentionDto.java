package com.knucapstone.rudoori.model.dto;

import lombok.*;

public class MentionDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MentionRequest {

        //{
        //  "content" : "사람이 착해요~~
        //}

        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class MentionResponse {
        private String opponentNickName;
        private String content;
    }
}
