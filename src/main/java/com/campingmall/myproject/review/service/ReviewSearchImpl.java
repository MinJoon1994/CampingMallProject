package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.entity.Review;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {
    
    //인자값이 없는 생성자
    public ReviewSearchImpl(){super(Review.class);}
    
    @Override
    public Page<Review> search1(Pageable pageable) {

        //QReview review = QReview.review;

        return null;
    }

    @Override
    public Page<Review> searchAll(String[] types, String keyword, Pageable pageable) {
        return null;
    }
}
