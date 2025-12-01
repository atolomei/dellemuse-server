package dellemuse.serverapp.audiostudio;

import java.util.Optional;

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
import dellemuse.serverapp.serverdb.model.AudioStudio;
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

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(AudioStudioPage.class.getName());

	private IModel<AudioStudio> model;
	
	private StringValue stringValue;
	private Exception exceptionError;
	
	//private AudioStudioEditor editor;

	private AudioStudioEditorMainPanel asEditorMainPanel;

	
	private Panel header;
	
	private String parentObjectName;
	private Long parentObjectId;
	private String parentObjectPrefix;
	
	private String mlo_parentObjectName;
	private Long mlo_parentObjectId;
	private String mlo_parentObjectPrefix;

	/**
	 * @param model
	 */
	public AudioStudioPage(IModel<AudioStudio> model) {
		super();
		setModel(model);
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
			addOrReplace( new ErrorPanel("top-panel", e));
			addOrReplace( new ErrorPanel("footer-panel", e));
		}
		
		addHeaderPanel();
			
		try { 
			//editor = new AudioStudioEditor("editor", getModel(), getServerUrl() + "/" + mlo_parentObjectPrefix + "/" + mlo_parentObjectId.toString() );
			//add(editor);
			asEditorMainPanel = new AudioStudioEditorMainPanel("editor", getModel(), getParentObjectUrl());
			add(asEditorMainPanel);

			
		} catch (Exception e) {
			addOrReplace( new ErrorPanel("editor", e));
		}
	}

	protected String getParentObjectUrl() {
		return getServerUrl() + "/" + mlo_parentObjectPrefix + "/" + mlo_parentObjectId.toString() ;
		
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
			bc.addElement(new HREFBCElement("/" +mlo_parentObjectPrefix + "/" + mlo_parentObjectId.toString(), Model.of(mlo_parentObjectName + " (" + getLabel("audio-guide").getObject()+ ")")));
			bc.addElement(new BCElement(getLabel("audio-studio-bcrumb", getModel().getObject().getDisplayname())));
			
			JumboPageHeaderPanel<AudioStudio> h = new JumboPageHeaderPanel<AudioStudio>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));
			h.setBreadCrumb(bc);
			h.setContext(getLabel("audio-studio"));
			this.header=h;
				
		} catch (Exception e) {
			this.header=new ErrorPanel("page-header", e);
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
			parentObjectId =ap.getId();
			parentObjectPrefix=ap.getPrefixUrl();
			
			if (ap instanceof TranslationRecord) {
				
				mlo_parentObjectName = 	ap.getName();
				mlo_parentObjectId   = ap.getId();
				mlo_parentObjectPrefix = ap.getPrefixUrl();
			}
			else {
				mlo_parentObjectName =   parentObjectName;
				mlo_parentObjectId   = 	 parentObjectId;
				mlo_parentObjectPrefix = parentObjectPrefix;
			}
			
		 
	}

}
