package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.constant.ItemTypes;
import com.campingmall.myproject.item.dto.ItemDTO;
import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.service.ItemService;
import com.campingmall.myproject.review.dto.ReviewDTO;
import com.campingmall.myproject.review.service.ReviewService;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "/shop")
public class ShopController {

    private final ItemService itemService;
    private final ReviewService reviewService;

    //1. shop 메인 페이지
    @GetMapping("/main")
    public String shopMain(ItemSearchDTO itemSearchDTO, Optional<Integer> page, Model model,
                           @Param("itemTypes")ItemTypes itemTypes,
                           @Param("searchDateType")String searchDateType,
                           @Param("searchPrice")String searchPrice
    ){

        log.info("파라미터 받은 값들");
        log.info(searchDateType);
        log.info(searchPrice);
        log.info(itemTypes);
        itemSearchDTO.setItemTypes(itemTypes);
        itemSearchDTO.setSearchPrice(searchPrice);
        itemSearchDTO.setSearchDateType(searchDateType);

        //페이징 설정
        Pageable pageable = PageRequest.of(page.isPresent()?page.get(): 0, 6);
        Page<MainItemDTO> items = itemService.getMainItemPage(itemSearchDTO,pageable);


        model.addAttribute("items",items);
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("maxPage",3); //페이지블록

        return "/shop/shopList";
    }

    //2. shop item 상세보기 요청
    @GetMapping(value="/item/{itemId}")
    public String shopItemDetail(ItemSearchDTO itemSearchDTO,
                                 @PathVariable("itemId")Long itemId, Model model){
        //상품 id 가지고 상품 상세정보 요청: 상품 기본정보, 상품이미지 정보
        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId);

        //상품 id 가지고 상품 리뷰 정보 요청
        List<ReviewDTO> reviewDTOList = reviewService.getReviewsByItemId(itemId);

        model.addAttribute("reviewDTOList",reviewDTOList);
        model.addAttribute("item",itemFormDTO);
        model.addAttribute("itemSearchDTO",itemSearchDTO);

        return "shop/shopItemDetail";
    }




}
