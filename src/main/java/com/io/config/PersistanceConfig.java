package com.io.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
@EnableJpaRepositories(basePackages = "com.io.repository")
@EnableTransactionManagement
@PropertySource("classpath:persistance.properties")
public class PersistanceConfig {

	@Autowired
	private Environment env;

	@Bean(name = "dataSource")
	public DataSource dataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	    dataSource.setUrl(env.getProperty("jdbc.url"));
	    dataSource.setUsername(env.getProperty("jdbc.user"));
	    dataSource.setPassword(env.getProperty("jdbc.pass"));
	    return dataSource;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
		vendorAdapter.setGenerateDdl(true);

		Properties jpaProterties = new Properties();
        jpaProterties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaProterties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        jpaProterties.put("hibernate.connection.CharSet", env.getProperty("hibernate.connection.CharSet"));
        jpaProterties.put("hibernate.connection.characterEncoding", env.getProperty("hibernate.connection.characterEncoding"));
        jpaProterties.put("hibernate.connection.useUnicode", env.getProperty("hibernate.connection.useUnicode"));
        jpaProterties.put("hibernate.generate_statistics", env.getProperty("hibernate.generateStatistics"));
        jpaProterties.put("hibernate.show_sql", env.getProperty("hibernate.showSql"));
        jpaProterties.put("hibernate.format_sql", env.getProperty("hibernate.formatSql"));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.io.domain");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();
		factory.setJpaProperties(jpaProterties);
	    return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}
	
}