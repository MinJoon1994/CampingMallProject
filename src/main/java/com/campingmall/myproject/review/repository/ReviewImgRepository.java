package com.campingmall.myproject.review.repository;

import com.campingmall.myproject.review.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {

    //1. 특정 리뷰에 대한 리뷰 이미지 조회시 정렬
    List<ReviewImg> findByReviewRnoOrderByIdAsc(Long rno);

    //2. 리뷰 대표 이미지 조회
    ReviewImg findByReviewRnoAndRepImgYn(Long rno,String repImgYn);
}
