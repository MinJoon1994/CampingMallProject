package com.campingmall.myproject.item.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.exception.OutOfStockException;
import com.campingmall.myproject.item.constant.ItemSellStatus;
import com.campingmall.myproject.item.constant.ItemTypes;
import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
@Entity
@Table(name = "item")
public class Item extends BaseEntity {
    //상품 기본 정보
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) //시퀀스로 AUTOINCREMENT 실행
    private Long id;                        //상품 코드
    @Column(nullable = false,length=50)
    private String itemNm;                  //상품 이름
    @Column(nullable = false)
    private int price;                      //상품 가격
    @Lob
    @Column(nullable = false)
    private String itemDetail;              //상품 설명
    @Column(nullable = false)
    private int stockNumber;                //재고 수량
    @Enumerated(EnumType.STRING)
    private ItemTypes itemTypes;            //상품 종류
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매상태

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;           //구매 후기

    //--------------------------------------------------------//
    //              기능 수행하는 메서드 정의
    //--------------------------------------------------------//

    //1. 엔티티 필드 수정하는 메서드
    public void  change(String itemNm, String itemDetail){
        this.itemNm=itemNm; this.itemDetail = itemDetail;
    }

    //2. 수정 폼으로부터 변경된 상품정보(DTO)를 Entity에 전달
    public void updateItem(ItemFormDTO itemFormDTO){
        this.itemNm = itemFormDTO.getItemNm();
        this.price = itemFormDTO.getPrice();
        this.stockNumber = itemFormDTO.getStockNumber();
        this.itemDetail = itemFormDTO.getItemDetail();
        this.itemSellStatus = itemFormDTO.getItemSellStatus();
        this.itemTypes = itemFormDTO.getItemTypes();
    }

    //3. 재고 수량 수정하기
    //3.1 주문시 재고수량 업데이터
    public void removeStorck(int stockNumber){
        int resStock = this.stockNumber - stockNumber;
        if(resStock < 0 ){
            //재고량이 부족할 경우 사용자가 작성한 예외 처리 발생
            throw new OutOfStockException("상품 재고가 부족합니다.(현재재고수량:"+this.stockNumber+")");
        }

        //재고 수량이 부족하지 않을 경우
        this.stockNumber = resStock;
    }

    //3.2 주문 취소시 재고수량 업데이트
    public void addStock(int stockNumber){this.stockNumber+=stockNumber;}
}
