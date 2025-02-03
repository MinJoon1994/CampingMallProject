package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartOrderDTO;
import com.campingmall.myproject.cart.repository.CartItemRepository;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemDTO;
import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.repository.ItemImgRepository;
import com.campingmall.myproject.item.repository.ItemRepository;
import com.campingmall.myproject.item.service.ItemService;
import com.campingmall.myproject.order.dto.*;
import com.campingmall.myproject.order.service.OrderService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final OrderService orderService;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final ItemImgRepository itemImgRepository;

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
    //                  바로 주문하기
    //---------------------------------------------------//
    @PostMapping("/directorder")
    public String directOrderPage(
            @RequestParam("itemId") Long itemId,
            @RequestParam("count") Long count,
            ItemSearchDTO itemSearchDTO,
            Principal principal,
            Model model
    ){
        log.info("직접 주문하기");
        log.info(itemId);
        log.info(count);

        ItemFormDTO item = itemService.getItemDetail(itemId);

        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("item", item);
        model.addAttribute("count", count);

        return "order/directOrderPage";
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

    //---------------------------------------------------//
    //           바로 주문하기
    //---------------------------------------------------//
    @PostMapping(value="/order/payment/direct")
    public @ResponseBody ResponseEntity orderPaymentDirectItem(
            Principal principal,
            @RequestBody DirectOrderDTO directOrderDTO
    ){

        String loginId = principal.getName();

        log.info("값 잘 넘어 왔니?");
        log.info(directOrderDTO);

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setItemId(directOrderDTO.getItemId());
        orderDTO.setCount(directOrderDTO.getCount());

        OrderAddressDTO orderAddressDTO = new OrderAddressDTO();

        orderAddressDTO.builder()
                .address(directOrderDTO.getAddress())
                .postcode(directOrderDTO.getPostcode())
                .extraAddress(directOrderDTO.getExtraAddress())
                .detailAddress(directOrderDTO.getDetailAddress())
                .recipientName(directOrderDTO.getRecipientName())
                .build();

        //상품들 주문하기
        Long orderId = orderService.order(orderDTO,loginId,orderAddressDTO);

        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

    //---------------------------------------------------//
    //           주문한 내역 확인하기 페이지
    //---------------------------------------------------//
    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHistory(@PathVariable("page") Optional<Integer> page,
                               ItemSearchDTO itemSearchDTO,
                               Principal principal,
                               Model model){
        //페이지 설정
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0, 2);

        //현재 로그인한 회원의 로그인아이디와 페이징 객체를 인자로 전달하여 구매이력 조회
        Page<OrderHistoryDTO> orderHistoryDTOList =
                orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("orders", orderHistoryDTOList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5); // 페이지 블럭(한 화면에 보여질 페이지 갯수)

        log.info("-------------- 구매이력");
        orderHistoryDTOList.getContent().forEach(o->log.info(o));

        return "order/orderHistory";
    }

    //---------------------------------------------------//
    //           주문 취소 처리하기
    //---------------------------------------------------//
    @PostMapping(value="/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(
            Principal principal,
            @PathVariable("orderId") Long orderId
    ){
        String loginId = principal.getName();

        //주문 취소시 현재 취소한 사용자(로그인)가 주문자인지 권한 검사
        if(!orderService.validateOrder(orderId,loginId)){
            return new ResponseEntity("주문취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        //주문 취소 서비스 요청
        orderService.cancelOrder(orderId);
        return new ResponseEntity(orderId,HttpStatus.OK);
    }

}
