package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.comment.CommentDto;
import com.example.mutsamarket.dto.comment.CommentListDto;
import com.example.mutsamarket.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService commentService;

    // POST /items/{itemId}/comments 댓글 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> createComment(
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody CommentDto dto
    ) {
        log.info(dto.toString());
        commentService.createComment(itemId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 등록되었습니다.");
        return ResponseEntity.ok(responseBody);
    }

    // GET /items/{itemId}/comments 댓글 전체 조회
    @GetMapping
    public Page<CommentListDto> readAllComments(@PathVariable("itemId") Long itemId) {
        return commentService.readCommentAll(itemId);
    }

    // GET /items/{itemId}/comments/{commentId} 댓글 단위 조회
    @GetMapping("/{commentId}")
    public CommentDto readComment(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.readComment(commentId);
    }

    // PUT /items/{itemId}/comments/{commentId} 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> updateComment(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        log.info(dto.toString());
        commentService.updateComment(itemId, commentId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 수정되었습니다.");
        return ResponseEntity.ok(responseBody);
    }

    // DELETE /items/{itemId}/comments/{commentId} 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        log.info(dto.toString());
        commentService.deleteComment(commentId, itemId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글을 삭제했습니다.");
        return ResponseEntity.ok(responseBody);
    }

    // PUT /items/{itemId}/comments/{commentId}/reply 답글 등록 구현하기
    @PutMapping("/{commentId}/reply")
    public ResponseEntity<Map<String, String>> createReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentDto dto
    ) {
        log.info(dto.toString());
        commentService.createReply(itemId, commentId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "답글이 등록되었습니다.");
        return ResponseEntity.ok(responseBody);
    }
}