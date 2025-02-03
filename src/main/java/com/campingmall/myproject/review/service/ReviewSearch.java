package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSearch {

    //페이징 설정 인터페이스
    Page<Review> search1(Pageable pageable);

    //조건 설정 인터페이스
    Page<Review> searchAll(String[] types, String keyword, Pageable pageable);

    //특정 게시글에 대한 댓글 개수 계산하는 인터페이스
}
