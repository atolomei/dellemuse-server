package dellemuse.serverapp.serverdb.object.service;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;

public class BaseObjectService {

    
    private final  DelleMuseObject object;
    
    
    public BaseObjectService(DelleMuseObject object) {
        this.object=object;
    }
    
    public  DelleMuseObject getObject() {
        return object;
    }
}
