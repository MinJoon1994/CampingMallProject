package com.campingmall.myproject.order.dto;

import com.campingmall.myproject.order.constant.OrderStatus;
import com.campingmall.myproject.order.entity.Order;
import com.campingmall.myproject.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Setter@Getter@ToString
public class OrderHistoryDTO {
    //주문 아이디
    private Long orderId;
    //주문 날짜 => 날짜 형식 전환
    private String orderDate;
    //주문 상태(주문,취소)
    private OrderStatus orderStatus;
    //주문 상품 리스트
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    //생성자 Order Entity 정보 읽기 -> DTO에 저장
    public OrderHistoryDTO(Order order){
        this.orderId = order.getId();

        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }
    
    //주문 상품 정보 읽어서 주문상품 리스트에 저장
    public void addOrderItemDTO(OrderItemDTO orderItemDTO){orderItemDTOList.add(orderItemDTO);}
}
