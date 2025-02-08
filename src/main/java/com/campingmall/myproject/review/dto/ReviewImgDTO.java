package com.campingmall.myproject.review.dto;

import com.campingmall.myproject.review.entity.ReviewImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter@Setter@ToString
public class ReviewImgDTO {

    private Long id;            //리뷰 이미지 식별
    private String imgName;     //리뷰 이미지 파일명
    private String oriImgName;  //원본 이미지
    private String imgUrl;      //이미지 조회 경로
    private String repImgYn;    //대표(기본) 이미지

    private ModelMapper modelMapper = new ModelMapper();

    public ReviewImgDTO of(ReviewImg reviewImg){
        //Entity getter() -> DTO setter() 전환
        //동일한 이름가진 속성명 getter(), setter() 동작
        //상품 이미지 Entity -> 상품 이미지 DTO 전환 기능 수행 하는 함수
        return modelMapper.map(reviewImg, ReviewImgDTO.class);
    }
}
