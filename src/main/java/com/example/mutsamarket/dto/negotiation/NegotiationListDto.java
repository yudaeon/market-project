package com.example.mutsamarket.dto.negotiation;

import com.example.mutsamarket.entity.NegotiationEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NegotiationListDto {
    @NotNull
    private Long id;
    @NotNull
    private Integer suggestedPrice;
    @NotNull
    private String status;

    public static NegotiationListDto fromEntity(NegotiationEntity entity){
        NegotiationListDto dto = new NegotiationListDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        return dto;
    }
}
