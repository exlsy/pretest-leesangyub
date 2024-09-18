package com.pretest.leesangyub.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.Principal;

@EqualsAndHashCode
@ToString
@Getter
@Builder
public class SessionInfo {

    private final Principal principal;
    private final String userId;
    private final String role;
    private final String sessionId;
    private final String userLevel;
    private final String accessLevel;

}
