package dellemuse.server.model;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.DelleMuseObject;

public abstract class ModelService<T extends DelleMuseObject, M extends DelleMuseModelObject> extends BaseService {

    public ModelService(Settings settings) {
        super(settings);
    }
    
    public abstract M getModel(T type);
    public abstract T getSource(M model);
    
    

}
