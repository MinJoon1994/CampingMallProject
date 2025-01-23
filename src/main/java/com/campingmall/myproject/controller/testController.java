package com.campingmall.myproject.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class testController {

    @GetMapping("/test")
    public String mainPage(){
        return "/test";
    }
}
