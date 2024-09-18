package com.pretest.leesangyub.config;

import com.pretest.leesangyub.properties.DBsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@EnableJpaRepositories(
        basePackages = "com.pretest.leesangyub.repository.pri",   //repository 경로
        entityManagerFactoryRef = "priEntityManagerFactory",
        transactionManagerRef = "priTransactionManager"
)
public class PriJpaConfig {

    @Autowired
    private DBsProperties dbsProperties;

    @Primary // db접속이 한개이상일 경우가 있으므로,
    @Bean
    public DataSource priDataSource() {
        log.info( "{} {} {} {} {}", dbsProperties.pridb().driver(), dbsProperties.pridb().ip(), dbsProperties.pridb().port(),
                dbsProperties.pridb().database(),dbsProperties.pridb().user() );
        String url = "jdbc:mariadb://"+dbsProperties.pridb().ip()+":"+dbsProperties.pridb().port()
                +"/"+dbsProperties.pridb().database()+"?rewriteBatchedStatements=true&serverTimezone=asia/seoul&characterEncoding=UTF-8";
        return DataSourceBuilder.create()
                .driverClassName(dbsProperties.pridb().driver())
                .url(url)
                .username(dbsProperties.pridb().user())
                .password(dbsProperties.pridb().pass())
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean priEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(priDataSource());
        em.setPackagesToScan(new String[] { "com.pretest.leesangyub.entity.pri" }); //entity 경로
        em.setPersistenceUnitName("priEntityManager");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        em.setJpaPropertyMap(properties);
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager priTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(priEntityManagerFactory().getObject());
        return transactionManager;
    }
}
