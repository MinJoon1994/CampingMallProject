package com.campingmall.myproject.cart.repository;

import com.campingmall.myproject.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

    //현재 로그인한 회원 Cart 조회 => 회원 아이디 기준
    Cart findByMemberId(Long memberId);
}
