package com.campingmall.myproject.order.entity;

import com.campingmall.myproject.order.dto.OrderAddressDTO;
import com.campingmall.myproject.order.dto.OrderRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="order_address")
@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Builder
public class OrderAddress {

    @Id
    @Column(name="order_address_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    //주소 id
    
    private String recipientName; //주문자 이름
    private String postcode;      //우편번호
    private String address;       //주소
    private String detailAddress; //상세주소
    private String extraAddress;  //참고 주소

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    //주문 배송지 정보 만들기
    public static OrderAddress createOrderAddress(OrderAddressDTO orderAddressDTO){
        OrderAddress orderAddress = OrderAddress.builder()
                .recipientName(orderAddressDTO.getRecipientName())
                .postcode(orderAddressDTO.getPostcode())
                .address(orderAddressDTO.getAddress())
                .detailAddress(orderAddressDTO.getDetailAddress())
                .extraAddress(orderAddressDTO.getExtraAddress())
                .build();

        return orderAddress;
    }
}
