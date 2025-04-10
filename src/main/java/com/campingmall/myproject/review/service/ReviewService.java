package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.dto.ReviewDTO;
import com.campingmall.myproject.review.dto.ReviewFormDTO;
import com.campingmall.myproject.search.dto.PageRequestDTO;
import com.campingmall.myproject.search.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService{

    //1. Review 등록 인터페이스
    Long register(ReviewFormDTO reviewDTO, List<MultipartFile> reviewImgFiles) throws Exception;

    //2. Review 조회 인터페이스
    ReviewDTO readOne(Long id);

    //3. Review 수정 인터페이스
    Long modify(Long id, ReviewDTO reviewDTO, List<MultipartFile> reviewImgFiles) throws Exception;

    //4. Review 삭제 인터페이스
    void delete(Long id);

    //5. Review 목록 : 페이징 처리
    PageResponseDTO<ReviewDTO> list (PageRequestDTO pageRequestDTO);

    //6. Review itemId 로 조회 인터페이스
    public List<ReviewDTO> getReviewsByItemId(Long itemId);

    //7. 리뷰 댓글 수 처리 인터페이스

}
