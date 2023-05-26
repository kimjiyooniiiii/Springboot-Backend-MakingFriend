package com.knucapstone.rudoori.controller;

import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.Board.BoardRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.model.dto.ScrapResponse;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ApiResponse<List<BoardResponse>> getBoardList(
            @RequestParam("page") int page, @RequestParam("size") int size)
    {
        return ApiResponse.createSuccess(boardService.getBoardList(page, size));
    }


    @PostMapping()
    public ApiResponse<BoardResponse> createBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserInfo userinfo
    ){
        return ApiResponse.createSuccess(boardService.createBoard(request, userinfo));
    }
    @GetMapping("/{boardId}")
    public ApiResponse<BoardResponse> getBoard(@PathVariable("boardId") Long boardId){
        return ApiResponse.createSuccess(boardService.getBoard(boardId));
    }

    @PatchMapping("/{boardId}")
    public ApiResponse<BoardResponse> updateBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody BoardRequest boardRequest,
            @AuthenticationPrincipal UserInfo userinfo
    ) throws Exception {
        return ApiResponse.createSuccess(boardService.updateBoard(boardId, boardRequest ,userinfo));
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Boolean> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserInfo userinfo
    ){
        return ApiResponse.createSuccess(boardService.deleteBoard(boardId ,userinfo));
    }

    @PostMapping("/{boardId}/scrap")
    public ApiResponse<ScrapResponse> createScrapBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserInfo userinfo
    ) {
        return ApiResponse.createSuccess(boardService.createScrapBoard(boardId, userinfo));
    }

    @DeleteMapping("/{boardId}/scrap")
    public ApiResponse<Boolean> deleteScrapBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserInfo userinfo
    ) {
        return ApiResponse.createSuccess(boardService.deleteScrapBoard(boardId, userinfo));
    }

    @GetMapping("/scrap")
    public ApiResponse<List<ScrapResponse>> getScrapBoard(
            @AuthenticationPrincipal UserInfo userinfo
    ) {
        return ApiResponse.createSuccess(boardService.getScrapBoard(userinfo));
    }
}
