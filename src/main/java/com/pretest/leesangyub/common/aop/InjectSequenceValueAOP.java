package com.pretest.leesangyub.common.aop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Slf4j
@Aspect
@Configuration
public class InjectSequenceValueAOP {

    @PersistenceContext
    private EntityManager em;

    @Before("execution(* org.springframework.data.repository.CrudRepository.save(*))") //This is for JPA.
    public void generateSequence(JoinPoint joinPoint) {
        Object [] args = joinPoint.getArgs();

        Arrays.stream(args).forEach(obj -> {

            Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> {

                Arrays.stream(field.getAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType().equals( InjectSequenceValue.class)) {
                        // log.info("******************** field annotation {}", annotation);
                        // field.setAccessible(  true);
                        // field.setAccessible( (obj instanceof Entity) ? true : false);
                        try {
                            String sequenceName=field.getAnnotation(InjectSequenceValue.class).sequencename();
                            String tableName=field.getAnnotation(InjectSequenceValue.class).tablename();
                            long nextval=getNextValue(sequenceName, tableName);
                            // log.info("*************** Next value : {}", nextval);
                            field.set(obj, nextval);

                        } catch (IllegalAccessException e) {
                            log.error("error ***** generateSequence IllegalAccessException occured!!");
                        }
                    }
                });
            });
        });
    }

    /**
     * This method fetches the next value from sequence
     */
    public long getNextValue(String sequence, String table) {
        long sequenceNextVal=0L;

        String nativeQuery = "SELECT coalesce(max("+sequence+" ),0)+1 FROM "+table;

        Number num = (Number)em.createNativeQuery("SELECT coalesce(max("+sequence+" ),0)+1 FROM "+table).getSingleResult();
        sequenceNextVal = num.longValue();

        log.info("getNextValue {} --> {}", nativeQuery, sequenceNextVal);

        return  sequenceNextVal;
    }

}
