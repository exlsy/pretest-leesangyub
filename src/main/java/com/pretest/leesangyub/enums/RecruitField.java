package com.pretest.leesangyub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum RecruitField {
    ALL("ALL", "전체분야"),
    BACKEND("BACKEND", "백엔드 개발"),
    FRONTEND("FRONTEND", "프론트엔드 개발"),
    CROSSAPP("CROSSAPP", "크로스플랫폼 개발"),
    MARKETING("MARKETING", "마켓팅"),
    DESIGN("DESIGN", "디자인"),
    PLANNING("PLANNING", "기획"),
    PM("PM", "프로젝트 관리");

    private final String value;
    private final String description;
}