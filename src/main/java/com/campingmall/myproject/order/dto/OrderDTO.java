package com.campingmall.myproject.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter@Setter@ToString
public class OrderDTO {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Min(value =1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 100, message = "최대 주문 수량은 100개 입니다.")
    private int count;
}
