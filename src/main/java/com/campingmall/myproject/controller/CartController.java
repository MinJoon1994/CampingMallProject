package com.campingmall.myproject.controller;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartItemDTO;
import com.campingmall.myproject.cart.service.CartService;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;
    //---------------------------------------------------------//
    //  1. 상세페이지에서 장바구니 담기 기능 요청시 처리
    //---------------------------------------------------------//
    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(
            @RequestBody @Valid CartItemDTO cartItemDTO,
            Principal principal,
            BindingResult bindingResult
    ){
        //1.1 장바구니에 담을 상품정보(상품아이디,수량)데이터 검증
        if(bindingResult.hasErrors()){
            //1.1.1 에러 메시지 처리할 객체
            StringBuilder sb = new StringBuilder();
            //1.1.2 에러가 발생한 필드만 추출하여 List 에 저장
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            //1.1.3 필드 항목 순차적으로 에러 항목별로 메시지 작성
            for(FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        //1.2 현재 로그인한 회원의 아이디 정보 추출
        String loginId = principal.getName();

        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDTO, loginId);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        //1.3 장바구니 기능 정상 수행 후 장바구니 ID와 상태코드 응답
        return new ResponseEntity(cartItemId,HttpStatus.OK);
    }

    //---------------------------------------------------------//
    //              2. 장바구니 페이지 이동
    //---------------------------------------------------------//
    @GetMapping(value = "/cart")
    public String cartHist(Principal principal, ItemSearchDTO itemSearchDTO, Model model){
        //2.1 현재 로그인한 회원 아이디 추출
        String loginId = principal.getName();

        //2.2 현재 로그인한 회원 장바구니 상품 조회
        List<CartDetailDTO> cartDetailDTOList = cartService.getCartList(loginId);

        //2.3 장바구니 상품 조회 결과 객체에 담아 View 페이지 이동
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("cartItems",cartDetailDTOList);

        return "cart/cartList";
    }

    //---------------------------------------------------------------------//
    //            REST API 방식 : PATCH(요청 자원의 일부만 처리)
    //---------------------------------------------------------------------//
    //                      3. 장바구니 상품 수정
    //---------------------------------------------------------------------//
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(
        @PathVariable("cartItemId") Long cartItemId,
        Principal principal,
        int count
    ){
        String loginId = principal.getName();

        if(count<=0){
            return new ResponseEntity<String>("최소 1개이상 담아주세요.",HttpStatus.BAD_REQUEST);
        }else if(!cartService.validateCartItem(cartItemId,loginId)){
            return new ResponseEntity<String>("수정(삭제) 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        //장바구니에 있는 상품 수량 업데이트 처리 요청
        cartService.updateCartItemCount(cartItemId,count);

        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    //---------------------------------------------------------------------//
    //                      4. 장바구니 상품 삭제
    //---------------------------------------------------------------------//
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(
            Principal principal,
            @PathVariable("cartItemId") Long cartItemId
    ){
        String loginId = principal.getName();
        if(!cartService.validateCartItem(cartItemId,loginId)){
            return new ResponseEntity<String>("삭제 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

}
