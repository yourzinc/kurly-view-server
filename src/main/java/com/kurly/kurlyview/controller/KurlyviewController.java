package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.KurlyviewSubscribeRequestDto;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Stack;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class KurlyviewController {
    private final MemberService memberService;

    @GetMapping("/kurlyviews")
    public ResponseEntity<?> getKurlyviews(@RequestHeader("Authorization") String token) {
        System.out.println("getKurlyviews");
        return ResponseEntity.ok(memberService.findAllKurlyview(token));
    }

    @GetMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> subscribeKurlyview(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.subscribe(token, memberId));
    }

    @DeleteMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> unsubscribeKurlyview(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.unsubscribe(token, memberId));
    }

    @GetMapping("/kurlyviews/{memberId}/status")
    public ResponseEntity<?> getSubscribeStatus(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.subscribeStatue(token, memberId));
    }
}
