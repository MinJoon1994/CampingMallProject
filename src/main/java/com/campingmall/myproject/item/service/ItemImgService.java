package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

public interface ItemImgService {
    //1. 상품 이미지 정보 등록 서비스
    public void savedItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception;

    //2. 상품 이미지 정보 수정 서비스
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception;
}
