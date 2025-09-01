package dellemuse.serverdb.object.service;

import dellemuse.serverdb.model.DelleMuseObject;

public class BaseObjectService {

    
    private final  DelleMuseObject object;
    
    
    public BaseObjectService(DelleMuseObject object) {
        this.object=object;
    }
    
    public  DelleMuseObject getObject() {
        return object;
    }
}
