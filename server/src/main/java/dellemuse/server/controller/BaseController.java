package dellemuse.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.JsonObject;
import jakarta.annotation.PostConstruct;

public class BaseController extends JsonObject implements ApplicationContextAware {
    
    @JsonIgnore
    @Autowired
    private ApplicationContext applicationContext;
    
    
    @Autowired
    public BaseController() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    @PostConstruct
    protected void init() {
    }
    

    
}
