package com.campingmall.myproject.order.repository;

import com.campingmall.myproject.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
