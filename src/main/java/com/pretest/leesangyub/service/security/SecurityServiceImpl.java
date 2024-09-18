package com.pretest.leesangyub.service.security;

import com.pretest.leesangyub.common.SessionInfo;
import com.pretest.leesangyub.module.JwtDecoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {

    private JwtDecoder jwtDecoder = new JwtDecoder();

    public Authentication getAuthenticationByToken(String jwtToken) {
        try {
            if(!StringUtils.hasText(jwtToken))
                return null;

            final var decInfos = jwtDecoder.decode(jwtToken);
            final var userId = (String)decInfos.get("USER_ID");
            final var role = (String)decInfos.get("ROLE");
            final var sessionId = (String)decInfos.get("SESSION_ID");
            final var userLevel = (String)decInfos.get("USER_LEVEL");
            final var accessLevel = (String)decInfos.get("ACCESS_LEVEL");

            // jwtDecoder.decode(jwtToken);
            log.info("jwtToken:{}", jwtToken);
            log.info("JWT Decode {}", decInfos.toString());

            return new UsernamePasswordAuthenticationToken(
                    SessionInfo.builder()
                            .userId(userId)
                            .role(role)
                            .accessLevel(accessLevel)
                            .sessionId(sessionId)
                            .userLevel(userLevel)
                            .build(),
                    decInfos);

        } catch(JsonProcessingException e) {
            log.error("error ***** {}", e.getMessage());
            return null;
        }
    }
}

