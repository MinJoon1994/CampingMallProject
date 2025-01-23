package com.campingmall.myproject.item.repository;

import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.service.ItemSearchService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>,
        ItemSearchService,
        QuerydslPredicateExecutor<Item>{

    //1.상품 목록
    List<Item> findByItemNm(String itemNm);

    //2. 상품명과 상품 상세 설명을 OR 조건을 통해 조회
    List<Item> findByItemNmOrItemDetail(String itemNm,String itemDetail);

    //3. 상품 가격이 전달된 매개변수 보다 값이 작은 상품 조회
    List<Item> findByPriceLessThan(Integer price);

    //4. 정렬
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //기존 DB에 사용하던 쿼리문 그대로 사용시 : nativeQuery=true 설정
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc",nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}

