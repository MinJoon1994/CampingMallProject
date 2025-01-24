package com.campingmall.myproject.order.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.order.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.weaver.ast.Or;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter@Setter@ToString
public class Order extends BaseEntity {

    @Id@GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; //회원

    @OneToMany(mappedBy = "order",
               cascade =  CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_address_id") // 명시적으로 컬럼 지정
    private OrderAddress orderAddress;

    private OrderStatus orderStatus;
    private LocalDateTime orderDate;


    //1.주문 상품 정보 담기: 주문상품, 주문서
    public void addOrderItem(OrderItem orderItem){
        //1.1 주문 상품을 주문 상품 목록에 저장
        orderItems.add(orderItem);
        
        //1.2 현재 주문서(Order)정보를 주문상품(OrderItem)에 주문(Order)정보 등록
        orderItem.setOrder(this);
    }

    //2. 주문 내역 구성: 주문 상품 목록, 주문 고객, 주문 상태
    public static Order createOrder(Member member, List<OrderItem> orderItemList,OrderAddress orderAddress){
        //2.1 주문 내역
        Order order = new Order();

        //2.2 현재 로그인 한 고객 정보
        order.setMember(member);

        //2.3 주문 상품목록 저장
        for(OrderItem orderItem: orderItemList){
            order.addOrderItem(orderItem);
        }

        //2.4 주문한 사람, 배송지 정보
        order.setOrderAddress(orderAddress);

        //2.5 주문상태 정보(주문,취소)
        order.setOrderStatus(OrderStatus.ORDER);

        //2.6 주문 날짜
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 3. 주문한 상품 총 금액 계산 처리
    public int getTotalPrice(){
        int totalPrice = 0;

        //주문상품 목록에서 금액을 모두 합산
        for(OrderItem orderItem: orderItems){
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;

    }

    //4. 주문 상품 취소시: 상품재고 재구성
    public void cancelOrder(){
        //주문 상태 -> 주문 취소 전환
        this.orderStatus = OrderStatus.CANCEL;

        //주문 상품 목록에 있는 상품 수량을 가지고 상품재고 수정
        for(OrderItem orderItem: orderItems){
            orderItem.cancel();
        }

    }


}
