package com.campingmall.myproject.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Getter@Setter
public class PageResponseDTO<E> {

    private int page;   //현재 페이지번호
    private int size;   //페이지당 읽어올 자료 개수
    private int total;  //총 자료 개수

    private int start;  //시작 페이지 인덱스
    private int end;    //끝 페이지 인덱스

    private boolean prev; //이전 페이지 유무 판별
    private boolean next; //다음 페이지 유무 판별
    
    private List<E> dtoList; //페이지에 보여질 자료 보관

    //생성자: 페이징 초기회
    @Builder(builderMethodName = "withAll") //메서드 단위
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        //자료가 없으면 메서드 실행 종료
        if(total<0) return;
        
        this.page=pageRequestDTO.getPage(); //요청한 페이지 번호
        this.size=pageRequestDTO.getSize(); //페이지당 보여질 자료의 개수
        this.total=total;
        this.dtoList = dtoList;

        //페이지 블럭에서 페이지 범위 계산 처리
        // 현재 페이지 번호/10.0 = > 자리올림 = > *10 : 현재페이지에 해당되는 페이지블럭의 끝번호 생성
        // 1-10사이 => 1*10 => 10, 11~20사이 => 2=> 2*10 => 20,....
        this.end = (int)(Math.ceil(this.page/10.0)) *10;

        //현재 페이지 번호에 해당되는 페이지 블럭의 시작번호 생성
        this.start = this.end -9;

        //총 페이지 수 = 총 레코드(지료)개수 / 1페이지 보여질 data 개수 => 소수점 이하는 자리올림
        int last = (int) (Math.ceil(total/(double)size));

        // 마지막 페이지 번호 설정: 페이지 블럭의 끝번호가 총페이지수 보다 크면 총페이지수가 마지막 페이지
        this.end = end>last? last: end;

        //페이지 블럭의 시작페이지가 1보다 크면 prev 에 true 설정
        this.prev = this.start>1;

        //총페이지수가 전체 레코드 총 개수보다 크면 false
        this.next = total>this.end * this.size;

    }
}
