package com.pretest.leesangyub.controller.freelancer;

import com.pretest.leesangyub.common.SessionInfo;
import com.pretest.leesangyub.common.data.CommonRequest;
import com.pretest.leesangyub.common.data.CommonResponse;
import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.common.utils.CommonUtils;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRSB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRSB;
import com.pretest.leesangyub.service.freelancer.FreelancerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="프리랜서 서비스", description = "프리랜서 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/freelancer")
@RestController
public class FreelancerController {

    private final FreelancerService freelancerService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "프리랜서 리스트 조회", description = "프리랜서 - 프리랜서 리스트 조회 서비스 API")
    @PostMapping("/list/search")
    public CommonResponse<FreelancerListRSB> freelancerListSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<FreelancerListRQB> flListRequest ) {

        final var listTrId = "100000";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, flListRequest));

        if(!listTrId.equals( flListRequest.getHeader().getTrId()) )
            throw new AppException(listTrId, AppCode.TRID_INVALID, flListRequest.getHeader().getTrId());

        CommonResponse<FreelancerListRSB> flListResponse =  CommonResponse.ok(listTrId,
                freelancerService.queryFreelancers(
                        listTrId,
                        flListRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, flListResponse));

        return flListResponse;

    }

    @Operation(summary = "프리랜서 조회", description = "프리랜서 - 프리랜서(단일) 조회 서비스 API")
    @PostMapping("/search")
    public CommonResponse<FreelancerSearchRSB> freelancerSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<FreelancerSearchRQB> flSearchRequest ) {

        final var searchTrId = "100010";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, flSearchRequest));

        if(!searchTrId.equals( flSearchRequest.getHeader().getTrId()) )
            throw new AppException(searchTrId, AppCode.TRID_INVALID, flSearchRequest.getHeader().getTrId());

        CommonResponse<FreelancerSearchRSB> flSearchResponse =  CommonResponse.ok(searchTrId,
                freelancerService.getFreelancer(
                        searchTrId,
                        flSearchRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, flSearchResponse));

        return flSearchResponse;
    }

}
