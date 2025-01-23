package com.campingmall.myproject.order.service;

import com.campingmall.myproject.order.dto.OrderDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OrderServiceImpl implements OrderService{

    @Override
    public Long order(OrderDTO orderDTO, String loginId) {
        return 0L;
    }

    @Override
    public Page<OrderHistoryDTO> getOrderList(String email, Pageable pageable) {
        return null;
    }

    @Override
    public boolean validateOrder(Long orderId, String loginId) {
        return false;
    }

    @Override
    public void cancelOrder(Long orderId) {

    }

    @Override
    public Long orders(List<OrderDTO> orderDTOList, String loginId) {
        return 0L;
    }
}
