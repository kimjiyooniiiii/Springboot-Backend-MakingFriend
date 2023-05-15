package com.knucapstone.rudoori.model.dto;

import lombok.*;

@Getter
@Setter
public class MentionDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MentionRequest {
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class MentionResponse {
        private String userId;
        private String content;

        @Builder
        public MentionResponse(String userId, String content) {
            this.userId = userId;
            this.content = content;
        }
    }
}
