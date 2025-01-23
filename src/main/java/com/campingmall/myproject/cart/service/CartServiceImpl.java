package com.campingmall.myproject.cart.service;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartItemDTO;
import com.campingmall.myproject.cart.dto.CartOrderDTO;
import com.campingmall.myproject.cart.entity.Cart;
import com.campingmall.myproject.cart.entity.CartItem;
import com.campingmall.myproject.cart.repository.CartItemRepository;
import com.campingmall.myproject.cart.repository.CartRepository;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.repository.ItemRepository;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    //장바구니 상품 주문하기 위한 서비스 요청
    //private final OrderService orderService;

    //---------------------------------------------------------//
    //          1. 상품 장바구니에 담기
    //---------------------------------------------------------//
    @Override
    public Long addCart(CartItemDTO cartItemDTO, String loginId) {

        // 장바구니 상품아이돌 상품정보 조회
        Item item = itemRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        
        // 현재 로그인한 회원 조회
        Member member = memberRepository.findByLoginId(loginId);
        
        // 로그인 한 회원 아이디로 카트 연결
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 회원의 장바구니가 존재하지 않는 경우
        if(cart==null){
            //현재 로그인한 회원의 정보를 가지고 장바구니 생성
            cart = Cart.createCart(member);
            //DB반영
            cartRepository.save(cart);
        }

        //장바구니 상품 : 장바구니에 등록한 상품 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(),item.getId());
        if(savedCartItem != null){
            //장바구니에 있는 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼 증가
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getId();
        }else{
            //장바구니에 없는 상품일 경우
            CartItem cartItem = CartItem.createCartItem(cart,item,cartItemDTO.getCount());
            cartItemRepository.save(cartItem);

            return cartItem.getId();
        }
    }

    //---------------------------------------------------------//
    //          2. 로그인한 회원의 장바구니 상품 조회
    //---------------------------------------------------------//
    @Override
    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String loginId) {
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        //2.1 로그인한 회원의 정보
        Member member = memberRepository.findByLoginId(loginId);
        // 장바구니와 회원아이디 맵핑관계
        Cart cart = cartRepository.findByMemberId(member.getId());

        //2.2
        if(cart==null){
            return cartDetailDTOList = cartItemRepository.findCartDetailDtoList(cart.getId());
        }

        //2.3
        cartDetailDTOList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDTOList;
    }

    //---------------------------------------------------------//
    //          3. 장바구니 수정권한 체크
    //---------------------------------------------------------//
    @Override
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String loginId) {

        //현재 로그인 회원 조회
        Member currentMember = memberRepository.findByLoginId(loginId);

        //장바구니 상품 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        //장바구니 상품 Entity 를 통해 Cart Entity 에 맵핑된 Member Entity 호출 => 장바구니 회원  호출
        Member savedmember = cartItem.getCart().getMember();

        //로그인한 회원과 장바구니 회원이 동일한지 Check
        if(!StringUtils.equals(currentMember.getLoginId(),savedmember.getLoginId())){
            return false;
        }

        return true;
    }

    //---------------------------------------------------------//
    //          4. 장바구니 상품 수량 업데이트
    //---------------------------------------------------------//
    @Override
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    //---------------------------------------------------------//
    //          5. 장바구니 상품 삭제
    //---------------------------------------------------------//
    @Override
    public void deleteCartItem(Long cartItemId) {
        //5.1 장바구니에서 삭제할 상품아이디 검색
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    //---------------------------------------------------------//
    //          6. 장바구니 상품 주문하기
    //---------------------------------------------------------//
    @Override
    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String loginId) {
        return 0L;
    }

    //---------------------------------------------------------//
    //          6. 장바구니 비우기
    //---------------------------------------------------------//
    @Override
    public Long deleteAllCartItem(List<CartOrderDTO> cartOrderDTOList, String loginId) {
        return 0L;
    }
}
