package com.campingmall.myproject.cart.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart_item")
@Getter@Setter@ToString
public class CartItem extends BaseEntity {
    //장바구니 상품
    //장바구니 상품 구조(상품에 대한 장바구니, 상품정보 필수)
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    //하나의 장바구니에는 여러개의 상품을 담을 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart; //FK

    //하나의 상품은 여러 장바구니의 상품으로 연결할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //FK

    private int count;

    //1. 장바구니에 담을 상품 Entity
    public static CartItem createCartItem(Cart cart, Item item, int count) {

        //1.1 장바구니 상품 Entity 생성
        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    //2. 장바구니에 담을 상품 수량 증감처리
    //해당 상품 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더해주는 메서드
    public void addCount(int count) {
        this.count += count; // 기존 수량 증가
    }

    //3. 장바구니 수량변화에 따른 업데이트 처리(상세페이지 View 에서 수량변화에 따른 업데이트)
    public void updateCount(int count){
        this.count=count;
    }

}
