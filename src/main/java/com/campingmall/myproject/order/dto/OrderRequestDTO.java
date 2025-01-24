package com.campingmall.myproject.order.dto;

import com.campingmall.myproject.cart.dto.CartOrderDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter@Setter@ToString
public class OrderRequestDTO {

    private List<CartOrderDTO> cartOrderDTOList;
    private String recipientName;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

}
