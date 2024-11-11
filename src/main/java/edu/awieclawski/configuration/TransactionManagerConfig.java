package edu.awieclawski.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement // (mode = AdviceMode.PROXY)
@RequiredArgsConstructor
public class TransactionManagerConfig {

    final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("edu.awieclawski");

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(additionalProperties());
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new NameTransactionManager(); // smart override ;)
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(Objects.requireNonNull(env.getProperty("spring.datasource.url")));
        dataSource.setUsername(Objects.requireNonNull(env.getProperty("spring.datasource.username")));
        dataSource.setPassword(Objects.requireNonNull(env.getProperty("spring.datasource.password")));
        return dataSource;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("spring.jpa.generate-ddl", env.getProperty("spring.jpa.generate-ddl"));
        hibernateProperties.setProperty("spring.jpa.open-in-view", env.getProperty("spring.jpa.open-in-view"));
        hibernateProperties.setProperty("spring.sql.init.mode", env.getProperty("spring.sql.init.mode"));
        hibernateProperties.setProperty("spring.sql.init.platform", env.getProperty("spring.sql.init.platform"));
        return hibernateProperties;
    }

}
