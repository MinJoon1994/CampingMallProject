package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.dto.ItemFormDTO;
import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.order.dto.OrderHistoryDTO;
import com.campingmall.myproject.order.service.OrderService;
import com.campingmall.myproject.review.dto.ReviewDTO;
import com.campingmall.myproject.review.dto.ReviewFormDTO;
import com.campingmall.myproject.review.entity.Review;
import com.campingmall.myproject.review.service.ReviewService;
import com.campingmall.myproject.search.dto.PageRequestDTO;
import com.campingmall.myproject.search.dto.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        log.info("======== shop/list prev/next ========");
        log.info("prev: "+pageResponseDTO.isPrev());
        log.info("next: "+pageResponseDTO.isNext());

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
        model.addAttribute("reviewFormDTO",new ReviewFormDTO());
        model.addAttribute("orders",orderHistoryDTOList);
        model.addAttribute("itemSearchDTO", itemSearchDTO);

        return "review/register";
    }

    //2.2 Review 등록 처리
    //현재 로그인 사용자 계정(loginId)과 게시글 작성자 loginId가 동일한지 판별
    @PreAuthorize("principal.username == #reviewDTO.author")
    @PostMapping(value = "/register")
    public String registerPost(@Valid ReviewFormDTO reviewFormDTO,
                               BindingResult bindingResult,
                               @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList,
                               //redirect 방식 요청시 : 정보관리 객체로 1회용
                               RedirectAttributes redirectAttributes){
        log.info("에러발생 ============================");
        log.info(bindingResult.hasErrors());

        log.info("================ reviewFormDTO ==================");
        log.info(reviewFormDTO);
        for (MultipartFile file : reviewImgFileList) {
            log.info("파일 이름: {}", file.getOriginalFilename());
        }


        //클라이언트로 부터 전송받은 ReviewDTO 문제가 발생했을 경우
        if(bindingResult.hasErrors()){
            //@Valid 어노테이션으로 유효성 검사 결과 오류가 있는 필드만 추출하여 저장
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/review/register";
        }



        //리뷰 등록 서비스 호출
        try {
            Long id = reviewService.register(reviewFormDTO, reviewImgFileList);
            redirectAttributes.addFlashAttribute("result","리뷰가 등록되었습니다.");
            redirectAttributes.addFlashAttribute("id",id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage","리뷰 등록 중 에러가 발생하였습니다.");
            return "redirect:/review/register";
        }


        return "redirect:/review/list";
    }


    //3. Review 자세히보기
    @GetMapping("/read/{id}")
    public String read(@PathVariable ("id") Long id, PageRequestDTO pageRequestDTO, Model model, ItemSearchDTO itemSearchDTO){

        //리뷰 자세히 보기 서비스 요청
        ReviewDTO reviewDTO = reviewService.readOne(id);

        model.addAttribute("itemSerachDTO",itemSearchDTO);
        model.addAttribute("dto",reviewDTO);

        return "/review/read";
    }
    
    //4. Review 삭제
    @PreAuthorize("principal.username == #review.author") // ✅ Review 엔티티의 author 값과 비교
    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        try {
            reviewService.delete(id); // ✅ 삭제 실행
            return ResponseEntity.ok("삭제되었습니다."); // ✅ 성공 시 200 OK 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage()); // ✅ 실패 시 500 에러 반환
        }
    }
    
    //5. Review 수정
    
    //5-1. Review 수정폼 불러오기
    @PreAuthorize("principal.username == #review.author") // ✅ Review 엔티티의 author 값과 비교
    @GetMapping(value = "/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model,ItemSearchDTO itemSearchDTO){
        ReviewDTO reviewDTO = reviewService.readOne(id);
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("dto",reviewDTO);

        return "/review/modify";
    }
    
    
    //5-2. Review 수정폼 처리
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         @Valid ReviewDTO reviewDTO,
                         @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList,
                         RedirectAttributes redirectAttributes){

        log.info("===============후기 수정폼===============");
        log.info("=>id: "+id);
        log.info("=>reviewDTO: "+reviewDTO);
        log.info("=>reviewImgFileList: "+reviewImgFileList);
        
        try {

            reviewService.modify(id,reviewDTO,reviewImgFileList);
            redirectAttributes.addFlashAttribute("result","리뷰가 수정되었습니다.");
            redirectAttributes.addFlashAttribute("id",id);

            return "redirect:/review/read/"+id;

        }catch (Exception e){
            
            log.info("후기 수정 오류 발생");
            log.info(e.getMessage());
            
            redirectAttributes.addFlashAttribute("errorMessage","리뷰 등록 중 에러가 발생하였습니다.");
            return "redirect:/review/modify/"+id;

        }
    }

}
