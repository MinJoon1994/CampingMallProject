package com.campingmall.myproject.exception;

//상품 주문수량보다 재고의 수가 적을 경우 발생시킬 Exception 정의
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
