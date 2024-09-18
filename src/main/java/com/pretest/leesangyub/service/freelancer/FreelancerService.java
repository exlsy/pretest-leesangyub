package com.pretest.leesangyub.service.freelancer;

import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRSB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRSB;


public interface FreelancerService {

    // 프리랜서 프로필 테이블 생성하기
    String createFreelancerTable();

    // 프리랜서 프로필 테이블 카운트
    Long getFreelancerCount();

    // 샘플 데이터 로딩하기
    void loadFreelancerSamples();

    // 프리랜서 리스트 조회 서비스
    FreelancerListRSB queryFreelancers(String trId, FreelancerListRQB listRQB) throws AppException;

    // 프리랜서 조회 서비스 -> 조회수 업데이트
    FreelancerSearchRSB getFreelancer(String trId, FreelancerSearchRQB listRQB) throws AppException;

}
