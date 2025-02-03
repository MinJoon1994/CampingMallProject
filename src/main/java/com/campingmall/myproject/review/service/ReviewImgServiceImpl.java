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

    // 2. 리뷰 이미지 정보 수정 서비스
    @Override
    public void updateReviewImg(Long reviewImgId, MultipartFile reviewImgFile) throws Exception {
        if(!reviewImgFile.isEmpty()){
            //2.1 기존 리뷰 이미지 불러오기
            ReviewImg savedReviewImg = reviewImgRepository.findById(reviewImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //2.2 기존 리뷰 이미지 삭제
            if(!StringUtils.isEmpty(savedReviewImg.getImgName())){
                fileService.deleteFile(reviewImgLocation+"/"+savedReviewImg.getImgName());
            }

            //2.3 변경된 리뷰 이미지 업로드
            String oriImgName = reviewImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
            String imgUrl = "/image/review/"+imgName;

            savedReviewImg.updateReviewImg(oriImgName,imgName, imgUrl);
        }
    }
}
