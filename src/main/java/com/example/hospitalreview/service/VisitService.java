package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.Hospital;
import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.Visit;
import com.example.hospitalreview.domain.dto.VisitCreateRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.HospitalRepository;
import com.example.hospitalreview.repository.ReviewRepository;
import com.example.hospitalreview.repository.UserRepository;
import com.example.hospitalreview.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;

    public void createVisit(VisitCreateRequest dto, String userName){

        //hospital이 없을 때 등록불가
        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(()-> new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("hospitalId:%s 가 없습니다.",dto.getHospitalId())));

        //user가 없을 때 등록불가
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("user:%s 가 없습니다.",userName)));

        Visit visit = Visit.builder()
                .user(user)
                .hospital(hospital)
                .disease(dto.getDisease())
                .amount(dto.getAmount())
                .build();
        visitRepository.save(visit);

    }
}
