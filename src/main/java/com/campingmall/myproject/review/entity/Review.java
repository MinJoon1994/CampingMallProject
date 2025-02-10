package com.campingmall.myproject.review.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.review.dto.ReviewImgDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Entity@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;               //리뷰 ID
    @Column(length = 100, nullable = false)
    private String title;           //리뷰 제목
    @Column(length = 500, nullable = false)
    private String content;         //리뷰 내용
    @Column(length = 50, nullable = false)
    private String author;          //리뷰 작성자

    @Column(nullable = false)
    private int star;               //별점

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false) // 외래키 설정
    @JsonBackReference                              // 자식 쪽에서 부모를 참조할 때 사용
    @ToString.Exclude                               // toString() 무한 루프 방지
    private Item item;              //item (FK)


    @OneToMany(mappedBy = "review",
               cascade = CascadeType.REMOVE,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @ToString.Exclude // toString() 무한 루프 방지
    private List<ReviewImg> reviewImgList = new ArrayList<>(); //reviewImg(FK)

    //수정 메서드
    public void change(String title, String content, int star){
        this.title = title;
        this.content = content;
        this.star = star;
    }
}
