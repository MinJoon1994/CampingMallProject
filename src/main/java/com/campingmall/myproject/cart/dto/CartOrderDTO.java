package com.campingmall.myproject.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class CartOrderDTO {
    private Long cartItemId;

    //CartOrderDTO 자기 자신을 List 로 가지고 있도록 구조 설정
    private List<CartOrderDTO> cartOrderDTOList;
}
