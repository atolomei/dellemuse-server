package dellemuse.server.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.DelleMuseObject;

public abstract class ModelService<T extends DelleMuseObject, M extends DelleMuseModelObject> extends BaseService {

    final Class<?> modelClass;
    final Class<?> sourceClass;
    
    public ModelService(Settings settings, Class<?> sourceClass, Class<?> modelClass) {
        super(settings);
        this.sourceClass = sourceClass;
        this.modelClass=modelClass;
    }

    @SuppressWarnings("unchecked")
    public  M model(T item) {
        
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