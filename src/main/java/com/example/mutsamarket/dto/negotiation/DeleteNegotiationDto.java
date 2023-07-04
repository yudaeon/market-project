package com.example.mutsamarket.dto.negotiation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteNegotiationDto {
    @NotBlank
    private String writer;
    @NotBlank
    private String password;
}
