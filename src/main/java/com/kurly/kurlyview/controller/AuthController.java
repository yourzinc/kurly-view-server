package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> getLogin(@RequestBody SignInRequestDto dto){
        return ResponseEntity.ok(memberService.login(dto));
    }
}
