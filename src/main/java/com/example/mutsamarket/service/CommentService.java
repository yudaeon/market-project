package com.example.mutsamarket.service;

import com.example.mutsamarket.dto.CommentDto;
import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.entity.CommentEntity;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.repository.CommentRepository;
import com.example.mutsamarket.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    //CREATE 댓글 등록
    public ResponseDto createComments(Long itemId, CommentDto commentDto){
        Optional<ItemEntity> checkItem = itemRepository.findById(itemId);
        if (checkItem.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity entity = new CommentEntity();
        entity.setItemId(commentDto.getItem_id());
        entity.setWriter(commentDto.getWriter());
        entity.setPassword(commentDto.getPassword());
        entity.setContent(commentDto.getContent());
        entity.setReply(commentDto.getReply());
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 등록되었습니다.");
        return response;
    }

    //READ ALL 댓글 전체 조회
    public Page<CommentDto> readCommentAll(Long itemId, Integer page, Integer limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<CommentEntity> entities = commentRepository.findByItemId(itemId, pageable);
        Page<CommentDto> commentDtos = entities.map(CommentDto::fromEntity);
        return commentDtos;
    }

    //READ 댓글 단위 조회
    public CommentDto readComment(Long itemId){
        Optional<CommentEntity> optionalComment = commentRepository.findById(itemId);
    return CommentDto.fromEntity(optionalComment.get());
    }

    //UPDATE 댓글 수정
    public ResponseDto updateComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<CommentEntity> updateComment = commentRepository.findById(commentId);
        if (updateComment.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (updateComment.isPresent()) {
            CommentEntity targetComment = updateComment.get();
            targetComment.setContent(dto.getContent());
            ResponseDto response = new ResponseDto();
            response.setMessage("댓글이 수정되었습니다.");

            return response;
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
        //DELETE 댓글 삭제
    public void deleteComment(Long itemId, CommentDto dto){
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(itemId);

        if (optionalCommentEntity.isPresent()){
            CommentEntity target = optionalCommentEntity.get();
            if (target.getPassword().equals(dto.getPassword())) commentRepository.deleteById(itemId);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
