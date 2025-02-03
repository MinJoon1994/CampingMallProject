package com.campingmall.myproject.order.service;

import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.ItemImg;
import com.campingmall.myproject.item.repository.ItemImgRepository;
import com.campingmall.myproject.item.repository.ItemRepository;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import com.campingmall.myproject.order.dto.OrderAddressDTO;
import com.campingmall.myproject.order.dto.OrderDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import com.campingmall.myproject.order.dto.OrderItemDTO;
import com.campingmall.myproject.order.entity.Order;
import com.campingmall.myproject.order.entity.OrderAddress;
import com.campingmall.myproject.order.entity.OrderItem;
import com.campingmall.myproject.order.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    public Long order(OrderDTO orderDTO, String loginId, OrderAddressDTO orderAddressDTO) {

        //1. 주문한 상품 아이디 -> 상품 정보 추출
        Item item = itemRepository.findById(orderDTO.getItemId()).orElseThrow(EntityExistsException::new);

        //2. 현재 료그인 한 회원의 이메일(아이디)를 이용해서 회원 정보 조회
        Member member = memberRepository.findByLoginId(loginId);

        //3. 주문 상품 목록 저장할 List 구조 객체 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        //4. 주문 상품 DTO를 통해 주문 상품 Entity 객체 생성하기
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDTO.getCount());

        orderItemList.add(orderItem);

        //5. 주문 상품 주소 생성
        OrderAddress orderAddress = OrderAddress.createOrderAddress(orderAddressDTO);

        Order order = Order.createOrder(member,orderItemList,orderAddress);
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

    //2-1 주문 이력(구매후기 리뷰를 위한) 서비스
    @Override
    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getOrderListReview(String loginId) {

        //1. 사용자의 아이디를 이용해 주문 목록 요청
        List<Order> orders = orderRepository.findOrderReview(loginId);

        //2. 검색하여 가져온 주문 목록을 순회하여 구매이력 페이지 전달할 List 객체 생성
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
        for(Order order: orders){
            OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(order);

            //주문서에 있는 주문 상품 Entity -> DTO 저장
            List<OrderItem> orderItemList = order.getOrderItems();

            for(OrderItem orderItem: orderItemList){
                //1. 대표 상품 이미지: 주문상품이고 대표상품인 조건 검색(and조건)
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(),"Y");

                //2. 주문한 상품 정보와 대표 상품이미지 Url
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl());

                //3. 주문상품이력 List
                orderHistoryDTO.addOrderItemDTO(orderItemDTO);
            }
            //주문 이력 List 에 주문 이력 저장
            orderHistoryDTOList.add(orderHistoryDTO);
        }
        //주문이력 List 반환
        return orderHistoryDTOList;
    }

    @Override
    public Long getOrderCount(String loginId) {

        //2. 총 주문 개수
        Long totalCount = orderRepository.countOrder(loginId);

        return totalCount;
    }


    //3. 주문 취소시 : 로그인 한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
    @Override
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String loginId) {
        //현재 로그인 회원의 이메일 정보
        Member currentMember = memberRepository.findByLoginId(loginId);

        //주문서에 등록된 회원 정보(로그인아이디) : 실제 주문한 회원 로그인 아이디
        Order order = orderRepository.findById(orderId).orElseThrow(EntityExistsException::new);

        Member savedMember = order.getMember();

        //현재 로그인한 회원 이메일과 주문서에 등록된 회원이메일 동일한지 검사
        if(!StringUtils.equals(currentMember.getLoginId(),savedMember.getLoginId()))
            return false;

        return true; //동일한 회원이면 true 반환
    }
    
    
    //4. 주문취소시: transaction 변경 감지 기능에 의해서 트렌젝션이 끝날 때 update 쿼리 실행
    //  : 주문 취소기능 수행 => order entity 에 주문 상태를 취소로 변경 
    //  orderItem entity 를 통해 Item Entity 재고 수량 변경
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();
    }
    
    //5. 주문상품들 주문 서비스
    @Override
    public Long orders(List<OrderDTO> orderDTOList, String loginId, OrderAddressDTO orderAddressDTO) {

        Member member = memberRepository.findByLoginId(loginId);
        List<OrderItem> orderItemList = new ArrayList<>();



        for(OrderDTO orderDTO: orderDTOList){



            log.info(orderDTO.getItemId());

            //5.1 orderDTO: 주문상품 아이디, 수량
            Item item = itemRepository.findById(orderDTO.getItemId()).orElseThrow(EntityNotFoundException::new);

            //5.2 orderDTO 값을 통해 OrderItem Entity 객체 생성
            OrderItem orderItem = OrderItem.createOrderItem(item,orderDTO.getCount());



            //5.3 생성된 orderItem Entity 를 List 구조 객체에 저장
            orderItemList.add(orderItem);
        }

        log.info("여기까지 잘 들어 왔니?5");

        //5.4 orderAddressDTo 값을 통해 OrderAddress Entity 객체 생성
        OrderAddress orderAddress = OrderAddress.createOrderAddress(orderAddressDTO);

        //5.5 주문 상품(OrderItem) Entity와 주문(Order) Entity 연관 맵핑
        Order order = Order.createOrder(member,orderItemList,orderAddress);

        orderRepository.save(order);

        return order.getId();
    }
}
