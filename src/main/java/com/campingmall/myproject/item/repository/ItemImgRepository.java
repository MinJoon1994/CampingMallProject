package com.campingmall.myproject.item.repository;

import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    //1. 특정 상품에 대한 상품 이미지 조회시 정렬
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //2. 상품 대표 이미지 조회
    ItemImg findByItemIdAndRepImgYn(Long itemId,String repImgYn);
}
