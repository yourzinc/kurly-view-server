package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.KurlyviewSubscribeRequestDto;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class KurlyviewController {
    private final MemberService memberService;

    @GetMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> subscribeKurlyview(@RequestHeader("X-ACCESS-TOKEN") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.subscribe(token, memberId));
    }

    @DeleteMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> unsubscribeKurlyview(@RequestHeader("X-ACCESS-TOKEN") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.unsubscribe(token, memberId));
    }
}
