package com.campingmall.myproject.order.repository;

import com.campingmall.myproject.order.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    //1. 현재 로그인 사용자의 주문 데이터를 페이지 조건에 맞춰 조회
    @Query("select o from Order o where o.member.loginId = :loginId order by o.orderStatus, o.orderDate desc")
    List<Order> findOrder(@Param("loginId") String loginId, Pageable pageable);

    //2. 현재 로그인한 회원의 주문 개수가 몇 개 인지 조회
    @Query("select count(o) from Order o where o.member.loginId = :loginId")
    Long countOrder(@Param("loginId") String loginId);

}
