package com.pretest.leesangyub.manager;

import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.repository.pri.FreelancerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class FreelancerManager {

    @Value("${taskapp.freelancer.schedule_interval}")
    private long scheduleInterval;

    @Autowired
    private final FreelancerRepository freelancerRepository;

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    // 테이블 생성
    synchronized public String createTable(String table, Function<String,String> handleTable ) {

        return handleTable.apply(table);

    }

    //
    public void increaseViewCount(String flId) {
        String cacheKey = AppConstants.REDIS_KEY_PROFILEVIEWCNT+"::"+flId;

        // Redis에서 조회 수 가져오기
        Integer viewCount = (Integer) redisTemplate.opsForValue().get(cacheKey);

        if (viewCount == null) {
            // Redis에 값이 없으면 DB에서 조회
            viewCount = freelancerRepository.getViewCntByFlId(flId);
        }

        viewCount++;

        // Redis에 업데이트된 조회 수 저장
        redisTemplate.opsForValue().set(cacheKey, viewCount);
    }

    //
    @Scheduled(fixedRateString = "${taskapp.freelancer.schedule_interval}")
    public void updateViewCounts() {
        log.info("FreelancerManager updateViewCounts -------------------- ");

        // Redis에서 조회 수가 저장된 키 가져오기
        Set<String> keys = redisTemplate.keys(AppConstants.REDIS_KEY_PROFILEVIEWCNT+"::*");

        if(!CollectionUtils.isEmpty( keys )) {
            keys.forEach(key -> {
                String flId = key.split("::")[1];
                Integer viewCount = (Integer) redisTemplate.opsForValue().get(key);
                log.debug("updateViewCounts -- {} {}", flId, viewCount);

                // DB에 조회수 업데이트
                freelancerRepository.updateViewCntByFlId(flId, viewCount);

                // Redis에서 키 삭제
                redisTemplate.delete(key);
            });
        } else {
            log.info("FreelancerManager updateViewCounts -- nothing to update");
        }

    }


}
