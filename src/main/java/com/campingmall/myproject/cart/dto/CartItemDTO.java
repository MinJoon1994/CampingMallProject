package com.campingmall.myproject.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class CartItemDTO {
    @NotNull(message = "아이디는 반드시 필요")
    private Long itemId;
    @Min(value = 1,message = "수량은 최소 1개")
    private int count;
}
