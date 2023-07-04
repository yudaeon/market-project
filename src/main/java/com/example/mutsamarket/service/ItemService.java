package com.example.mutsamarket.service;


import com.example.mutsamarket.dto.item.ItemDto;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.repository.ItemRepository;
import com.sun.jdi.event.StepEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.CompositeNestedGeneratedValueGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final ItemRepository itemRepository;

    //CREATE 물품 정보 등록
    public ItemDto createItem(ItemDto dto) {
        ItemEntity newItem = new ItemEntity();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setImageUrl(dto.getImageUrl());
        newItem.setMinPriceWanted(dto.getMinPriceWanted());
        //최초로 물품이 등록될 때, 중고 물품의 상태는 판매중 상태가 된다.
        newItem.setStatus("판매중");
        newItem.setWriter(dto.getWriter());
        //사용자가 물품을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
        newItem.setPassword(dto.getPassword());
        log.info(String.valueOf(newItem));

        return ItemDto.fromEntity(repository.save(newItem));
    }

    //READ 물품 조회 (전체 상품)
    public Page<ItemDto> readItemsPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<ItemEntity> itemEntityPage = repository.findAll(pageable);
        Page<ItemDto> itemDtoPage = itemEntityPage.map(ItemDto::fromEntity);

        return itemDtoPage;
    }

    //READ : 물품 조회 기능 (단일 품목 아이디로)
    public ItemDto readItem(Long id) {
        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            return ItemDto.fromEntity(optionalItem.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 상품이 없습니다.");
        }
    }

    //UPDATE 물품 정보 수정
    public ItemDto updateItem(Long id, ItemDto dto) {
        Optional<ItemEntity> updateItem = repository.findById(id);
        if (updateItem.isPresent()) {
            ItemEntity target = updateItem.get();

            //1. 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다. -> 수정
            if (target.getPassword().equals(dto.getPassword())) {
                target.setTitle(dto.getTitle());
                target.setDescription(dto.getDescription());
                target.setMinPriceWanted(dto.getMinPriceWanted());
                target.setStatus(dto.getStatus());
                target.setWriter(dto.getWriter());
                target.setPassword(dto.getPassword());

                return ItemDto.fromEntity(repository.save(target));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 틀렸습니다.");
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 상품이 없습니다.");
    }

    //이미지 업로드 -> 코드 수정필요
//    public updateImage(Long id, MultipartFile image, String writer, String password) {
//        Optional<ItemEntity> optionalItem = repository.findById(id);
//        if (optionalItem.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//
//        ItemEntity item = optionalItem.get();
//
//        if (!item.getPassword().equals(password)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//
//        //이미지 저장
//        String imageUrl = String.format("media/%d/", id);
//        try {
//            Files.createDirectories(Path.of(imageUrl));
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        String imageOriginalFilename = image.getOriginalFilename();
//        String[] imageFilenameSplit = imageOriginalFilename.split("\\.");
//        String extention = imageFilenameSplit[imageFilenameSplit.length -1];
//        String imageFileName = "image." + extention;
//        String imagePath = imageUrl + imageFileName;
//        log.info(imagePath);
//        try {
//            image.transferTo(Path.of(imagePath));
//        } catch (IOException e){
//            log.error(e.getMessage()); throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        item.setImageUrl(String.format("/static/%d/%s", id, imageFileName));
//        return ItemDto.fromEntity(repository.save(item));
//    }
    public ItemDto updateImage(Long id, MultipartFile image, String writer, String password) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ItemEntity item = optionalItem.get();

        if (!item.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 이미지 저장
        String imageUrl = String.format("media/%d/", id);
        try {
            Files.createDirectories(Path.of(imageUrl));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String imageOriginalFilename = image.getOriginalFilename();
        String[] imageFilenameSplit = imageOriginalFilename.split("\\.");
        String extension = imageFilenameSplit[imageFilenameSplit.length - 1];
        String imageFileName = "image." + extension;
        String imagePath = imageUrl + imageFileName;
        log.info(imagePath);
        try {
            image.transferTo(Path.of(imagePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        item.setImageUrl(String.format("/static/%d/%s", id, imageFileName));
        return ItemDto.fromEntity(itemRepository.save(item));
    }


    //DELETE 물품 정보 삭제
    public void deleteItem(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);

        if (optionalItemEntity.isPresent()) {
            ItemEntity target = optionalItemEntity.get();
            // 비밀번호가 일치하면 삭제하기
            if (target.getPassword().equals(dto.getPassword())) repository.deleteById(id);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 틀렸습니다.");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 상품이 없습니다.");
    }
}