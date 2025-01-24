package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartOrderDTO;
import com.campingmall.myproject.cart.repository.CartItemRepository;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.order.dto.OrderRequestDTO;
import com.sun.net.httpserver.HttpsServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    //---------------------------------------------------//
    //             결제/주문 페이지로 보내기
    //---------------------------------------------------//
    @PostMapping("/cartorder")
    public String cartOrderPage(
        @RequestParam("itemIds[]") List<Long> itemIds,
        ItemSearchDTO itemSearchDTO,
        Principal principal,
        Model model
    ){
        //선택된 카트 아이템 아이디로 카트 아이템 정보 불러오기
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        for (Long itemId : itemIds) {
            log.info("넘겨받은 id 값: " + itemId);
            List<CartDetailDTO> selectedItemDetails = cartItemRepository.findCartItemDetailDTO(itemId);
            cartDetailDTOList.addAll(selectedItemDetails); // 리스트 병합
        }

        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("cartItems", cartDetailDTOList);

        return "order/orderPage";
    }
    //---------------------------------------------------//
    //           결제/주문 페이지에서 주문하기
    //---------------------------------------------------//
    @PostMapping(value="/order/payment")
    public @ResponseBody ResponseEntity orderPaymentItem(
            Principal principal,
            @RequestBody OrderRequestDTO orderRequestDTO
            ){

        String loginId = principal.getName();

        log.info("값 잘 넘어 왔니?");
        log.info(orderRequestDTO);

        List<CartOrderDTO> cartOrderDTOList = orderRequestDTO.getCartOrderDTOList();

        if(cartOrderDTOList == null || cartOrderDTOList.size()==0){
            return new ResponseEntity("주문할 상품을 선택해주세요", HttpStatus.BAD_REQUEST);
        }

        for(CartOrderDTO cartOrder : cartOrderDTOList){

            if(!cartService.validateCartItem(cartOrder.getCartItemId(),loginId)){
                return new ResponseEntity("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }

        }

        //상품들 주문하기
        Long orderId = cartService.orderCartItem(cartOrderDTOList, loginId , orderRequestDTO);

        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
