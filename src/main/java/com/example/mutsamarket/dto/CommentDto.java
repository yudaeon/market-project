package com.example.mutsamarket.dto;


import com.example.mutsamarket.entity.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long item_id;

    @NotBlank(message = "작성자를 입력하세요.")
    private String writer;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    public static CommentDto fromEntity(CommentEntity commentEntity){
        CommentDto dto = new CommentDto();
        dto.setItem_id(commentEntity.getItemId());
        dto.setWriter(commentEntity.getWriter());
        dto.setPassword(commentEntity.getPassword());
        dto.setContent(commentEntity.getContent());
        return dto;
    }
}
