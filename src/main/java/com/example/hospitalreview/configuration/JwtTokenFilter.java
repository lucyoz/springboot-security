package com.example.hospitalreview.configuration;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.service.UserService;
import com.example.hospitalreview.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader:{}",authorizationHeader);     //header에 Authorization이 없다면 null

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        //token분리

        String token = "";
        try {
            token = authorizationHeader.split(" ")[1];
            JwtTokenUtil.isExpired(token, secretKey);
        } catch (Exception e) {
            log.error("token 추출에 실패했습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        //token에서 userName분리
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName:{}", userName);

        // UserDetail
        User user = userService.getUserByUserName(userName);
        log.info("userRole:{}",user.getRole());


        // 권한 주거나 안 주기[입구]
        // 개찰구 역할: 현재는 모두 닫혀 있음
        // Token 이용해서 문 열기, Role 바인딩
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, Arrays.asList(new SimpleGrantedAuthority(user.getRole().name()))); //java8이라서 Arrays 씀. java9부터는 List.of()
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);


    }

}
