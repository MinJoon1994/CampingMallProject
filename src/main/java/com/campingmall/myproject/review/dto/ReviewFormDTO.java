package com.campingmall.myproject.review.dto;

import com.campingmall.myproject.review.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter@ToString
public class ReviewFormDTO {
    private Long id;
    @NotBlank(message = "제목은 반드시 입력해야 합니다.")
    private String title;
    @NotBlank(message = "내용은 반드시 입력해야 합니다.")
    private String content;
    @NotBlank(message = "작성자는 반드시 입력해야 합니다.")
    private String author;

    @NotNull(message = "별점은 반드시 입력해야 합니다.")
    private int star;
    @NotNull(message = "리뷰할 상품아이디는 반드시 존재해야 합니다.")
    private Long itemId;

    private static ModelMapper modelMapper = new ModelMapper();
    public static ReviewFormDTO of(Review review){return modelMapper.map(review, ReviewFormDTO.class);}

    //리뷰 저장 후 수정할 때 리뷰 이미지 정보 저장하는 LIST
    //리뷰 이미지 Entity -> 리뷰 이미지 DTO 전환
    private List<ReviewImgDTO> reviewImgDTOList = new ArrayList<>();

    //리뷰 이미지 아이디(PK)를 저장하는 LIST
    private List<Long> reviewImgIds = new ArrayList<>();

    public Review createReview(){return modelMapper.map(this,Review.class);}
}
