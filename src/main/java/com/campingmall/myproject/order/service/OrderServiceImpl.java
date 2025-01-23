package com.campingmall.myproject.order.service;

import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.ItemImg;
import com.campingmall.myproject.item.repository.ItemImgRepository;
import com.campingmall.myproject.item.repository.ItemRepository;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import com.campingmall.myproject.order.dto.OrderDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import com.campingmall.myproject.order.dto.OrderItemDTO;
import com.campingmall.myproject.order.entity.Order;
import com.campingmall.myproject.order.entity.OrderItem;
import com.campingmall.myproject.order.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    //1. 상품 주문 서비스(상품 아이디, 수량, 로그인 고객: 주문 고객)
    @Override
    public Long order(OrderDTO orderDTO, String loginId) {

        //1. 주문한 상품 아이디 -> 상품 정보 추출
        Item item = itemRepository.findById(orderDTO.getItemId()).orElseThrow(EntityExistsException::new);

        //2. 현재 료그인 한 회원의 이메일(아이디)를 이용해서 회원 정보 조회
        Member member = memberRepository.findByLoginId(loginId);

        //3. 주문 상품 목록 저장할 List 구조 객체 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        //4. 주문 상품 DTO를 통해 주문 상품 Entity 객체 생성하기
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDTO.getCount());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    //2. 주문 이력(주문 상품 목록) 서비스
    @Override
    @Transactional(readOnly = true)
    public Page<OrderHistoryDTO> getOrderList(String loginId, Pageable pageable) {

        //1. 사용자 아이디와 페이징 조건을 이용해 주문 목록 요청
        List<Order> orders = orderRepository.findOrder(loginId,pageable);

        //2. 총 주문 개수
        Long totalCount = orderRepository.countOrder(loginId);

        //3. 검색하여 가져온 주문 목록을 순회하여 구매이력 페이지 전달할 List 객체 생성
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
        for(Order order: orders){
            OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(order);

            //주문서에 있는 주문 상품 Entity -> DTO 저장
            List<OrderItem> orderItemList = order.getOrderItems();

            for(OrderItem orderitem: orderItemList){
                //1. 대표 상품 이미지: 주문상품이고 대표상품인 조건 검색(and조건)
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderitem.getItem().getId(),"Y");

                //2. 주문한 상품 정보와 대표 상품이미지 url
                //  : 주문상품 이력에 표시할 주문상품 정보 DTO 에 저장
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderitem, itemImg.getImgUrl());

                //3. 주문상품이력 List
                orderHistoryDTO.addOrderItemDTO(orderItemDTO);
            }
            //주문 이력 List 에 주문이력 저장: 주문1, 주문2, ...
            orderHistoryDTOList.add(orderHistoryDTO);
        }
        //페이지 구현 객체 생성
        return new PageImpl<>(orderHistoryDTOList,pageable,totalCount);
    }

    @Override
    public boolean validateOrder(Long orderId, String loginId) {
        return false;
    }

    @Override
    public void cancelOrder(Long orderId) {

    }

    @Override
    public Long orders(List<OrderDTO> orderDTOList, String loginId) {
        return 0L;
    }
}
