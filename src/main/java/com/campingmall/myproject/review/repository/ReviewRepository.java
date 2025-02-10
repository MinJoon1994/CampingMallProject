package com.campingmall.myproject.review.repository;

import com.campingmall.myproject.review.entity.Review;
import com.campingmall.myproject.review.service.ReviewSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review>, ReviewSearch {

    // 1.아이템 ID 로 리뷰 조회
    List<Review> findByItemIdOrderByIdDesc(Long itemId);
}
