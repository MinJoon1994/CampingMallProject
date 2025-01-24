package com.campingmall.myproject.cart.service;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartItemDTO;
import com.campingmall.myproject.cart.dto.CartOrderDTO;
import com.campingmall.myproject.order.dto.OrderRequestDTO;

import java.util.List;

public interface CartService {

    //1. 상품 장바구니에 담기(상품 id, 상품수량, 장바구니 소유자: 회원)
    public Long addCart(CartItemDTO cartItemDto,String loginId);

    //2. 로그인한 회원의 장바구니 상품 조회
    public List<CartDetailDTO> getCartList(String loginId);

    //3. 장바구니 수정권한 체크
    public boolean validateCartItem(Long cartItemId, String loginId);

    //4. 장바구니 수량 업데이트
    public void updateCartItemCount(Long cartItemId, int Count);

    //5. 장바구니 상품 삭제
    public void deleteCartItem(Long cartItemId);

    //6. 장바구니 상품 주문하기
    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String loginId, OrderRequestDTO orderRequestDTO);

    //7. 장바구니 비우기
    public Long deleteAllCartItem(List<CartOrderDTO> cartOrderDTOList, String loginId);

}
