package com.campingmall.myproject.item.dto;

import com.campingmall.myproject.entity.BaseEntity;
import com.campingmall.myproject.item.constant.ItemSellStatus;
import com.campingmall.myproject.item.constant.ItemTypes;
import com.campingmall.myproject.item.entity.Item;
import com.campingmall.myproject.item.entity.ItemImg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter@ToString
public class ItemFormDTO {
    private Long id; //상품 이미지 식별 PK
    @NotBlank(message = "상품명은 필수 입력값 입니다.")
    private String itemNm;
    @NotNull(message = "상품가격은 필수 입력값 입니다.")
    private Integer price;
    @NotBlank(message = "상품 상세 설명은 필수 입력값 입니다.")
    private String itemDetail;
    @NotNull(message = "재고 수량은 필수 입력값 입니다.")
    private Integer stockNumber;
    private ItemSellStatus itemSellStatus;
    private ItemTypes itemTypes;

    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemFormDTO of(Item item){
        return modelMapper.map(item, ItemFormDTO.class);
    }

    //상품 저장 후 수정할 때 상품 이미지 정보 저장하는 LIST
    //상품 이미지 Entity -> 상품 이미지 DTO 전환
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

    //상품 이미지 아이디(PK)를 저장하는 LIST
    private List<Long> itemImgIds = new ArrayList<>();


    public Item createItem(){
        return modelMapper.map(this,Item.class);
    }
}
