package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    //1.상품 등록
    public Long savedItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFiles) throws Exception;

    //2.상품 조회
    public ItemFormDTO getItemDetail(Long itemId);

    //3.상품 수정
    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFiles) throws Exception;

    //4.상품 목록(관리자 페이지)
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

    //5.상품 SHOP(메인) 목록
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
}

