package com.pretest.leesangyub.service.security;

import org.springframework.security.core.Authentication;

public interface SecurityService {
    // rest api 토큰 인증
    Authentication getAuthenticationByToken(String bearerToken);
}
