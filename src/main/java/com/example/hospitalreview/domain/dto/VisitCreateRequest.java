package com.example.hospitalreview.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VisitCreateRequest {

    private Integer hospitalId;
    private String disease;
    private float amount;
}
