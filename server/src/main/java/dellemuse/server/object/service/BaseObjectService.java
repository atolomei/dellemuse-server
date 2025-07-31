package dellemuse.server.object.service;

import dellemuse.server.db.model.DelleMuseObject;

public class BaseObjectService {

    
    private final  DelleMuseObject object;
    
    
    public BaseObjectService(DelleMuseObject object) {
        this.object=object;
    }
    
    public  DelleMuseObject getObject() {
        return object;
    }
}
