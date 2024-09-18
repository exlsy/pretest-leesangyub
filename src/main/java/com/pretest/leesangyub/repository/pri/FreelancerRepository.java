package com.pretest.leesangyub.repository.pri;

import com.pretest.leesangyub.entity.pri.Freelancer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FreelancerRepository extends JpaRepository<Freelancer, Long>, FreelancerDao {

    Optional<Freelancer> findFreelancerByFlId(String flId);

    @Transactional
    void deleteFreelancerByFlId(String flId);

}
