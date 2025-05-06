package com.blpsteam.blpslab1.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableJpaRepositories(
        basePackages = "com.blpsteam.blpslab1.repositories.core",
        entityManagerFactoryRef = "coreEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Bean
    public DataSource primaryDataSource() throws NamingException {
        return (DataSource) new JndiTemplate().lookup("java:/DB1DataSource");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory() throws NamingException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan("com.blpsteam.blpslab1.data.entities.core");
        em.setPersistenceUnitName("corePU");
        em.setJtaDataSource(primaryDataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.transaction.jta.platform",
                "org.hibernate.engine.transaction.jta.platform.internal.JBossAppServerJtaPlatform");
        jpaProperties.put("javax.persistence.transactionType", "JTA");
        jpaProperties.put("hibernate.transaction.coordinator_class", "jta");

        em.setJpaProperties(jpaProperties);

        return em;
    }
}