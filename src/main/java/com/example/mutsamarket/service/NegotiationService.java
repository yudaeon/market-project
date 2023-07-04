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

import java.util.List;
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
    //
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
        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (!negotiationRepository.existsById(proposalId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isPresent() && optionalNegotiation.get().getWriter().equals(dto.getPassword())) {
            NegotiationEntity target = optionalNegotiation.get();
            target.setSuggestedPrice(dto.getSuggestedPrice());
            return NegotiationDto.fromEntity(negotiationRepository.save(target));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


    public NegotiationDto updateNegotiationStatus(Long itemId, Long proposalId, NegotiationDto dto) {
        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!negotiationRepository.existsById(proposalId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (itemRepository.existsAllByIdAndWriterLikeAndPasswordLike(itemId, dto.getWriter(), dto.getPassword())) {
            Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
            NegotiationEntity target = optionalNegotiation.get();
            target.setStatus(dto.getStatus());
            return NegotiationDto.fromEntity(negotiationRepository.save(target));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    //상품 수락 상태 수정 - 거절
    //상품제안자는 작성자이름과 비밀번호를 첨부해야한다.
    public void updateAcceptStatus(Long itemId, Long proposalId, NegotiationDto dto) {
        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!negotiationRepository.existsById(proposalId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isPresent() && optionalNegotiation.get().getStatus().equals(dto.getPassword())) {
            if (optionalNegotiation.get().getStatus().equals("거절")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            NegotiationEntity target = optionalNegotiation.get();
            target.setStatus(dto.getStatus());
            negotiationRepository.save(target);

            ItemEntity targetItem = itemRepository.findById(itemId).get();
            targetItem.setStatus("판매 완료");
            itemRepository.save(targetItem);

            List<NegotiationEntity> rejectNegotiation = negotiationRepository.findAllByItemId(itemId);
            for (NegotiationEntity reject : rejectNegotiation) {
                if (reject.getStatus().equals("제안")) {
                    reject.setStatus("거절");
                    negotiationRepository.save(reject);
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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