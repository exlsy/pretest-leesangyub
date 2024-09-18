package com.pretest.leesangyub.repository.pri;

import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.enums.RecruitField;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Slf4j
//@SpringBootTest
// @Transactional
// @Rollback
@ExtendWith(MockitoExtension.class)
public class FreelancerRepositoryMockUnitTest {

    @Mock
    private FreelancerRepository freelancerRepo;

    private Freelancer freelancer;

    @BeforeEach
    public void setUp() {
        freelancer = Freelancer.builder()
                .seq(1)
                .flId("fl_00000001")
                .flName("홍길동")
                .exprYears(5)
                .recruitField(RecruitField.BACKEND)
                .viewCnt(0)
                .accumPoint(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // CREATE
    @Test
    public void testCreateFreelancer() {
        // Given
        when(freelancerRepo.save(any(Freelancer.class))).thenReturn(freelancer);

        // When
        Freelancer createdFreelancer = freelancerRepo.save(freelancer);

        // Then
        assertNotNull(createdFreelancer);
        assertEquals("fl_00000001", createdFreelancer.getFlId());
        assertEquals("홍길동", createdFreelancer.getFlName());
        assertEquals(RecruitField.BACKEND, createdFreelancer.getRecruitField());
        assertEquals(0, createdFreelancer.getViewCnt());

        verify(freelancerRepo, times(1)).save(any(Freelancer.class));

    }

    // READ Test
    @Test
    public void testFindFreelancerById() {
        // Given
        when(freelancerRepo.findFreelancerByFlId("fl_00000001")).thenReturn(Optional.of(freelancer));

        // When
        Optional<Freelancer> foundFreelancer = freelancerRepo.findFreelancerByFlId("fl_00000001");

        // Then
        assertTrue(foundFreelancer.isPresent());
        assertEquals("fl_00000001", foundFreelancer.get().getFlId());
        assertEquals("홍길동", foundFreelancer.get().getFlName());
        verify(freelancerRepo, times(1)).findFreelancerByFlId("fl_00000001");
    }


}
