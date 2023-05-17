package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.AuthenticationResponse;
import com.knucapstone.rudoori.model.dto.Board.BoardCreateRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.BoardRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Transactional
    public BoardResponse createBoard(BoardCreateRequest request, @AuthenticationPrincipal UserInfo userinfo) {

        System.out.println(userinfo.getUserId());

        var user = userRepository.findById(userinfo.getUserId())
                .orElseThrow();


        Posts post = Posts.builder()
                .userId(user) // 외래 키 값을 가진 객체
                .writer(user.getNickname()) // 외래 키의 닉네임 값
                .title(request.getTitle())
                .content(request.getContent())
                .likeCount(0)
                .dislikeCount(0)
                .scrap(0)
                .build();


        boardRepository.save(post);

        return BoardResponse.builder()
                .postId(post.getPostId())
                .writer(post.getWriter())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .scrap(post.getScrap())
                .createdDt(post.getCreatedDt())
                .build();
    }

    @Transactional
    public BoardResponse getBoard(Long boardId) {
        System.out.println("---boardId출력");
        System.out.println(boardId);
        Posts post = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        return BoardResponse
                .builder()
                .postId(post.getPostId())
                .writer(post.getWriter())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .scrap(post.getScrap())
                .createdDt(post.getCreatedDt())
                .build();
    }
}
