package com.campingmall.myproject.review.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Entity@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rno;               //리뷰 ID
    @Column(length = 100, nullable = false)
    private String title;           //리뷰 제목
    @Column(length = 500, nullable = false)
    private String content;         //리뷰 내용
    @Column(length = 50, nullable = false)
    private String author;          //리뷰 작성자

    private String loginId;         //작성자 로그인 아이디

    @Column(nullable = false)
    private int star;               //별점

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false) // 외래키 설정
    private Item item;              //item (FK)

    //수정 메서드
    public void change(String title, String content, int star){
        this.title = title;
        this.content = content;
        this.star = star;
    }
}
