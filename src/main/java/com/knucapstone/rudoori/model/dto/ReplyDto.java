package com.knucapstone.rudoori.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class ReplyDto {

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReplyRequest {

        String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CreateReplyResponse {

        Long postId;
        Long parentId;
        String userId;
        String content;

    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyGroup {

        String parentNickname;
        String parentContent;

        List<List<String>> children;
    }


}
