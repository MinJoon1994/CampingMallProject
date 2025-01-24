package com.campingmall.myproject.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor@NoArgsConstructor
@Builder
public class OrderAddressDTO {
    private String recipientName;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
}
