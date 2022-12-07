package com.example.hospitalreview.controller;

import com.example.hospitalreview.domain.dto.VisitCreateRequest;
import com.example.hospitalreview.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<String> createVisitLog(@RequestBody VisitCreateRequest visitCreateRequest, Authentication authentication){

        String userName = authentication.getName();
        visitService.createVisit(visitCreateRequest,userName);
        return ResponseEntity.ok().body("방문 기록이 등록되었습니다.");
    }

}
