package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.domain.dto.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.UserRepository;
import com.example.hospitalreview.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secreatKey;
    private long expireTimeMs = 1000*60*60;         //1시간동안 유효하다

    public UserDto join(UserJoinRequest userJoinRequest){
        //비즈니스 로직 - 회원가입
        //회원 userName(id) 중복 check
        //중복이면 회원가입 x --> Exception(예외) 발생
        userRepository.findByUserName(userJoinRequest.getUserName())
               .ifPresent(user->{
                   throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName:%s",userJoinRequest.getUserName()));
               });

        //회원가입 .save()
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }

    public String login(String userName, String password) {

        // userName 있는지 여부 확인
        // 없으면 NOT_FOUND 에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new HospitalReviewAppException(ErrorCode.NOT_FOUND,String.format("%s는 가입된 적이 없습니다.",userName)));

        // password 일치하는지 여부 확인
        if (!encoder.matches(password, user.getPassword())){
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,String.format("userName 또는 password가 잘못되었습니다."));
        }

        // 두가지 확인 중 예외 안 났으면 Token 발행
        return JwtTokenUtil.createToken(userName,secreatKey,expireTimeMs);
    }
}
