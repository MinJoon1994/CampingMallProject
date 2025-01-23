package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemSearchService {

    // 1.상품 조회 List
    // 페이징 처리 인터페이스
    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

    //2. SHOP 화면에 표시될 상품 LIST 조회
    Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
}
