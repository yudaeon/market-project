package com.example.mutsamarket.dto.negotiation;

import com.example.mutsamarket.entity.NegotiationEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class NegotiationDto {
    @NotNull
    private Long itemId;
    @NotNull
    private Integer suggestedPrice;
    @NotBlank
    private String status;
    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    public static NegotiationDto fromEntity(NegotiationEntity entity){
        NegotiationDto dto = new NegotiationDto();
        dto.setItemId(entity.getItemId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}






