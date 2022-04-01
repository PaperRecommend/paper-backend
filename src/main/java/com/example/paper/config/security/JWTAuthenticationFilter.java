package com.example.paper.config.security;

import com.example.paper.entity.po.UserPO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.objectMapper = new ObjectMapper();
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
//            UsernamePasswordAuthenticationToken 继承 AbstractAuthenticationToken 实现 Authentication
//            所以当在页面中输入用户名和密码之后首先会进入到 UsernamePasswordAuthenticationToken验证(Authentication)，
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NoneTokenException e) {
            logger.info(e.getMessage());
        } catch (InvalidTokenException e){
            logger.info(e.getMessage());
        } catch (ExpiredTokenException e){
            logger.info(e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws NoneTokenException, InvalidTokenException,ExpiredTokenException {
        String header = request.getHeader(JwtTokenUtils.TOKEN_NAME);
        if(header==null){
            throw new NoneTokenException("无验证");
        }
        try{
            JwtTokenUtils.getClaimsFromToken(header);
        }catch (ExpiredJwtException e){
            throw new ExpiredTokenException("token已过期");
        }catch (Exception e){
            throw new InvalidTokenException("解析失败");
        }
        String username = JwtTokenUtils.getUsernameFromToken(header);
        int id = JwtTokenUtils.getIdFromToken(header);
        List<GrantedAuthority> authorities = new ArrayList<>();
        logger.info("username：" + username);
        logger.info("id：" + id);
        logger.info("create time：" + JwtTokenUtils.getCreatedDateFromToken(header));
        logger.info("expired time：" + JwtTokenUtils.getExpirationDateFromToken(header));
//            踩坑提醒 此处password不能为null
        UserPO principal = new UserPO().setName(username).setId(id).setPassword("");
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private class InvalidTokenException extends Exception {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    private class NoneTokenException extends Exception {
        public NoneTokenException(String message) {
            super(message);
        }
    }

    private class ExpiredTokenException extends Exception {
        public ExpiredTokenException(String message) {
            super(message);
        }
    }
}
