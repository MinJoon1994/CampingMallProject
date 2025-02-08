package com.campingmall.myproject.review.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.cglib.core.internal.LoadingCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;        // 리뷰 ID

    @NotEmpty(message = "3자 이상 100자 이내로 입력하세요.")
    @Size(min=3,max=100)
    private String title;    // 리뷰 제목
    @NotEmpty(message = "게시글 내용이 비어있습니다.")
    private String content;  // 리뷰 내용
    @NotEmpty(message = "작성자는 반드시 존재해야 합니다.")
    private String author;   // 리뷰 작성자

    private String itemNm;   // 상품 이름

    private int star;        // 별점

    private Long itemId;     // 상품 ID (item 의 FK);

    private String createdBy;
    private String modifiedBy;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private List<ReviewImgDTO> reviewImgList; // ✅ 추가: 리뷰 이미지 리스트
}
