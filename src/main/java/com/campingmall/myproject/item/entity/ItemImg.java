package com.campingmall.myproject.item.entity;

import com.campingmall.myproject.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
@Entity
public class ItemImg extends BaseEntity {
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    // 연관관계 설정 : 다른 테이블과 조인
    // 1개의 상품 엔티티에는 여러개의 상품 이미지 엔티티와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    //업데이트 처리하는 메서드(원본이미지 파일명, 이미지 파일명, 이미지 경로)
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName=oriImgName;
        this.imgName=imgName;
        this.imgUrl=imgUrl;
    }
}
