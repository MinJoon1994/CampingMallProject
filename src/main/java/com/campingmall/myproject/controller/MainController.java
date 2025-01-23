package com.campingmall.myproject.controller;

import com.campingmall.myproject.item.dto.ItemSearchDTO;
import com.campingmall.myproject.item.service.ItemService;
import com.campingmall.myproject.shop.dto.MainItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;
    @GetMapping("/")
    public String mainPage(ItemSearchDTO itemSearchDTO, Model model){

        model.addAttribute("itemSearchDTO",itemSearchDTO);

        return "/main/main";
    }
}
