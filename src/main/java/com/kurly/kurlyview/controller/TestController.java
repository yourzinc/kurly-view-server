package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.TestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
