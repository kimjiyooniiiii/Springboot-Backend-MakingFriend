package com.knucapstone.rudoori.controller;

import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.AuthenticationRequest;
import com.knucapstone.rudoori.model.dto.AuthenticationResponse;
import com.knucapstone.rudoori.model.dto.Board.BoardCreateRequest;
import com.knucapstone.rudoori.model.dto.Board.BoardResponse;
import com.knucapstone.rudoori.model.dto.UserInfoResponse;
import com.knucapstone.rudoori.service.AuthenticationService;
import com.knucapstone.rudoori.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    @PostMapping()
    public ApiResponse<Boolean> createBoard(
            @RequestBody BoardCreateRequest request
    ){
        return ApiResponse.createSuccess(boardService.createBoard(request));
    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardResponse> getBoard(@RequestParam Long boardId){
        return ApiResponse.createSuccess(boardService.getBoard(boardId));
    }

}
