package com.campingmall.myproject.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    private Long id;                //상품 번호
    private String itemNm;          //상품 이름
    private Integer price;          //상품 가격
    private String itemDetail;      //상품 설명
    private String itemSellStatus;  //상품 판매상태
}
