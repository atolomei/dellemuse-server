package dellemuse.server.db;


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dellemuse.server.DelleMuseApplication;
import dellemuse.server.Settings;



// import jakarta.persistence.EntityManagerFactory;

/**
 * 
 * 
 */
@Configuration
@EnableTransactionManagement
public class HibernateConf {

	
	//@Autowired
	//private final Settings settigsService;
	
	
//	public HibernateConf(Settings settigsService) {
//		this.settigsService= settigsService;
//	}
	
    
  public HibernateConf() {
  }
	
	
	@Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(DelleMuseApplication.hibernateConfPackages);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DelleMuseApplication.driverClassName);
        dataSource.setUrl(DelleMuseApplication.url);
        dataSource.setUsername(DelleMuseApplication.userName);
        dataSource.setPassword(DelleMuseApplication.password);
        return dataSource;
    }
    
    @Bean(name = "transactionManager")
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
          = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.ddl-auto", "none");
        //hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return hibernateProperties;
    }

}
