package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.entity.QReview;
import com.campingmall.myproject.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {
    
    //인자값이 없는 생성자
    public ReviewSearchImpl(){super(Review.class);}
    
    @Override
    public Page<Review> search1(Pageable pageable) {

        QReview review = QReview.review;

        //1. BooleanBuilder : 조건절 처리하는 클래스
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //2. Query 객체 생성
        JPQLQuery<Review> query = from(review);
        query.where(booleanBuilder);
        query.where(review.id.gt(0L));

        //Paging
        this.getQuerydsl().applyPagination(pageable, query);

        //query result
        List<Review> result = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(result,pageable,total);
    }

    @Override
    public Page<Review> searchAll(String[] types, String keyword, Pageable pageable) {

        log.info("===== searchAll =====");
        log.info("types: "+types);
        log.info("String: "+keyword);
        log.info("Pageable: "+pageable);

        //1. QReview 도메인 생성
        QReview review = QReview.review;

        log.info("QReview 도메인 생성 완료");

        //2. Query 객체 생성
        JPQLQuery<Review> query = from(review);

        log.info("Query 객체 생성 완료");

        //3. 조건에 해당하는 where 추가
        if((types!=null && types.length>0)&& keyword!=null){
            //조건절 처리하는 객체 생성
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types){
                switch (type){
                    case "t":
                        booleanBuilder.or(review.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(review.content.contains(keyword));
                        break;
                    case "a":
                        booleanBuilder.or(review.author.contains(keyword));
                        break;
                }//switch
            }//for

            query.where(booleanBuilder);

        }//if

        log.info("if 조건절 통과");

        //4. paging
        this.getQuerydsl().applyPagination(pageable,query);

        log.info("paging 통과");

        //5. query 수행
        List<Review> result = query.fetch();

        log.info("=>result: "+result);

        long total = query.fetchCount(); // or=> query.fetch().size();

        log.info(result);
        log.info(total);

        return new PageImpl<>(result, pageable, total);
    }
}
