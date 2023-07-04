package com.example.mutsamarket.dto.comment;

import com.example.mutsamarket.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentListDto {
    private Long id;
    private String content;
    private String reply; //답글

    public static CommentListDto fromEntity(CommentEntity entity){
        CommentListDto dto = new CommentListDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        return dto;
    }
}
