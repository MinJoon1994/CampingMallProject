package com.campingmall.myproject.order.entity;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.order.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter@Setter@ToString
public class Order extends BaseEntity {

    @Id@GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; //회원

    @OneToMany(mappedBy = "order",
               cascade =  CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
}
