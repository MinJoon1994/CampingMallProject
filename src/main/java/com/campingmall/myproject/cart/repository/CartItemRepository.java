package com.campingmall.myproject.cart.repository;

import com.campingmall.myproject.cart.dto.CartDetailDTO;
import com.campingmall.myproject.cart.dto.CartItemDTO;
import com.campingmall.myproject.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    //1. 현재 로그인한 회원이 장바구니와 상품 아아디를 이용해서 장바구니 상품 조회
    CartItem findByCartIdAndItemId(Long cartId,Long ItemId);

    //2. 장바구니 페이지에 전달할 CartDetailDTO List 를 Query문으로 조회
    @Query("""
            select new com.campingmall.myproject.cart.dto.CartDetailDTO(ci.id, i.itemNm, i.price, ci.count, im.imgUrl)
            from
                CartItem ci, ItemImg im
            join ci.item i
            where
                ci.cart.id=:cartId and im.item.id = ci.item.id and im.repImgYn = 'Y'
            order by
                ci.regTime desc
            """)
    List<CartDetailDTO> findCartDetailDtoList(@Param("cartId") Long cartId);
}
