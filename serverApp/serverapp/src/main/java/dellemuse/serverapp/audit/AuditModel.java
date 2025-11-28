package dellemuse.serverapp.audit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import org.apache.wicket.model.IModel;
 
import org.springframework.util.Assert;

import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
 

public class AuditModel implements IModel<DelleMuseAudit> {

	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Class<?> clazza;
	private DelleMuseAudit object;
	private boolean detached = false;
	
		
	public AuditModel(DelleMuseAudit object) {
		this.id=object.getId();
		
		if (clazza==null) {
			String classname = object.getClass().getName();
			int i = classname.indexOf("_");
			if (i>0) classname = classname.substring(0, i);
			i = classname.indexOf("$");
			if (i>0) classname = classname.substring(0, i);
		try {
			clazza = Class.forName(classname);
			} catch (ClassNotFoundException e) {
			 throw new RuntimeException(e);
			}
		}
		setObject(object);
	}
	
	 
	@Override
	public DelleMuseAudit getObject()  {

		if (this.detached) {
			
			if (this.id==null)
				throw new RuntimeException("id: " +id +(getObjectClass()!=null ? (" | class:"+getObjectClass().getName()) : " | no class"));
			
			  
			
			Optional<?> o =	load();
						
			if (o.isPresent()) {
				this.detached = false;
				this.object = (DelleMuseAudit) o.get();
			}

			if (this.object==null)
				throw new RuntimeException("id: " +id +(getObjectClass()!=null ? (" | class:"+getObjectClass().getName()) : " | no class"));
			
		}	
		return this.object;
	}

	protected Optional<?> load() {
		Optional<?> o =	getDelleMuseAuditDBService().findById(getId());
		return o;
	}
	
	protected Class<?> getObjectClass() {
		return this.clazza;
	}
	
	
	protected DelleMuseAuditDBService getDelleMuseAuditDBService() {
		return (DelleMuseAuditDBService) ServiceLocator.getInstance().getBean(DelleMuseAuditDBService.class);
	}
	
	public void setObject(DelleMuseAudit object) {
		this.object = object;
		if (object!=null)
			id = ((DelleMuseAudit) object).getId();
	}
	
	@Override
	public void detach() {
		try {

			if (detached) 
				return;
			
		 
			if (clazza==null) {
				String classname = object.getClass().getName();
				int i = classname.indexOf("_");
				if (i>0) classname = classname.substring(0, i);
				i = classname.indexOf("$");
				if (i>0) classname = classname.substring(0, i);
				clazza = Class.forName(classname);
			}
			 
			id= ((DelleMuseAudit)object).getId();
			
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
		if (obj instanceof AuditModel) {
			return ((AuditModel)obj).id.equals(id);
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
