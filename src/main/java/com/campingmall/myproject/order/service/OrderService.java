package com.campingmall.myproject.order.service;

import com.campingmall.myproject.order.dto.OrderAddressDTO;
import com.campingmall.myproject.order.dto.OrderDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    //1. 상품 주문 서비스(상품 아이디, 수량, 로그인 고객: 주문 고객)
    public Long order(OrderDTO orderDTO, String loginId,OrderAddressDTO orderAddressDTO);

    //2. 주문 이력 (주문 상품 목록) 서비스
    public Page<OrderHistoryDTO> getOrderList(String loginId, Pageable pageable);

    //3. 주문 이력 카운팅
    public Long getOrderCount(String loginId);

    //4. 주문 취소시 : 로그인 한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
    public boolean validateOrder(Long orderId, String loginId);

    //5. 주문 취소시 : 주문 취소기능 수행
    public void cancelOrder(Long orderId);

    //6. 장바구니에 담겨 있는 주문상품 주문 서비스
    public Long orders(List<OrderDTO> orderDTOList, String loginId, OrderAddressDTO orderAddressDTO);
}
