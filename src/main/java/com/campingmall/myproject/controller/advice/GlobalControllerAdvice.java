package com.campingmall.myproject.config;

import com.campingmall.myproject.security.CustomUserPrincipal;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@Log4j2
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserInfoToModel(Model model, @AuthenticationPrincipal Object principal) {
        String username = null;

        if (principal instanceof CustomUserPrincipal userPrincipal) {
            username = userPrincipal.getUsername();
        } else if (principal instanceof OAuth2User oAuth2User) {
            String name = (String) oAuth2User.getAttributes().get("name");
            String email = (String) oAuth2User.getAttributes().get("email");
            username = (name != null ? name : email);
        }

        model.addAttribute("username", username);
    }
}
