package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.negotiation.NegotiationDto;
import com.example.mutsamarket.dto.negotiation.NegotiationListDto;
import com.example.mutsamarket.dto.item.ResponseDto;
import com.example.mutsamarket.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Valid
@RequestMapping("/items")
@RequiredArgsConstructor
public class NegotiationController {
    private final NegotiationService service;

    //구매 제안 등록
    //POST /items/{itemId}/proposals
    @PostMapping("/{itemId}/proposals")
    public ResponseDto createNegotiation(
            @PathVariable("itemId") Long itemId,
            @RequestBody NegotiationDto dto) {
        service.createNegotiation(itemId, dto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("구매 제안이 등록되었습니다.");
        return responseDto;
    }

    // 구매제안 조회 (작성자, 비밀번호 일치시)
    //GET /items/{itemId}/proposals?writer=jeeho.edu&password=qwerty1234&page=1
    @GetMapping("/{itemId}/proposals")
    public Page<NegotiationListDto> readNegotiation(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "writer") String writer,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "page") Integer page){
        return service.readNegotiation(itemId, writer, password,page);
    }

    // PUT /items/{itemId}/proposals/{proposalId} 제안 수정
    // 제안 수정, 상태 변경, 구매 확정 기능 추가
    @PutMapping("/{itemId}/proposals/{proposalId}")
    public ResponseEntity<ResponseDto> updateNegotiation(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody NegotiationDto negotiationDto
    ) {
        // 제안수정 기능 추가
        if (negotiationDto.getStatus() == null) {
            this.service.updateNegotiation(itemId, proposalId, negotiationDto);
            ResponseDto response = new ResponseDto();
            response.setMessage("제안이 수정되었습니다.");
            return ResponseEntity.ok(response);

        }
        //구매 확정 기능 수정
        else if (negotiationDto.getStatus() != null && negotiationDto.getStatus().equals("확정")) {
            this.service.confirmNegotiation(itemId, proposalId, negotiationDto);
            ResponseDto response = new ResponseDto();
            response.setMessage("구매가 확정되었습니다.");
            return ResponseEntity.ok(response);
        }
        //수락, 거절 상태변경 기능 추가
        else {
            this.service.acceptNegotiation(itemId, proposalId, negotiationDto);
            ResponseDto response = new ResponseDto();
            response.setMessage("제안의 상태가 변경되었습니다.");
            return ResponseEntity.ok(response);
        }
    }

    //DELETE /items/{itemId}/proposals/{proposalId}
    //구매 제안 삭제 기능 추가
    @DeleteMapping("/{itemId}/proposals/{proposalId}")
    public ResponseDto deleteNegotiation(@PathVariable("itemId") Long itemId,
                                         @PathVariable("proposalId")Long proposalId,
                                         @RequestBody NegotiationDto dto){
        this.service.deleteNegotiation(itemId, proposalId, dto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("제안을 삭제했습니다.");
        return responseDto;
    }
}