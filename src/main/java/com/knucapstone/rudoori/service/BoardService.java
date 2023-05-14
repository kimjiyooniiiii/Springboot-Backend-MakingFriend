package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.Board.BoardCreateRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.BoardRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Transactional
    public boolean createBoard(BoardCreateRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = httpRequest.getHeader("Authorization").substring(7);
        String jwtUserId = jwtService.extractUserId(accessToken);
        UserInfo userId = userRepository.findById(jwtUserId)
                .orElseThrow();

        Posts post = Posts.builder()
                .userId(userId) // 외래 키 값
                .writer(userId.getNickname()) // 외래 키의 닉네임 값
                .title(request.getTitle())
                .content(request.getContent())
                .like(0)
                .dislike(0)
                .scrap(0)
                .build();

        boardRepository.save(post);

        return true;
    }

    @Transactional
    public BoardResponse getBoard(Long boardId) {
        Posts post = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        return BoardResponse
                .builder()
                .postId(post.getPostId())
                .writer(post.getWriter())
                .title(post.getTitle())
                .content(post.getContent())
                .like(post.getLike())
                .dislike(post.getDislike())
                .scrap(post.getScrap())
                .createdDt(post.getCreatedDt())
                .build();
    }
}
