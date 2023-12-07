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
@EnableJpaRepositories(basePackages = {"com.hungama.Repository"}, entityManagerFactoryRef = "mysqlEntityManagerFactory", transactionManagerRef = "mysqlTransactionManager")
public class MysqDbConfig 

{	
	
	private static final Logger log = LogManager.getLogger(VendorStatus.class);

	
  @Value("${spring.datasource.driverClassName}")
  private String DRIVER;
  
  @Value("${spring.datasource.password}")
  private String PASSWORD;
  
  @Value("${spring.datasource.url}")
  private String URL;
  
  @Value("${spring.datasource.username}")
  private String USERNAME;
  
  @Value("${spring.jpa.database-platform}")
  private String DIALECT;
  
  @Value("${spring.jpa.show-sql}")
  private String SHOW_SQL;
  
  @Value("${spring.hibernate.hbm2ddl.auto}")
  private String HBM2DDL_AUTO;
  
  @Value("${spring.entitymanager.packagesToScan}")
  private String PACKAGES_TO_SCAN;
  
  @Bean(name = {"mysqlEntityManagerFactory"})
  public LocalContainerEntityManagerFactoryBean mysqlEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(mysqlDataSource());
    em.setPackagesToScan(new String[] { "com.hungama.UserModel" });
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter((JpaVendorAdapter)vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", this.DIALECT);
    properties.put("hibernate.show_sql", this.SHOW_SQL);
    em.setJpaPropertyMap(properties);
    return em;
  }
  
  @Bean
  public DataSource mysqlDataSource() {
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
  public PlatformTransactionManager mysqlTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(mysqlEntityManager().getObject());
    return (PlatformTransactionManager)transactionManager;
  }
}
