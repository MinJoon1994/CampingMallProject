package com.campingmall.myproject.review.repository;

import com.campingmall.myproject.review.entity.Review;
import com.campingmall.myproject.review.service.ReviewSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review>, ReviewSearch {

}
