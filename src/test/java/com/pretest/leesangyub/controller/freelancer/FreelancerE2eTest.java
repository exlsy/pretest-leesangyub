package com.pretest.leesangyub.controller.freelancer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pretest.leesangyub.common.data.CommonReqHeader;
import com.pretest.leesangyub.common.data.CommonRequest;
import com.pretest.leesangyub.dto.AppDTO;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRQB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FreelancerE2eTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    // @Autowired
    // private FreelancerController freelancerController;


    @Test
    @DisplayName("프리랜서 리스트 조회 성공 테스트") // 페이징 결과
    public void testFreelancerListSearch_Success() throws Exception {

        // Given -- 현재 테스트 데이터가 11건이 입력되어 있는 상태.
        String listTrId = "100000";
        List<AppDTO.SortObj> sortObjs = new ArrayList<>();
        sortObjs.add( AppDTO.SortObj.builder().field("created_at").order("ASC").build());

        FreelancerListRQB requestBody = FreelancerListRQB.builder()
                .isHeaderInfo(true)
                .rowCnt(5)
                .startNum(0)
                .flName("")
                .recruitField("ALL")
                .sortObjs( sortObjs )
                .build();
        CommonRequest<FreelancerListRQB> request = CommonRequest.<FreelancerListRQB>builder()
                .header(CommonReqHeader.builder().trId(listTrId).build())
                .body(requestBody)
                .build();

        // When & then
        mockMvc.perform(post("/freelancer/list/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                        // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.totalCnt").value(11))
                .andExpect(jsonPath("$.body.headerInfos").exists())
                .andExpect(jsonPath("$.body.freelancerList").exists())
                .andExpect(jsonPath("$.body.freelancerList.length()").value(5))
                // .andExpect(jsonPath("$.body.freelancerList").)
                .andDo(print());

    }

    @Test
    @DisplayName("프리랜서 리스트 조건 조회  성공 테스트") // 페이징 결과
    public void testFreelancerListSearch_Success2() throws Exception {

        // Given -- 현재 테스트 데이터가 11건이 입력되어 있는 상태. BACKEND는 3건
        String listTrId = "100000";
        List<AppDTO.SortObj> sortObjs = new ArrayList<>();
        sortObjs.add( AppDTO.SortObj.builder().field("created_at").order("ASC").build());

        FreelancerListRQB requestBody = FreelancerListRQB.builder()
                .isHeaderInfo(true)
                .rowCnt(10)
                .startNum(0)
                .flName("")
                .recruitField("BACKEND")
                .sortObjs( sortObjs )
                .build();
        CommonRequest<FreelancerListRQB> request = CommonRequest.<FreelancerListRQB>builder()
                .header(CommonReqHeader.builder().trId(listTrId).build())
                .body(requestBody)
                .build();

        // Mocking freelancerService behavior
        // freelancerController.freelancerListSearch(null, request);

        // When & then
        mockMvc.perform(post("/freelancer/list/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.totalCnt").value(3))
                .andExpect(jsonPath("$.body.headerInfos").exists())
                .andExpect(jsonPath("$.body.freelancerList").exists())
                .andExpect(jsonPath("$.body.freelancerList.length()").value(3))
                // .andExpect(jsonPath("$.body.freelancerList").)
                .andDo(print());

    }

    @Test
    @DisplayName("잘못된 TrId로 인한 Excepton 테스트")
    public void testFreelancerListSearch_TrIdInvalid() throws Exception {
        // Given
        String listTrId = "100100";  // 100000

        FreelancerListRQB requestBody = FreelancerListRQB.builder()
                .isHeaderInfo(true)
                .rowCnt(5)
                .startNum(0)
                .flName("")
                .recruitField("ALL")
                // .sortObjs( sortObjs )
                .build();
        CommonRequest<FreelancerListRQB> request = CommonRequest.<FreelancerListRQB>builder()
                .header(CommonReqHeader.builder().trId(listTrId).build())
                .body(requestBody)
                .build();;

        // When & Then
        mockMvc.perform(post("/freelancer/list/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // AppException으로 인해 400 오류 발생
                .andExpect(jsonPath("$.header.rtnCode").value("E120010"))
                .andDo(print());
    }

    @Test
    @DisplayName("잘못된 recruitField로 인한 Excepton 테스트")
    public void testFreelancerListSearch_RecruitFieldInvalid() throws Exception {
        // Given
        String listTrId = "100000";  // 100000

        FreelancerListRQB requestBody = FreelancerListRQB.builder()
                .isHeaderInfo(true)
                .rowCnt(5)
                .startNum(0)
                .flName("")
                .recruitField("CEO")
                .build();
        CommonRequest<FreelancerListRQB> request = CommonRequest.<FreelancerListRQB>builder()
                .header(CommonReqHeader.builder().trId(listTrId).build())
                .body(requestBody)
                .build();;

        // When & Then
        mockMvc.perform(post("/freelancer/list/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // AppException으로 인해 400 오류 발생
                .andExpect(jsonPath("$.header.rtnCode").value("E120003"))
                .andDo(print());
    }

    @Test
    @DisplayName("프리랜서 단일 조회 성공 테스트") // 페이징 결과
    public void testFreelancerSearch_Success() throws Exception {

        // Given
        String searchTrId = "100010";

        FreelancerSearchRQB requestBody = FreelancerSearchRQB.builder()
                .flId("fl_00000001")
                .build();

        CommonRequest<FreelancerSearchRQB> request = CommonRequest.<FreelancerSearchRQB>builder()
                .header(CommonReqHeader.builder().trId(searchTrId).build())
                .body(requestBody)
                .build();

        // When & then
        mockMvc.perform(post("/freelancer/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("000000"))
                .andExpect(jsonPath("$.body.flId").value("fl_00000001"))
                .andExpect(jsonPath("$.body.flName").value("김소이"))
                .andExpect(jsonPath("$.body.recruitField").value("BACKEND"))
                .andDo(print());

    }

    @Test
    @DisplayName("프리랜서 단일 조회 실패 테스트") // 페이징 결과
    public void testFreelancerSearch_FlIdNotExist() throws Exception {

        // Given
        String searchTrId = "100010";

        FreelancerSearchRQB requestBody = FreelancerSearchRQB.builder()
                .flId("fl_00000020")
                .build();

        CommonRequest<FreelancerSearchRQB> request = CommonRequest.<FreelancerSearchRQB>builder()
                .header(CommonReqHeader.builder().trId(searchTrId).build())
                .body(requestBody)
                .build();

        // When & then
        mockMvc.perform(post("/freelancer/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("E120001"))
                .andExpect(jsonPath("$.header.rtnMessage").exists())
                .andDo(print());

    }
}
