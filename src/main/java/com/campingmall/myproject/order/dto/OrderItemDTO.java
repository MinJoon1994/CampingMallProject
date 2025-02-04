package com.campingmall.myproject.order.dto;

import com.campingmall.myproject.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString
public class OrderItemDTO {

    private String itemNm;  //상품 이름
    private int count;      //상품 수량
    private int orderPrice; //주문 금액
    private String imgUrl;  //상품 이미지 경로
    private Long itemId;

    //생성자
    public OrderItemDTO(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count  = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
        this.itemId = orderItem.getItem().getId();
    }
}
