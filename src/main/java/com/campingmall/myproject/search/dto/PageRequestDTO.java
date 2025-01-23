package com.campingmall.myproject.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    //현재 기본 페이지 설정
    @Builder.Default
    private int page = 1;

    //페이지당 읽어올 자료 개수
    @Builder.Default
    private int size = 6;

    //아이템 검색 타입 A,B,C,D
    private String type;

    //검색 키워드
    private String keyword;

    //검색 타입구분 처리하는 메서드
    public String[] getTypes(){
        if(type==null || type.isEmpty() || type.isBlank()) return null;

        return type.split("");
    }

    //페이징 초기값 설정
    public Pageable getPageable(String ...props){
        //Pageable 에서 첫번째 페이지 번호: 0부터 설정, 1페이지를 0페이지 맵핑 형태
        return PageRequest.of(this.page-1,this.size, Sort.by(props).descending());
    }

    //요청할 url에 전달할 페이징 및 검색 관련 매개변수 설정
    private String link;
    public String getLink(){
        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page="+this.page);
            builder.append("&size="+this.size);

            //검색 유형 유무 처리
            if(type != null && !type.isEmpty()){
                builder.append("&type="+type);
            }

            //검색어 유무 처리
            if(keyword != null){
                try{
                    builder.append("&keyword="+ URLEncoder.encode(keyword,"UTF-8"));
                }catch (UnsupportedEncodingException e){
                    throw new RuntimeException(e);
                }
            }//end if

            link = builder.toString();
        }

        return link;
    }
}
