package com.example.mutsamarket.service;

import com.example.mutsamarket.dto.comment.CommentDto;
import com.example.mutsamarket.dto.comment.CommentListDto;
import com.example.mutsamarket.entity.CommentEntity;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.repository.CommentRepository;
import com.example.mutsamarket.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    // CREATE: 댓글 작성
    public CommentDto createComment(Long itemId, CommentDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity newEntity = new CommentEntity();
        newEntity.setItemId(itemId);
        newEntity.setWriter(dto.getWriter());
        newEntity.setPassword(dto.getPassword());
        newEntity.setContent(dto.getContent());
        newEntity = commentRepository.save(newEntity);
        return CommentDto.fromEntity(newEntity);
    }

    // READ ALL: 댓글 전체 조회
    public Page<CommentListDto> readCommentAll(Long itemId) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("id"));
        Page<CommentEntity> entityPage = commentRepository.findAllByItemId(itemId, pageable);
        return entityPage.map(CommentListDto::fromEntity);
    }

    // READ: 댓글 단위 조회
    public CommentDto readComment(Long commentId) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return CommentDto.fromEntity(optionalComment.get());
    }

    // UPDATE: 댓글 수정
    public CommentDto updateComment(Long commentId, Long itemId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity comment = optionalComment.get();
        if (!comment.getPassword().equals(dto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // DELETE: 댓글 삭제
    public void deleteComment(Long commentId, Long itemId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity comment = optionalComment.get();
        if (!comment.getPassword().equals(dto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        commentRepository.deleteById(commentId);
    }

    // PUT: 답글 등록 기능 구현
    public CommentDto updateCommentReply(Long itemId, Long commentId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        CommentEntity comment = optionalComment.get();

        if (!comment.getItemId().equals(itemId) )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ItemEntity item = optionalItem.get();
        if (!item.getPassword().equals(dto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        comment.setReply(dto.getReply());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }
}