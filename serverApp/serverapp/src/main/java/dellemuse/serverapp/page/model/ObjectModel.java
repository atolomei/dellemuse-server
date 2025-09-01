package dellemuse.serverapp.page.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import org.apache.wicket.model.IModel;
 
import org.springframework.util.Assert;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

public class ObjectModel<T extends DelleMuseObject> implements IModel<T> {

	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Class<?> clazz;
	private T object;
	private boolean detached = false;
	
		
	public ObjectModel(T object) {
		this.id=object.getId();
		 setObject(object);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T getObject()  {

		 
		if (detached) {
			if (id==null)
				throw new RuntimeException("id: " +id +(clazz!=null ? (" | class:"+clazz.getName()) : " | no class"));
			
			DBService<?,Long> service = DBService.getDBService(clazz);
			Optional<?> o = service.findById(getId());
			
			if (o.isPresent()) {
				detached = false;
				object = (T) o.get();
			}

			if (object==null)
				throw new RuntimeException("id: " +id +(clazz!=null ? (" | class:"+clazz.getName()) : " | no class"));
			
		}	
		return object;
	}

	
	public void setObject(T object) {
		this.object = object;
		if (object!=null)
			id = ((DelleMuseObject) object).getId();
	
	}
	
	@Override
	public void detach() {
		try {

			if (detached) 
				return;
			
			if (clazz==null) {
				String classname = object.getClass().getName();
				int i = classname.indexOf("_");
				if (i>0) classname = classname.substring(0, i);
				i = classname.indexOf("$");
				if (i>0) classname = classname.substring(0, i);
				clazz = Class.forName(classname);
			}
		
			id= ((DelleMuseObject)object).getId();
			
			detached = true;
			object = null;
		}
		catch (java.lang.NullPointerException e1 ) {
			detached = true;
		}
		catch (ClassNotFoundException  e2 ) {
			throw new RuntimeException(e2);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectModel<?>) {
			return ((ObjectModel<?>)obj).id.equals(id);
		}
		return false;
	}
	
	public Long getId() {
		return id;
	}
	
	@Override
	public int hashCode()    {
		return id.hashCode();
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		if (!detached) {
			detach();
		}	
		if (!detached) {
			Assert.isTrue(detached, "!detached");
		}
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}
	
}
