package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemImgDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.ItemImg;
import com.campingmall.myproject.item.repository.ItemImgRepository;
import com.campingmall.myproject.item.repository.ItemRepository;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import groovy.util.logging.Log4j2;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService{

    //파일
    private final ItemRepository itemRepository; //상품 정보
    private final ItemImgRepository itemImgRepository; //상품 이미지 정보
    private final ItemImgService itemImgService;

    //파일 업로드 실제 경로
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    //1. 상품 정보 등록(상품기본정보,상품이미지 파일)
    @Override
    public Long savedItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFiles)throws Exception {

        //1.1 상품 정보 등록
        Item item = itemFormDTO.createItem(); //상품 등록 화면폼에서 넣은 DTO데이터 -> Entity로 맵핑
        itemRepository.save(item); //DB에 반영

        //1.2 상품 이미지 등록
        for(int i=0; i<itemImgFiles.size(); i++){
            ItemImg itemImg = new ItemImg();

            itemImg.setItem(item); // 상품이미지 Entity 와 상품 Entity 조인

            //첫번쨰 상품 이미지 Entity 를 대표 이미지로 설정
            if(i==0){
                itemImg.setRepImgYn("Y");
            }else{
                itemImg.setRepImgYn("N");
            }

            itemImgService.savedItemImg(itemImg,itemImgFiles.get(i));
        }

        return item.getId();
    }

    //2. 상품 조회
    @Override
    public ItemFormDTO getItemDetail(Long itemId) {
        //2.1 특정 상품에 대한 상품이미지 정보 조회: DB->Entity
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        //2.2 List 에 있는 entity -> DTO 로 변환
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList) {
            //entity -> dto
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);

            //dto -> List
            itemImgDTOList.add(itemImgDTO);
        }

        //2.3 상품 기본 정보 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        //2.4 Entity -> DTO
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList);

        return itemFormDTO;
    }
    
    //3. 상품 정보(상품기본 정보, 상품이미지 정보) 수정
    @Override
    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFiles) throws Exception {

        //3.1 기존 상품 정보 호출
        Item item = itemRepository.findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);

        //3.2 수정 폼으로 부터 전달 받은 상품 정보를 entity 전달(dto -> entity)
        item.updateItem(itemFormDTO);

        List<Long> itemImgIds = itemFormDTO.getItemImgIds();
        for(int i = 0; i<itemImgIds.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFiles.get(i));
        }
        return item.getId();
    }


    //4.상품 검색
    @Override
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        return itemRepository.getAdminItemPage(itemSearchDTO,pageable);
    }
    
    
    //상품 SHOP(메인) 리스트
    @Override
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDTO,pageable);
    }
}
