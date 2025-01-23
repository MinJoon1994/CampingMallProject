package com.campingmall.myproject.member.service;

import com.campingmall.myproject.member.dto.MemberDTO;
import com.campingmall.myproject.member.entity.Member;

public interface MemberService {

    public Member saveMember(MemberDTO memberDTO);

}
