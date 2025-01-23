package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.service.ItemServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    private final ItemServiceImpl itemService;

    //1.상품 등록하기 위한 입력 폼 요청
    @GetMapping(value = "/admin/item/new")
    public String itemForm(ItemSearchDTO itemSearchDTO, Model model){
        log.info("상품등록 폼 요청");
        model.addAttribute("itemFormDTO",new ItemFormDTO());
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        return "item/itemForm";
    }

    //1-2. 상품 기본정보 : Entity -> DTO 작업
    @PostMapping(value="/admin/item/new")
    public String itemNew(
            @Valid ItemFormDTO itemFormDTO,
            BindingResult bindingResult,
            Model model,
            @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList
    ){

        //유효성 검사후 이상 있으면 상품 등록 화면으로 전환
        if(bindingResult.hasErrors()){
            log.info("에러발생");
            return "item/itemForm";
        }

        //상품 대표이미지가 없으면 상품 등록 화면으로 전환
        if(itemImgFileList.get(0).isEmpty()&&itemFormDTO.getId()==null){
            model.addAttribute("errorMessage","대표이미지는 필수 입력값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.savedItem(itemFormDTO,itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생 하였습니다.");
            return "item/itemForm";
        }

        return"redirect:/";
    }

    //2.상품 조회
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model){
        try{
            ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDTO",itemFormDTO);

            log.info("itemFormDTO : "+itemFormDTO);

            return "item/itemForm";
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDTO",new ItemFormDTO());
        }

        return "item/itemForm";
    }

    //3. 상품 수정
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model){
        //유효성 검사후 이상 있으면 상품 등록 화면으로 전환
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        //상품 대표이미지가 없으면 상품 등록 화면으로 전환
        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId()==null){
            log.info("대표이미지가 비어있어요.");
            model.addAttribute("errorMessage","첫번째 상품(대표)이미지는 필수 입력값입니다.");
            return "item/itemForm";
        }

        try {

        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //4. 상품 목록(검색)
    // 중복되는 url 이 없도록 하나의 컨트롤러에는 하나의 url 이 배정되는 방식
    @GetMapping(value = {"/admin/item","/admin/itempage/{page}"})
    public String itemManger(
            ItemSearchDTO itemSearchDTO, @PathVariable("page")Optional<Integer> page,
            Model model){

        //4.1 페이지 기본 설정
        Pageable pageable = PageRequest.of(page.isPresent()?page.get(): 0, 6);

        //4.2 목록 요청 서비스(검색)
        Page<Item> items = itemService.getAdminItemPage(itemSearchDTO, pageable);

        model.addAttribute("items",items);
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("maxPage",6);

        return "shop/shopList";
    }
}
