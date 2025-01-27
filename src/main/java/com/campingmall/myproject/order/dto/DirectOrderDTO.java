package com.campingmall.myproject.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class DirectOrderDTO {

    private Long itemId;
    private Long count;
    private String recipientName;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
}
