package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.model.dto.ReplyDto;
import com.knucapstone.rudoori.model.dto.ScrapResponse;
import com.knucapstone.rudoori.model.entity.Posts;
import com.knucapstone.rudoori.model.entity.Reply;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.model.entity.UserScraps;
import com.knucapstone.rudoori.repository.BoardJpaRepository;
import com.knucapstone.rudoori.repository.BoardRepository;
import com.knucapstone.rudoori.repository.ReplyRepository;
import com.knucapstone.rudoori.model.dto.Board.BoardRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.repository.ScrapRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ScrapRepository scrapRepository;


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

    // 게시글과 댓글 열람
    @Transactional
    public BoardResponse getBoard(Long boardId) {
        Posts post = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);

        Optional<Posts> post2 = boardRepository.findById(boardId);

        // 해당 게시글의 댓글 모두 불러오기
        List<Reply> replies = replyRepository.findAllByPost(post2);

        // 댓글 정렬하기
        List<ReplyDto.ReplyGroup> groups = sortReply(boardId, replies);

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
                .replyGroup(groups)
                .build();
    }

    // 댓글 묶음 받는 메소드
    private List<ReplyDto.ReplyGroup> sortReply(Long boardId, List<Reply> replies) {

        // 해당 게시글의 모든 '부모 댓글 + 자식 댓글' 묶음
        List<ReplyDto.ReplyGroup> groups = new ArrayList<>();

        for (Reply reply : replies) {
            // 자신이 부모인 경우
            if (reply.getParent() == null) {

                // 자식 댓글들 모두 불러오기
                List<Reply> childrenList = reply.getChildren();

                // 모든 자식댓글들의 '닉네임,댓글내용' 담기
                List<List<String>> allChild = new ArrayList<>();

                for (Reply child : childrenList) {
                    // 자식 댓글의 '닉네임, 댓글내용' 담기
                    List<String> oneChild = new ArrayList<>();

                    oneChild.add(child.getUserId().getNickname());
                    oneChild.add(child.getContent());

                    allChild.add(oneChild);
                }

                // 부모 댓글과 자식 댓글 묶기
                ReplyDto.ReplyGroup oneGroup = ReplyDto.ReplyGroup.builder()
                        .parentNickname(reply.getUserId().getNickname())
                        .parentContent(reply.getContent())
                        .children(allChild)
                        .build();

                groups.add(oneGroup);

            }
        }
        return groups;

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

        throw new RuntimeException("자신의 게시글만 수정할 수 있습니다");

    }

    @Transactional
    public boolean deleteBoard(Long boardId, UserInfo userinfo) {
        var post = boardRepository.findById(boardId).orElseThrow(); // db 안에 저장된 값

        // board의 작성자 userId랑 요청한 userId랑 같은 경우 삭제 가능
        if (userinfo.getUserId().equals(post.getUserId().getUserId())) {
            boardRepository.deleteById(post.getPostId());
            return true;
        } else {
            throw new RuntimeException("자신의 게시글만 삭제할 수 있습니다");
        }
    }

    public List<BoardResponse> getBoardList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Posts> post = boardJpaRepository.findPage(size, page);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Posts p : post) {
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

    // 부모 댓글 생성
    @Transactional
    public ReplyDto.CreateReplyResponse createParentReply(Long boardId, UserInfo userInfo, ReplyDto.CreateReplyRequest request) {

        // 게시글 유효성 확인
        var post = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("게시글이 존재하지 않습니다."));

        // 작성자 유효성 확인
        var user = userRepository.findByUserId(userInfo.getUserId()).orElseThrow(() -> new NullPointerException("작성자가 잘못되었습니다."));

        // 댓글 객체 생성
        Reply newReply = Reply.builder()
                .post(post)
                .userId(user)
                .content(request.getContent())
                .build();

        // 댓글 저장
        replyRepository.save(newReply);

        // 결과 리턴
        return ReplyDto.CreateReplyResponse.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .content(request.getContent())
                .build();
    }

    // 자식 댓글 생성
    @Transactional
    public ReplyDto.CreateReplyResponse createChildReply(Long boardId, Long parentId, UserInfo userInfo, ReplyDto.CreateReplyRequest request) {
        // 게시글 유효성 확인
        var post = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("게시글이 존재하지 않습니다."));

        // 작성자 유효성 확인
        var user = userRepository.findByUserId(userInfo.getUserId()).orElseThrow(() -> new NullPointerException("작성자가 잘못되었습니다."));

        // 부모 댓글 유효성 확인
        var parentReply = replyRepository.findById(parentId).orElseThrow(() -> new NullPointerException("부모 댓글이 존재하지 않습니다."));

        Reply newReply = Reply.builder()
                .post(post)
                .userId(user)
                .content(request.getContent())
                .parent(parentReply)
                .build();

        parentReply.addChild(newReply);

        replyRepository.save(newReply);

        return ReplyDto.CreateReplyResponse.builder()
                .parentId(parentId)
                .postId(post.getPostId())
                .content(request.getContent())
                .userId(user.getUserId())
                .build();

    }

    @Transactional
    public ScrapResponse createScrapBoard(Long postId, UserInfo userinfo) {
        var post = boardRepository.findById(postId).orElseThrow(() -> new NullPointerException("존재하지 않는 게시글입니다."));
        var user = userRepository.findById(userinfo.getUserId()).orElseThrow(() -> new NullPointerException("존재하지 않는 사용자입니다."));

        Optional<UserScraps> scrap = scrapRepository.findByUserIdAndPostId(userinfo, post);
        if(scrap.isPresent()){
            throw new RuntimeException("이미 스크랩한 게시글 입니다.");
        }
        else {
            UserScraps userScraps = UserScraps.builder()
                    .userId(user)
                    .postId(post)
                    .build();

            scrapRepository.save(userScraps);

            return ScrapResponse.builder()
                    .postId(userScraps.getPostId().getPostId())
                    .userId(userScraps.getUserId().getUserId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .writer(post.getWriter())
                    .build();
        }
    }


    @Transactional
    public boolean deleteScrapBoard(Long postId, UserInfo userinfo) {
        var post = boardRepository.findById(postId).orElseThrow(() -> new NullPointerException("존재하지 않는 게시글입니다."));
         Optional<UserScraps> scrap = scrapRepository.findByUserIdAndPostId(userinfo, post);

        if(scrap.isEmpty()){
            throw new RuntimeException("스크랩 되지 않은 게시글 입니다.");
        }
        scrapRepository.delete(scrap.get());
        return true;
    }

    @Transactional
    public List<ScrapResponse> getScrapBoard(UserInfo userinfo) {
        // 자신이 한 scrap 정보들을 가져옴
        // userScraps는 scrapId, userId, postId, createdAt을 가짐
        List<UserScraps> userScraps = scrapRepository.findByUserId(userinfo);
        List<ScrapResponse> scrapList  = new ArrayList<>();

        for (UserScraps s : userScraps) {

            // for문마다 repository 새로 불러오는거 최적화 or 다른 방법 구상 필요!!
            // join 해야하나 scrap이랑??
            var post = boardRepository.findById(s.getPostId().getPostId()).orElseThrow(() -> new NullPointerException("존재하지 않는 게시글입니다."));

            scrapList.add(
                    ScrapResponse.builder()
                            .postId(s.getPostId().getPostId())
                            .userId(s.getUserId().getUserId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .writer(post.getWriter())
                            .build());
        }

        return scrapList;
    }
}
