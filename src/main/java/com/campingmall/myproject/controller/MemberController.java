package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.service.ItemService;
import com.campingmall.myproject.member.dto.MemberDTO;
import com.campingmall.myproject.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ItemService itemService;

    //1. 회원 등록 폼
    @GetMapping("/new")
    public String memberForm(ItemSearchDTO itemSearchDTO, Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        model.addAttribute("itemSearchDTO", itemSearchDTO);

        //회원가입 폼 포워딩
        return "/member/memberForm";
    }

    //2. 회원가입 DB 처리
    @PostMapping(value="/new")
    public String memberRegister(
            @Valid MemberDTO memberDTO,
            BindingResult bindingResult,
            Model model){

        log.info("폼에서 넘어온 MemberDTO");
        log.info(memberDTO);

        //서버쪽에서 DTO 데이터 유효성 검사
        //유효성 검사결과 1개이상 에러가 있으면 처리
        if(bindingResult.hasErrors()){
            log.info("=>hasError: "+bindingResult.toString());
            return "member/memberForm";
        }
        try{
            memberService.saveMember(memberDTO);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/login";
    }

    //--------------------------------------------------------//
    //                  로그인, 로그인 에러                   //
    //--------------------------------------------------------//

    //  1. 로그인
    @GetMapping(value = "/login")
    public String memberLoginForm(ItemSearchDTO itemSearchDTO,Model model){
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("memberDTO",new MemberDTO());
        log.info("=>로그인 폼 불러오기");

        //로그인 폼 포워딩
        return "/login/loginForm";
    }
    
    // 2.로그인 에러
    @GetMapping(value = "/login/error")
    public String memberLogout(Model model){
        log.info("=>로그인 에러");
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
        return "login/loginForm";
    }
}
