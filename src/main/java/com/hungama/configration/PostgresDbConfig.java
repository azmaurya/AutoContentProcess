package com.hungama.configration;
import java.util.HashMap;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.hungama.service.VendorStatus;

@Configuration
@EnableJpaRepositories(basePackages = {"com.hungama.Repo"}, entityManagerFactoryRef = "pgEntityManagerFactory", transactionManagerRef = "pgTransactionManager")
public class PostgresDbConfig 
{private static final Logger log = LogManager.getLogger(PostgresDbConfig.class);

  @Value("${postgres.db.driver}")
  private String DRIVER;
  
  @Value("${postgres.db.password}")
  private String PASSWORD;
  
  @Value("${postgres.db.url}")
  private String URL;
  
  @Value("${postgres.db.username}")
  private String USERNAME;
  
  @Value("${postgres.hibernate.dialect}")
  private String DIALECT;
  
  @Value("${postgres.hibernate.show_sql}")
  private String SHOW_SQL;
  
  @Value("${postgres.hibernate.hbm2ddl.auto}")
  private String HBM2DDL_AUTO;
  
  @Value("${postgres.entitymanager.packagesToScan}")
  private String PACKAGES_TO_SCAN;
  
  @Bean(name = {"pgEntityManagerFactory"})
  public LocalContainerEntityManagerFactoryBean pgEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(pgDataSource());
    em.setPackagesToScan(new String[] { "com.hungama.model" });
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter((JpaVendorAdapter)vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", this.DIALECT);
    properties.put("hibernate.show_sql", this.SHOW_SQL);
    em.setJpaPropertyMap(properties);
    return em;
  }
  
  @Bean
  public DataSource pgDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(this.DRIVER);
    dataSource.setUrl(this.URL);
    dataSource.setUsername(this.USERNAME);
    dataSource.setPassword(this.PASSWORD);
    log.info("URL "+URL);
    log.info("USERNAME "+USERNAME);
    log.info("PASSWORD "+PASSWORD);
    return (DataSource)dataSource;
  }
  
  @Bean
  public PlatformTransactionManager pgTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(pgEntityManager().getObject());
    return (PlatformTransactionManager)transactionManager;
  }
}
