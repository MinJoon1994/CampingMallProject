package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.entity.ItemImg;
import com.campingmall.myproject.item.repository.ItemImgRepository;
import groovy.util.logging.Log4j2;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgServiceImpl implements ItemImgService{

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation}")
    private String itemImgLocation;
    
    //1.상품 이미지 정보 등록 서비스
    @Override
    public void savedItemImg(ItemImg itemImg, MultipartFile itemImgFile)throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = ""; //업로드된 파일이름 추출
        String imgUrl = ""; //클라이언트가 서버에 있는 이미지 요청시 사용되는 Url
        
        //1.파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //파일 업로드 기능 서비스 요청
            imgName = fileService.uploadFile(
                    itemImgLocation,        // c:/upload : application.properties 에서 설정한 파일 업로드
                    oriImgName,             // 상품이미지 파일 이름(첨부파일)
                    itemImgFile.getBytes()  // 파일을 파일의 바이트 배열로 업로드
            );
            imgUrl="/uploadimage/campingmall/"+imgName;
        }//if end
        
        //상품 이미지 정보 저장 : (업로드 전 이미지 파일, 업로드 후 이미지 파일, 이미지 파일 요청 URL)
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);
    }
    
    //2.상품 이미지 정보 수정 서비스
    @Override
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()){
            //2.1 기존 상품 이미지 불러오기
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //2.2 기존 상품 이미지 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }
            //2.3 변경된 상품 이미지 업로드
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/image/item/"+imgName;

            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl);
        }
    }
}
