package com.campingmall.myproject.review.service;

import com.campingmall.myproject.review.dto.ReviewDTO;
import com.campingmall.myproject.review.dto.ReviewFormDTO;
import com.campingmall.myproject.review.entity.Review;
import com.campingmall.myproject.review.entity.ReviewImg;
import com.campingmall.myproject.review.repository.ReviewRepository;
import com.campingmall.myproject.search.dto.PageRequestDTO;
import com.campingmall.myproject.search.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewImgService reviewImgService;
    private final ModelMapper modelMapper;

    //파일 업로드 실제 경로
    @Value("${itemImgLocation}")
    private String reviewImgLocation;

    // 1. Review 생성
    @Override
    public Long register(ReviewFormDTO reviewFormDTODTO, List<MultipartFile> reviewImgFiles) throws Exception {

        log.info("리뷰 생성을 위해 들어온 값 확인");
        log.info(reviewFormDTODTO);
        log.info(reviewImgFiles);

        //1.1 리뷰 정보 등록
        Review review = reviewFormDTODTO.createReview();
        reviewRepository.save(review);

        log.info("저장완료");

        //1.2 리뷰 이미지 등록
        for(int i=0; i<reviewImgFiles.size(); i++){
            ReviewImg reviewImg = new ReviewImg();

            reviewImg.setReview(review);
            if(i==0){
                reviewImg.setRepImgYn("Y"); // 첫번째 이미지를 대표미지로 설정
            }else{
                reviewImg.setRepImgYn("N");
            }

            reviewImgService.savedReviewImg(reviewImg,reviewImgFiles.get(i));
        }

        return review.getId();
    }


    // 2. Review 조회
    @Override
    public ReviewDTO readOne(Long rno) {

        //2-1. null 예외처리
        Optional<Review> result = reviewRepository.findById(rno);
        //2-2. 영속성 컨텍스트 Entity 값 읽기
        Review review = result.orElseThrow();
        //Entity -> DTO
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);

        return reviewDTO;
    }
    
    //3. Review 수정
    @Override
    public Long modify(ReviewDTO reviewDTO) {

        //3-1. 수정할 Review 번호 읽기
        Optional<Review> result = reviewRepository.findById(reviewDTO.getId());
        //3-2. Entity 값 읽기
        Review review = result.orElseThrow();
        //3-3. entity 값 수정 : dto 값을 entity 에 저장
        review.change(reviewDTO.getTitle(),reviewDTO.getContent(),reviewDTO.getStar());

        //3-4. 수정된 Entity -> DB에 반영
        Long id = reviewRepository.save(review).getId();

        return id;
    }

    //4. Review 삭제
    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
    
    //5. 페이징 처리
    @Override
    public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("=>List "+pageRequestDTO);

        //검색 타입, 키워드에 대한 처리
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("id");

        log.info("=>types: "+types);
        log.info("=>keyword: "+keyword);
        log.info("=>pageable: "+pageable);

        //조건 검색한 결과값 가져오기
        Page<Review> result = reviewRepository.searchAll(types,keyword,pageable);

        log.info("=>result:"+result);

        //Page 객체 있는 내용들을 List 구조로 가져오기
        List<ReviewDTO> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<ReviewDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
