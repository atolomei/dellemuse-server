package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.DelleMuseObject;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.db.service.DBService;
import jakarta.transaction.Transactional;


@Service
public abstract class ModelService<T extends DelleMuseObject, M extends DelleMuseModelObject> extends BaseService {

    final Class<?> modelClass;
    final Class<?> sourceClass;
    final DBService<T,Long> dbService;
    
	@JsonIgnore 
	static final private ObjectMapper hb6mapper = new ObjectMapper();
	  
	static  {
		
		hb6mapper.registerModule(new JavaTimeModule());
		hb6mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		hb6mapper.registerModule(new Jdk8Module());
		hb6mapper.registerModule(new Hibernate6Module());
		// hb6mapper.registerModule(new Hibernate6Module().configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false));
		// mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
	}

	
	 public boolean isDetached(T entity) {
		      return !getDBService().getEntityManager().contains(entity);
	 }
		 
	 @Transactional
	 public void reloadIfDetached(T src) {
	    	if ( !getDBService().getEntityManager().contains(src)) {
	 		   src = getDBService().findById(src.getId()).get();
	 	  }
		}

	 
	public DBService<T,Long> getDBService() {
		return dbService;
	}


	@Override
	@JsonIgnore 
	public ObjectMapper getObjectMapper() {
		return hb6mapper;
	}

	
    
    public ModelService(Settings settings, Class<?> sourceClass, Class<?> modelClass, DBService<T,Long> dbService) {
        super(settings);
        this.sourceClass = sourceClass;
        this.modelClass=modelClass;
        this.dbService=dbService;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public  M model(T item) {
        
      	if (isDetached(item)) 
     		   item = getDBService().findById(item.getId()).get();

      	
            String json = null;
            try {
                json = getObjectMapper().writeValueAsString(item);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try {
                return (M) getObjectMapper().readValue(json, modelClass);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        

    
    @SuppressWarnings("unchecked")
    public T source(M model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (T) getObjectMapper().readValue(json, sourceClass);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
/**

{
modelclass: class
id: id
name: name
....}



**/