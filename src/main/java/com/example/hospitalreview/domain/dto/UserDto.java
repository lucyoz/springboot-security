package com.example.hospitalreview.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String email;
}
