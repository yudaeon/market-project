package com.example.mutsamarket.dto;

import com.example.mutsamarket.entity.NegotiationEntity;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.message.Message;
import org.springframework.web.bind.annotation.PutMapping;

@Data
public class NegotiationDto {
    private Long id;

    @NotNull(message = "제품의 아이디를 입력하세요.")
    private Long itemId;

    @NotNull(message = "제품의 제안 금액을 입력하세요")
    @Min(value = 0, message = "최소금액은 0원 이상입니다.")
    private Long suggestedPrice;

    @NotBlank(message = "제품 상태를 입력하세요.")
    private String status;

    @NotBlank(message = "작성자를 입력하세요.")
    private String writer;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    public static NegotiationDto fromEntity(NegotiationEntity negotiationEntity) {
        NegotiationDto dto = new NegotiationDto();
        dto.setId(negotiationEntity.getId());
        dto.setItemId(negotiationEntity.getItemId());
        dto.setSuggestedPrice(negotiationEntity.getSuggestedPrice());
        dto.setStatus(negotiationEntity.getStatus());
        dto.setWriter(negotiationEntity.getWriter());
        dto.setPassword(negotiationEntity.getPassword());
        return dto;
    }
}