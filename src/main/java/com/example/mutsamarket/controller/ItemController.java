package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.item.ItemDto;
import com.example.mutsamarket.dto.item.ResponseDto;
import com.example.mutsamarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    // POST /items 상품 등록 기능
    @PostMapping
    public ResponseDto create(@RequestBody ItemDto itemDto) {
        service.createItem(itemDto);
        ResponseDto response = new ResponseDto();
        response.setMessage("등록이 완료되었습니다.");
        return response;
    }

    // GET /items 상품 전체 조회 기능
    @GetMapping
    public Page<ItemDto> readAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "25") Integer limit) {
        return service.readItemsPage(page, limit);
    }

    // GET /items/{itemId} 상품 단일 품목 조회 기능(상품 아이디로)
    @GetMapping("/{itemId}")
    public ItemDto read(@PathVariable("itemId") Long id) {
        return service.readItem(id);
    }

    // PUT /items/{itemId} 상품 수정 기능
    @PutMapping("/{itemId}")
    public ResponseDto update(@PathVariable("itemId") Long id, @RequestBody ItemDto dto) {
        service.updateItem(id, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("물품이 수정되었습니다.");
        return response;
    }

    //PUT /items/{itemId}/image
    @PutMapping("/{itemId}/image")
    public ResponseEntity<ResponseDto> updateImage(
            @PathVariable("itemId") Long itemId,
            @RequestParam("image") MultipartFile image,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password
    ) {
        service.updateImage(itemId, image, writer, password);
        ResponseDto response = new ResponseDto();
        response.setMessage("이미지가 등록되었습니다.");
        return ResponseEntity.ok(response);
    }

    // DELETE /items/{itemId}
    @DeleteMapping("/{itemId}")
    public ResponseDto delete(@PathVariable("itemId") Long id, @RequestBody ItemDto dto) {
        service.deleteItem(id, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("물품을 삭제했습니다.");
        return response;
    }
}