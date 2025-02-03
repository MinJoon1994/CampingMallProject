package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.entity.ReviewImg;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewImgService {

    //1. 리뷰 이미지 정보 등록 서비스
    public void savedReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile) throws Exception;

    //2. 리뷰 이미지 정보 수정 서비스
    public void updateReviewImg(Long reviewImgId, MultipartFile reviewImgFile) throws Exception;
}
