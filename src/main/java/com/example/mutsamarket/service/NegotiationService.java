package com.example.mutsamarket.service;

import com.example.mutsamarket.dto.negotiation.NegotiationDto;
import com.example.mutsamarket.dto.negotiation.NegotiationListDto;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.entity.NegotiationEntity;
import com.example.mutsamarket.repository.ItemRepository;
import com.example.mutsamarket.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final ItemRepository itemRepository;

    //구매 제안 등록
    //구매제안이 등록되면 "제안" 상태로 변환
    public NegotiationDto createNegotiation(Long itemId, NegotiationDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        NegotiationEntity entity = new NegotiationEntity();
        entity.setItemId(itemId);
        entity.setSuggestedPrice(dto.getSuggestedPrice());
        entity.setWriter(dto.getWriter());
        entity.setStatus("제안");
        entity.setPassword(dto.getPassword());

        return NegotiationDto.fromEntity(negotiationRepository.save(entity));
    }

    //구매 제안 조회
    public Page<NegotiationListDto> readNegotiation(Long itemId, String writer, String password, Integer page) {
        Pageable pageable = PageRequest.of(page  -1, 25);

        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!negotiationRepository.existsByItemId(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    //대상 물품의 주인 - 작성자, 비밀번호 첨부 해야함
    if (itemRepository.existsAllByIdAndWriterLikeAndPasswordLike(itemId, writer, password)) {
        Page<NegotiationEntity> negotiationEntityPage = negotiationRepository.findAllByItemId(itemId, pageable);
        return negotiationEntityPage.map(NegotiationListDto::fromEntity);
    }

    //구매제안자 - 이름, 비밀번호 일치시 확인 가능 + 페이지 기능 지원
    if (negotiationRepository.existsAllByItemIdAndWriterLikeAndPasswordLike(itemId, writer, password)) {
        Page<NegotiationEntity> negotiationEntityPage = negotiationRepository.findAllByItemIdAndWriterAndPasswordLike(itemId, writer, password, pageable);
        return negotiationEntityPage.map(NegotiationListDto::fromEntity);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
}


    //상품 제안 수정 - 작성자와 비밀번호 첨부해야함
    public NegotiationDto updateNegotiation(Long itemId, Long proposalId, NegotiationDto dto) {
       Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
       if (optionalNegotiation.isEmpty())
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);

       NegotiationEntity negotiation = optionalNegotiation.get();
        if (!negotiation.getItemId().equals(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        //작성자 비밀번호 일치 확인
        if(!negotiation.getWriter().equals(dto.getWriter()) || (!negotiation.getPassword().equals(dto.getPassword())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        negotiation.setSuggestedPrice(dto.getSuggestedPrice());
            return NegotiationDto.fromEntity(negotiationRepository.save(negotiation));
    }
    //구매 수락
    public NegotiationDto acceptNegotiation(Long itemId, Long proposalId, NegotiationDto dto){
        Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        //아이디 확인
        NegotiationEntity negotiation = optionalNegotiation.get();
        if (!negotiation.getItemId().equals(itemId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        //비밀번호 확인
        ItemEntity item = optionalItem.get();
        if (!item.getWriter().equals(dto.getWriter()) || (!item.getPassword().equals(dto.getPassword())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        negotiation.setStatus(dto.getStatus());
        return NegotiationDto.fromEntity(negotiationRepository.save(negotiation));

    }

    //상품 수락 상태 수정 - 거절
    //상품제안자는 작성자이름과 비밀번호를 첨부해야한다.
    public NegotiationDto confirmNegotiation(Long itemId, Long proposalId, NegotiationDto dto){
        Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        NegotiationEntity negotiation = optionalNegotiation.get();
        if (!negotiation.getItemId().equals(itemId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (!negotiation.getWriter().equals(dto.getWriter()) || (!negotiation.getPassword().equals(dto.getPassword())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //거래 수락
        if (!negotiation.getStatus().equals("수락"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        negotiation.setStatus(dto.getStatus());
        NegotiationEntity confirmNegotiation = negotiationRepository.save(negotiation);

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ItemEntity entity = optionalItem.get();
        entity.setStatus("판매 완료");

        for (NegotiationEntity negotiationEntity : negotiationRepository.findAllByItemId(itemId)){
            if (negotiationEntity.equals(proposalId))
                continue;
            negotiationEntity.setStatus("거절");
            negotiationRepository.save(negotiationEntity);
        }
        return NegotiationDto.fromEntity(confirmNegotiation);
    }

    //상품 제안 삭제 -> 삭제시 작성자와 비밀번호 첨부필요
    public void deleteNegotiation(Long itemId, Long proposalId, NegotiationDto dto) {
        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!negotiationRepository.existsById(proposalId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<NegotiationEntity> checkNegotiation = negotiationRepository.findById(proposalId);
        if (checkNegotiation.isPresent() && checkNegotiation.get().getWriter().equals(dto.getWriter()) &&
                checkNegotiation.get().getPassword().equals(dto.getPassword())) {
            negotiationRepository.delete(checkNegotiation.get());
        }
    }

}