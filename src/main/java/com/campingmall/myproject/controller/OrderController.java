package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.repository.CartItemRepository;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    
    @PostMapping("/cartorder")
    public String cartOrderPage(
        @RequestParam("itemIds[]") List<Long> itemIds,
        ItemSearchDTO itemSearchDTO,
        Model model
    ){
        //선택된 카트 아이템 아이디로 카트 아이템 정보 불러오기
        List<CartDetailDTO> cartDetailDTOList= new ArrayList<>();
        for(int i =0; i < itemIds.size(); i++){
           List<CartDetailDTO> selectedItemDetail = cartItemRepository.findCartDetailDtoList(itemIds.get(i));

        }

        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("selectedItems", cartDetailDTOList);
        return "order/orderPage";
    }
}
