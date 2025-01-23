package com.campingmall.myproject.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    @NotBlank(message = "아이디는 반드시 입력해야 합니다.")
    private String loginId;
    @NotBlank(message = "이름은 반드시 입력해야 합니다.")
    private String name;
    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    private String password;
    @NotBlank(message = "전화번호는 반드시 입력해야 합니다.")
    private String phoneNumber;
    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    private String email;
    @NotBlank(message = "기본주소는 반드시 입력해야 합니다.")
    private String defaultAddress;
    private String addresses;
    private String socialId;
    private List<String> linkedService;

    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean marketingConsent;

}
