package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.common.expection.NonSelfException;
import com.knucapstone.rudoori.model.dto.Board.BoardRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.BoardJpaRepository;
import com.knucapstone.rudoori.repository.BoardRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserRepository userRepository;


    @Transactional
    public BoardResponse createBoard(BoardRequest request, UserInfo userinfo) {

//        System.out.println(userinfo.getUserId()); // 요청자의 userId 나옴

        Posts post = Posts.builder()
                .userId(userinfo) // 외래 키 값을 가진 객체
                .writer(userinfo.getNickname()) // 외래 키의 닉네임 값
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

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest, UserInfo userinfo) throws Exception {

        var post = boardRepository.findById(boardId).orElseThrow(); // db 안에 저장된 값
        // boardRequest는 board를 수정 할 값들이 들어가있음 "title":"타이틀","body":"바디"

        // board의 작성자 userId랑 요청한 userId랑 같은 경우 수정 가능
        if (userinfo.getUserId().equals(post.getUserId().getUserId())) {
            post.setTitle(boardRequest.getTitle());
            post.setContent(boardRequest.getContent());
            // media 추가해야함
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

        throw new NonSelfException("자신의 게시글만 수정할 수 있습니다");

    }

    @Transactional
    public boolean deleteBoard(Long boardId, UserInfo userinfo) {
        var post = boardRepository.findById(boardId).orElseThrow(); // db 안에 저장된 값

        // board의 작성자 userId랑 요청한 userId랑 같은 경우 삭제 가능
        if (userinfo.getUserId().equals(post.getUserId().getUserId())) {
            boardRepository.deleteById(post.getPostId());
            return true;
        } else {
            throw new NonSelfException("자신의 게시글만 삭제할 수 있습니다");
        }
    }

    public List<BoardResponse> getBoardList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Posts> post = boardJpaRepository.findPage(size, page);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for(Posts p : post){
            boardResponses.add(BoardResponse.builder()
                            .postId(p.getPostId())
                            .title(p.getTitle())
                            .content(p.getContent())
                            .writer(p.getWriter())
                            .likeCount(p.getLikeCount())
                            .dislikeCount(p.getDislikeCount())
                            .scrap(p.getScrap())
                            .createdDt(p.getCreatedDt())
                    .build());
        }
        return boardResponses;
    }
}
