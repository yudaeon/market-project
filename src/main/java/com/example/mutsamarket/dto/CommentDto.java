package com.example.mutsamarket.dto;


import com.example.mutsamarket.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long item_id;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static CommentDto fromEntity(CommentEntity commentEntity){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setItem_id(commentEntity.getItemId());
        commentDto.setWriter(commentEntity.getWriter());
        commentDto.setPassword(commentEntity.getPassword());
        commentDto.setContent(commentDto.getContent());
        commentDto.setReply(commentDto.getReply());
        return commentDto;
    }
}
