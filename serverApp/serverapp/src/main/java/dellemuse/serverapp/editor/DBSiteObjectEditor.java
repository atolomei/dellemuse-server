package dellemuse.serverapp.editor;

import java.util.Optional;
import java.util.Set;

import org.apache.wicket.model.IModel;

 
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;

public abstract class DBSiteObjectEditor<T> extends DBObjectEditor<T> {

	private static final long serialVersionUID = 1L;

	public DBSiteObjectEditor(String id, IModel<T> model) {
		super(id, model);
	}
	
	
	@Override
	public boolean hasWritePermission() {
		
		Optional<User> ouser = getSessionUser();
		
		if (ouser.isEmpty())
			return false;

		User user = ouser.get();

		if (user.isRoot())
			return true;

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();

			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN)));
				if (isAccess)
					return true;
			}
		}

		
		 if (getSiteModel()==null)
			 return false;
		
		{
			final Long sid = getSiteModel().getObject().getId();

			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		return false;
		 
	}

	public abstract IModel<Site> getSiteModel();
	
	
}
