package com.campingmall.myproject.order.entity;

import com.campingmall.myproject.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.Stack;

@Entity
@Table(name="order_item")
@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
public class OrderItem {

    @Id@GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
    
    //주문 상품 정보, 수량
    public static OrderItem createOrderItem(Item item, int count){

        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        //주문 상품 수량만큼 재고 수량 감소
        item.removeStorck(count);
        return orderItem;
    }

    //주문 상품 금액 계산
    public int getTotalPrice(){return orderPrice * count; }

    //주문 취소 => 주문 상품 수량만큼 재고 수량 증가
    public void cancel(){this.getItem().addStock(count);}
    

}
