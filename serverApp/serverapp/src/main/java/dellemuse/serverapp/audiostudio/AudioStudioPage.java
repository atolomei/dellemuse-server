package dellemuse.serverapp.audiostudio;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Optional;
import java.util.Set;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Site;
import io.odilon.util.Check;
import io.wktui.error.ErrorPanel;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

/**
 * 
 * Texto
 * 
 * Language Voice Speed Model
 * 
 * generate
 * 
 * add music intro
 *
 * ----- Eleven Multilingual v2 Speed Stability Similarity boost Style -----
 */

@MountPath("/studio/${id}")
public class AudioStudioPage extends BasePage {

	private static final long serialVersionUID = 1L;
	 
	static private Logger logger = Logger.getLogger(AudioStudioPage.class.getName());

	private IModel<AudioStudio> model;

	private StringValue stringValue;
	private Exception exceptionError;

	private AudioStudioEditorMainPanel asEditorMainPanel;

	private Panel header;

	private String parentObjectName;
	private Long parentObjectId;
	private String parentObjectPrefix;

	private String mlo_parentObjectName;
	private Long mlo_parentObjectId;
	private String mlo_parentObjectPrefix;

	private boolean isAccesibleVersion =false;

	/**
	 * @param model
	 */
	public AudioStudioPage(IModel<AudioStudio> model, boolean isAccesibleVersion) {
		super();
		setModel(model);
		
		this.isAccesibleVersion=isAccesibleVersion;
		
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject() != null, "modelOjbect is null");
		setModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	public AudioStudioPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {
			
			setUpModel();

			if (getModel() == null) {
				setResponsePage(new ErrorPage(exceptionError));
				return;
			}

			add(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
			add(new GlobalFooterPanel<Void>("footer-panel"));

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("top-panel", e));
			addOrReplace(new ErrorPanel("footer-panel", e));
		}

		addHeaderPanel();
		
		if (!this.hasAccessRight(getSessionUser())) {
			add( new ErrorPanel("editor", getLabel("not-authorized")));
			return;
		}
		
		try {
			asEditorMainPanel = new AudioStudioEditorMainPanel("editor", getModel(), getParentObjectUrl(), this.isAccesibleVersion);
			add(asEditorMainPanel);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("editor", e));
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (getModel() != null)
			getModel().detach();
	}

	public IModel<AudioStudio> getModel() {
		return model;
	}

	public void setModel(IModel<AudioStudio> model) {
		this.model = model;
	}

	protected void addHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/" + mlo_parentObjectPrefix + "/" + mlo_parentObjectId.toString(), Model.of(mlo_parentObjectName + " (" + getLabel("audio-guide").getObject() + ")")));
			bc.addElement(new BCElement(getLabel("audio-studio-bcrumb", getModel().getObject().getDisplayname())));

			JumboPageHeaderPanel<AudioStudio> h = new JumboPageHeaderPanel<AudioStudio>("page-header", getModel(), 
					
					new Model<String>(mlo_parentObjectName)
					
					);
			h.setBreadCrumb(bc);
			
			h.setIcon(AudioStudio.getIcon());
			h.setContext(getLabel("audio-studio"));
			h.setHeaderCss("mb-0 pb-2 border-none");
			this.header = h;

		} catch (Exception e) {
			this.header = new ErrorPanel("page-header", e);
		}
		addOrReplace(this.header);
	}

	protected void setUpModel() {

		if (getModel() == null) {
			if (stringValue != null) {
				Optional<AudioStudio> o_ag = getAudioStudioDBService().findWithDeps(Long.valueOf(stringValue.toLong()));
				if (o_ag.isPresent()) {
					setModel(new ObjectModel<AudioStudio>(o_ag.get()));
				}
			}
		} else {
			if (!getModel().getObject().isDependencies()) {
				Optional<AudioStudio> o_ag = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
				if (o_ag.isPresent()) {
					setModel(new ObjectModel<AudioStudio>(o_ag.get()));
				}
			}
		}

		AudioStudioParentObject ap = getAudioStudioDBService().findParentObjectWithDeps(getModel().getObject()).get();

		parentObjectName = ap.getName();
		parentObjectId = ap.getId();
		parentObjectPrefix = ap.getPrefixUrl();

		if (ap instanceof TranslationRecord) {
			
			mlo_parentObjectName 	= ((TranslationRecord) ap).getParentObject().getName();
			mlo_parentObjectId 		= ((TranslationRecord) ap).getParentObject().getId();
			mlo_parentObjectPrefix 	= ((TranslationRecord) ap).getParentObject().getPrefixUrl();
		
		} else {
			
			mlo_parentObjectName = parentObjectName;
			mlo_parentObjectId = parentObjectId;
			mlo_parentObjectPrefix = parentObjectPrefix;
		}
		
		
	}
	
	protected String getParentObjectUrl() {
		return getServerUrl() + "/" + mlo_parentObjectPrefix + "/" + mlo_parentObjectId.toString();
	}

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

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
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
				if (isAccess)
					return true;
			}
		}

		{
		
			Optional<Site> o = getAudioStudioDBService().getSite( getModel().getObject() );
			if (o.isEmpty())
				return true;
			
			final Long sid = o.get().getId();

			Set<RoleSite> set = user.getRolesSite();
			
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}
}
