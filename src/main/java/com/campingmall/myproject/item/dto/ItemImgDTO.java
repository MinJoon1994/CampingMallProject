package com.campingmall.myproject.item.dto;

import com.campingmall.myproject.item.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter@Setter
public class ItemImgDTO {

    private Long id;            // 상품 이미지 식별
    private String imgName;     // 이미지 파일명
    private String oriImgName;  // 원본 이미지
    private String imgUrl;      // 이미지 조회 경로
    private String repImgYn;    // 대표(기본) 이미지

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDTO of(ItemImg itemImg){
        //Entity getter() -> DTO setter() 전환
        //동일한 이름가진 속성명 getter(), setter()동작
        //상품 이미지 Entity -> 상품 이미지 DTO 전환 기능수행 하는 함수
        return modelMapper.map(itemImg,ItemImgDTO.class);
    }
}
