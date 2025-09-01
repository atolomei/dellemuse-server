package dellemuse.serverapp.serverdb.service.base;
/*
 * Odilon Object Storage
 * (c) kbee 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.JsonObject;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;



/**
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public abstract class BaseService extends  JsonObject {
                        
    @JsonIgnore
    static final private ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jdk8Module());
    }

    @JsonIgnore
    @Autowired
    private final ServerDBSettings settings;
    
    @JsonIgnore
    private ServiceStatus status;


    public BaseService(ServerDBSettings settings) {
        this.status = ServiceStatus.STOPPED;        
        this.settings=settings;
    }

    public ServerDBSettings getSettings() {
        return settings;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public ServiceStatus getStatus() {
        return this.status;
    }


}
