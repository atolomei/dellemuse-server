package dellemuse.serverapp.serverdb;


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

 
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class HibernateConf {

	
	    static final public String PACKAGES_TO_SCAN = "dellemuse.serverapp.serverdb.model";
	   
        @Value("${spring.datasource.url}")
        private String dbUrl;

        @Value("${spring.datasource.username}")
        private String dbUsername;

        @Value("${spring.datasource.password}")
        private String dbPassword;

        @Value("${spring.datasource.driver-class-name}")
        private String dbDriver;

        @Bean
        public DataSource dataSource() {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(dbDriver);
            ds.setUrl(dbUrl);
            ds.setUsername(dbUsername);
            ds.setPassword(dbPassword);
            return ds;
        }
        
     
        @Bean(name = "entityManagerFactory")
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
            emf.setDataSource(dataSource);
            emf.setPackagesToScan(PACKAGES_TO_SCAN);  
            emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

            Properties jpaProps = new Properties();
            jpaProps.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            jpaProps.setProperty("hibernate.hbm2ddl.auto", "none");

            emf.setJpaProperties(jpaProps);
            return emf;
        }

        @Bean
        public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }
}



