package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest userJoinRequest){
        //비즈니스 로직 - 회원가입
        //회원 userName(id) 중복 check
        //중복이면 회원가입 x --> Exception(예외) 발생
        userRepository.findByUserName(userJoinRequest.getUserName())
               .ifPresent(user->new RuntimeException("해당 UserName이 중복됩니다."));

        //회원가입 .save()
        User savedUser = userRepository.save(userJoinRequest.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }

}
