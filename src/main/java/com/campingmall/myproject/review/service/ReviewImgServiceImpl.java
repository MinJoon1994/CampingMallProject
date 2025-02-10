package com.campingmall.myproject.review.service;

import com.campingmall.myproject.item.service.FileService;
import com.campingmall.myproject.review.entity.ReviewImg;
import com.campingmall.myproject.review.repository.ReviewImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImgServiceImpl implements ReviewImgService {

    private final ReviewImgRepository reviewImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation}")
    private String reviewImgLocation;

    // 1. 리뷰 이미지 정보 등록 서비스
    @Override
    public void savedReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile) throws Exception {

        String oriImgName = reviewImgFile.getOriginalFilename();
        String imgName = ""; //업로드 된 파일이름 추출
        String imgUrl = ""; //클라이언트가 서버에 있는 이미지 요청시 사용되는 Url

        //1.파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //파입 업로드 기능 서비스 요청
            imgName = fileService.uploadFile(
                    reviewImgLocation,
                    oriImgName,
                    reviewImgFile.getBytes()
            );
            imgUrl="/uploadimage/campingmall/"+imgName;
        }

        reviewImg.updateReviewImg(oriImgName,imgName,imgUrl);
        reviewImgRepository.save(reviewImg);
        
    }

    // 2. 리뷰 이미지 정보 삭제
    @Override
    public void deleteReviewImg(Long reviewId) throws Exception {

        //2.1 review Id로 reviewImgEntity 불러오기
        List<ReviewImg> reviewImgList = reviewImgRepository.findByReviewIdOrderByIdAsc(reviewId);

        //2.2 불러온 ReviewImgId 전부 삭제
        reviewImgRepository.deleteAll(reviewImgList);

    }
}
