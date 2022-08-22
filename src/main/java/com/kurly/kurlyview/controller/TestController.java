package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.TestResponseDto;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import com.kurly.kurlyview.security.jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok(TestResponseDto.builder().message("success").build());
    }
}
