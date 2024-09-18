package com.pretest.leesangyub;

import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.service.freelancer.FreelancerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class AppInitializer implements CommandLineRunner {

    @Autowired
    private FreelancerService freelancerService;

    @Override
    public void run(String... args) throws Exception {
        log.info("AppInitializer Args: {}", Arrays.toString(args));

        String createRtn = freelancerService.createFreelancerTable();
        if(AppConstants.FAIL.equals(createRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }
        if( freelancerService.getFreelancerCount() <=0) {
            log.info("----- 샘플데이터를 로딩합니다.");
            freelancerService.loadFreelancerSamples();
        }

    }


}
