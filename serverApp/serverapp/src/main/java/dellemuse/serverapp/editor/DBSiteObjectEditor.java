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

	private Boolean hasWritePermission;
	
	public DBSiteObjectEditor(String id, IModel<T> model) {
		super(id, model);
	}
	
		
	@Override
	public boolean hasWritePermission() {
		
		if (hasWritePermission != null)
			return hasWritePermission;
		
		Optional<User> ouser = getSessionUser();
		
		if (ouser.isEmpty()) {

			hasWritePermission = Boolean.FALSE;
			return hasWritePermission;
		}

		User user = ouser.get();

		if (user.isRoot()) {
			hasWritePermission = Boolean.TRUE;
			return hasWritePermission;
		}

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();

			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN)));
				if (isAccess) {
				
					hasWritePermission = Boolean.TRUE;
					return hasWritePermission;

				}
			}
		}

		
		 if (getSiteModel()==null) {
				hasWritePermission = Boolean.FALSE;
				return hasWritePermission;
		 }
		
		 
		{
			final Long sid = getSiteModel().getObject().getId();

			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess) {
					hasWritePermission = Boolean.TRUE;
					return hasWritePermission;

				}
			}
		}

		hasWritePermission = Boolean.FALSE;
		return hasWritePermission;
		 
	}

	public abstract IModel<Site> getSiteModel();
	
	
}
