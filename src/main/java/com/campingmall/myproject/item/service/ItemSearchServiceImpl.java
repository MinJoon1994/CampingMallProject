package com.campingmall.myproject.item.service;

import com.campingmall.myproject.item.constant.ItemSellStatus;
import com.campingmall.myproject.item.constant.ItemTypes;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.QItem;
import com.campingmall.myproject.item.entity.QItemImg;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import com.campingmall.myproject.shop.dto.QMainItemDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
@Log4j2
public class ItemSearchServiceImpl implements ItemSearchService{

    //1. 동적 쿼리 생성하기
    private JPAQueryFactory queryFactory;
    public ItemSearchServiceImpl (EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    //2. 조건식 처리 하기

    //2.1 판매유형
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //2.2 아이템 타입
    private BooleanExpression searchItemTypesEq(ItemTypes searchItemTypes){

        if(searchItemTypes!=ItemTypes.ALL) {
            return searchItemTypes == null ? null : QItem.item.itemTypes.eq(searchItemTypes);
        }

        return null;
    }

    //2.3 판매기간
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all",searchDateType)|| searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }

        return QItem.item.regTime.after(dateTime);
    }

    // 2.3 검색 키워드
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }

        return null;
    }

    //2.4 가격
    private OrderSpecifier<?> searchByPrice(String searchPrice){
        if(StringUtils.equals("low",searchPrice)){
            return QItem.item.price.asc();
        }else if(StringUtils.equals("high",searchPrice)){
            return QItem.item.price.desc();
        }else{
            return null;
        }
    }

    //2.5 신상품
    private OrderSpecifier<?> searchByDate(String searchDateType){
        if(StringUtils.equals("desc",searchDateType)){
            return QItem.item.updateTime.desc();
        }
        return null;
    }



    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        //1.조건에 부합하는 자료 조회
        List<Item> itemList = queryFactory
                .selectFrom(QItem.item)
                .where(
                        //기간
                        regDtsAfter(itemSearchDTO.getSearchDateType()),
                        //타입
                        searchItemTypesEq(itemSearchDTO.getItemTypes()),
                        //판매상태
                        searchSellStatusEq(itemSearchDTO.getItemSellStatus()),
                        //검색키워드
                        searchByLike(itemSearchDTO.getSearchBy(),itemSearchDTO.getSearchQuery())
                )
                .orderBy(
                        //가격
                        searchByPrice(itemSearchDTO.getSearchPrice())
                )
                .offset(pageable.getOffset()) // 데이터를 가져올 시작 인덱스
                .limit(pageable.getPageSize()) // 페이지당 가져올 최대 개수
                .fetch(); //조회 대상 리스트 반환

        //2. 조건에 부합하는 자료 개수
        long total = queryFactory
                .select(Wildcard.count).from(QItem.item)
                .where(
                        regDtsAfter(itemSearchDTO.getSearchItemName()),
                        searchSellStatusEq(itemSearchDTO.getItemSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(),itemSearchDTO.getSearchQuery()),
                        searchItemTypesEq(itemSearchDTO.getItemTypes())
                ).fetchOne();

        return new PageImpl<>(itemList, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        //~ from item where itemNm like '%키워드%'
        return StringUtils.isEmpty(searchQuery)?null:QItem.item.itemNm.like("%"+searchQuery+"%");
    }


    @Override
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        //도메인에 있는 상품 Entity, 상품 이미지 Entity 도메인 객체 생성
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        JPAQuery<MainItemDTO> query = queryFactory
                .select(new QMainItemDTO(item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDTO.getSearchQuery()))
                .where(searchItemTypesEq(itemSearchDTO.getItemTypes()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 동적으로 orderBy 추가
        if (itemSearchDTO.getSearchDateType() != null && itemSearchDTO.getSearchPrice()==null) {
            query.orderBy(searchByDate(itemSearchDTO.getSearchDateType()));
        }
        if (itemSearchDTO.getSearchPrice() != null && itemSearchDTO.getSearchDateType()==null) {
            query.orderBy(searchByPrice(itemSearchDTO.getSearchPrice()));
        }

        List<MainItemDTO> content = query.fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDTO.getSearchQuery()))
                .where(//타입
                        searchItemTypesEq(itemSearchDTO.getItemTypes())
                )
                .fetchOne();

        return new PageImpl<>(content,pageable,total);
    }
}
