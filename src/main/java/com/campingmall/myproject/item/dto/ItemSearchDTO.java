package com.campingmall.myproject.item.dto;

import com.campingmall.myproject.item.constant.ItemSellStatus;
import com.campingmall.myproject.item.constant.ItemTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ItemSearchDTO {

    //1. 상품 등록일 비교해서 상품 데이터 조회
    private String searchDateType;

    /*
    *  all: 상품 등록일 전체
    *  1d : 최근 하루 동안 등록된 상품
    *  1w : 최근 일주일
    *  1m : 최근 한달
    */

    //2. 상품명으로 조회
    private String searchItemName;
    //3. 상품 유형으로 조회
    private ItemTypes itemTypes;

    /*
    * A: 텐트
    * B: 조명
    * C: 도구
    * D: 밀키트
    */
    
    //4. 가격으로 조회
    private String searchPrice;
    //6. 상품 유형을 기준으로 조회
    private String searchBy;
    //7. 검색어 기준으로 조회
    private String searchQuery="";
    //8. 상품 판매상태로 조회
    private ItemSellStatus itemSellStatus;




}
