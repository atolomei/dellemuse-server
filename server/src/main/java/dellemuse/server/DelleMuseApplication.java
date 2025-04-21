package dellemuse.server;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dellemuse.model.Constant;
import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableJpaRepositories({"dellemuse.server.db.repository"})
@ComponentScan({"dellemuse.server"})
@EntityScan({"dellemuse.server.db.model"})
public class DelleMuseApplication {

    static private Logger std_logger = Logger.getLogger("StartupLogger");

    static public String[] cmdArgs = null;
    
    static public String hibernateConfPackages ="dellemuse.server.db";
    static public String driverClassName = "org.postgresql.Driver";
    static public String url = "jdbc:postgresql://localhost:5432/dellemuse";
    static public String userName = "postgres";
    static public String password = "novamens";

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DelleMuseApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        cmdArgs = args;
        application.run(args);
    }
    
    @PostConstruct
    public void onInitialize() {
        
        std_logger.info("");
        for (String s : DelleMuseVersion.getAppCharacterName())
            std_logger.info(s);
        
        std_logger.info(Constant.SEPARATOR);
        
        std_logger.info("");
        std_logger.info("This software is licensed under the Apache License, Version 2.0");
        std_logger.info("http://www.apache.org/licenses/LICENSE-2.0");

        initShutdownMessage();
    }
    
   private void initShutdownMessage() {
       Runtime.getRuntime().addShutdownHook(new Thread() {
           public void run() {
               std_logger.info("");
               std_logger.info("'Dulce et decorum est pro patria mori'");
               std_logger.info("roman legionaries said when falling in battle");
               std_logger.info("Shuting down... goodbye");
               
               std_logger.info("");
           }
       });
   }
    
    
}
