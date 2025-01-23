package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final CartService cartService;
    
    // 장바구니에서 상품 주문시 장바구니상품담은 정보로 주문페이지
    @GetMapping("/cartorder")
    public String orderPage(Principal principal, ItemSearchDTO itemSearchDTO, Model model){
        //현재 로그인한 회원 아이디 추출
        String loginId = principal.getName();

        List<CartDetailDTO> cartDetailDTOList = cartService.getCartList(loginId);

        model.addAttribute("cartItems",cartDetailDTOList);
        model.addAttribute("itemSearchDTO",itemSearchDTO);

        return "order/cartOrder";

    }
}
