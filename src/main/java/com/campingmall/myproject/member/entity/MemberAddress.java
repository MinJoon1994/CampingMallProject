package com.campingmall.myproject.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class MemberAddress {

    @Id
    @Column(name="member_address_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;    //멤버 주소 아이디

    private String address; //우편 번호

    private String streetaddress; //지번 주소

    private String detailaddress; //상세 주소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
}
