package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import com.campingmall.myproject.order.service.OrderService;
import com.campingmall.myproject.review.dto.ReviewDTO;
import com.campingmall.myproject.review.service.ReviewService;
import com.campingmall.myproject.search.dto.PageRequestDTO;
import com.campingmall.myproject.search.dto.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;
    
    //1. Review 목록
    @GetMapping(value = "/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, ItemSearchDTO itemSearchDTO){
        
        //Review 목록
        PageResponseDTO<ReviewDTO> pageResponseDTO = reviewService.list(pageRequestDTO);
        
        model.addAttribute("pageResponseDTO",pageResponseDTO);
        
        //디스패치 했을 경우 pageRequestDTO 객체 공유가 되지 않을 경우 설정
        model.addAttribute("pageRequestDTO",pageRequestDTO);
        model.addAttribute("itemSearchDTO",itemSearchDTO);

        return "review/reviewList";
        
    }

    //2. Review 등록
    //2.1 Review 등록폼 불러오기
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/register")
    public String register(Model model,
                           Principal principal,
                           ItemSearchDTO itemSearchDTO){

        //현재 로그인한 회원의 로그인 아이디를 인자로 전달하여 구매이력 조회
        List<OrderHistoryDTO> orderHistoryDTOList = orderService.getOrderListReview(principal.getName());

        model.addAttribute("orders",orderHistoryDTOList);
        model.addAttribute("itemSearchDTO", itemSearchDTO);

        return "review/register";
    }

    //2.2 Review 등록 처리
    //현재 로그인 사용자 계정(loginId)과 게시글 작성자 loginId가 동일한지 판별
    @PreAuthorize("principal.username == #reviewDTO.author")
    @PostMapping(value = "/register")
    public String registerPost(@Valid ReviewDTO reviewDTO,
                               BindingResult bindingResult,
                               @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList,
                               //redirect 방식 요청시 : 정보관리 객체로 1회용
                               RedirectAttributes redirectAttributes){
        log.info("에러발생 ============================");
        log.info(bindingResult.hasErrors());

        //클라이언트로 부터 전송받은 ReviewDTO 문제가 발생했을 경우
        if(bindingResult.hasErrors()){
            //@Valid 어노테이션으로 유효성 검사 결과 오류가 있는 필드만 추출하여 저장
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/review/register";
        }

        //리뷰 등록 서비스 호출
        try {
            Long rno = reviewService.register(reviewDTO, reviewImgFileList);
            redirectAttributes.addFlashAttribute("result","리뷰가 등록되었습니다.");
            redirectAttributes.addFlashAttribute("rno",rno);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage","리뷰 등록 중 에러가 발생하였습니다.");
            return "redirect:/review/register";
        }


        return "redirect:/review/list";
    }

}
