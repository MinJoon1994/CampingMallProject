package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import com.campingmall.myproject.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AccountController {

    private final CartService cartService;
    private final OrderService orderService;

    //---------------------------------------------------------//
    //  1. Account page 호출 메서드
    //---------------------------------------------------------//
    @GetMapping(value= "/myshop")
    public String accountPage(Principal principal,
                              Model model,
                              ItemSearchDTO itemSearchDTO){

        // 현재 로그인한 회원 아이디 추출
        String loginId = principal.getName();

        // 현재 로그인한 회원 장바구니 상품 조회
        List<CartDetailDTO> cartDetailDTOList = cartService.getCartList(loginId);
        int cartCount = cartDetailDTOList.size();

        // 현재 로그인한 회원 구매 이력 조회
       Long orderCount = orderService.getOrderCount(loginId);

       model.addAttribute("itemSearchDTO", itemSearchDTO);
       model.addAttribute("cartCount", cartCount);
       model.addAttribute("orderCount", orderCount);

        return "/account/myShopping";
    }
}
