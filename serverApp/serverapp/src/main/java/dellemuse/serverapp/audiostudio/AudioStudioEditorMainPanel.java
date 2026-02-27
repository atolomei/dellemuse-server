package dellemuse.serverapp.audiostudio;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.guidecontent.GuideContentEditor;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import io.wktui.error.AlertHelpPanel;
import io.wktui.error.AlertPanel;
import io.wktui.event.HelpAjaxEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import wktui.base.InvisiblePanel;

/**
 *
 * 
 */
public class AudioStudioEditorMainPanel extends DBModelPanel<AudioStudio> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(AudioStudioEditorMainPanel.class.getName());

	private AudioStudioState state;
	private String parentObjectUrl;

	
	private String parentName;
	private String parentInfo;
	private IModel<String> parentType;
	private Long parentId;
	private ObjectState parentState;
	
	
	private InfoAudioStudioEditor info;
	
	private Panel pillEditor1;
	private Panel pillEditor2;
	private Panel pillEditor3;
	
	private PillPanel pill1;
	private PillPanel pill2;
	private PillPanel pill3;

	private WebMarkupContainer helpContainer;

	boolean isHelpVisible = false;
	private boolean isAccesibleVersion = false;	
	
	/**
	 * @param id
	 * @param model
	 */
	public AudioStudioEditorMainPanel(String id, IModel<AudioStudio> model, String parentObjectUrl, boolean isAccesibleVersion) {
		super(id, model);
		this.isAccesibleVersion=isAccesibleVersion;
		this.parentObjectUrl = parentObjectUrl;
		setOutputMarkupId(true);
	}

	
  

	
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addParentObject();

		addAlert();
		addInfo();
		
		addOrReplace(getPillPanel1());
		addOrReplace(getPillPanel2());
		addOrReplace(getPillPanel3());

		addOrReplace(getPill1Editor());
		addOrReplace(new InvisiblePanel("step2"));
		addOrReplace(new InvisiblePanel("step3"));

		cssSelected();
		
		helpContainer = new WebMarkupContainer("helpContainer");
		helpContainer.setOutputMarkupId(true);
		add(helpContainer);
		helpContainer.add(new InvisiblePanel("help"));
		
		
		AjaxLink<Void> h=new AjaxLink<Void> ( "help") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				fire(new HelpAjaxEvent(ServerAppConstant.help, target));
			}
		};
		add(h);
	

		
		
	}
	
	
	
	protected void addAlert() {
		SimpleAlertRow<Void> alert = new SimpleAlertRow<Void>("simpleAlertRow") {
			private static final long serialVersionUID = 1L;
			
			public boolean isVisible() {
				return AudioStudioEditorMainPanel.this.parentState==ObjectState.DELETED;
			}
			
			protected IModel<String> getText() {
				return  AudioStudioEditorMainPanel.this.getLabel("object-deleted");
			}
			
			public int getAlertType() {
				return AlertPanel.WARNING;
			}
		};
		add(alert);
	}


	public String getHelpKey() {
		return Help.AUDIO_STUDIO;
	}
	
	protected Panel getHelpPanel(String id, String key, String lang) {
		String h = getHelpService().gethelp(key, lang);
		if (h == null) {
			h = key + "-" + lang + " not found";
		}
		AlertPanel<Void> a = new AlertHelpPanel<>(id, Model.of(h));
		a.add(new org.apache.wicket.AttributeModifier("class", "help"));
		return a;
	}
	
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<HelpAjaxEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(HelpAjaxEvent event) {
				if (isHelpVisible) {
					isHelpVisible = false;
					helpContainer.get("help").setVisible(false);
				} else {
					helpContainer.addOrReplace(getHelpPanel("help", getHelpKey(), getLocale().getLanguage()));
					isHelpVisible = true;
				}
				event.getTarget().add(helpContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof HelpAjaxEvent)
					return true;
				return false;
			}
		});
	 	
		
		
		
		add(new io.wktui.event.WicketEventListener<AudioStudioAjaxEvent>() {
		
			private static final long serialVersionUID = 1L;
		
			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof AudioStudioAjaxEvent)
					return true;
				return false;
			}

			@Override
			public void onEvent(AudioStudioAjaxEvent event) {

				if (event.getName().equals("step1.next")) {

					getPill1Editor().setVisible(false);
					getPill2Editor().setVisible(true);

					setState(AudioStudioState.AUDIO_VOICE_MUSIC);
					cssSelected();
					
					event.getTarget().add(AudioStudioEditorMainPanel.this);
					logger.debug("step1.next");
				}
				

				if (event.getName().equals("step2.prev")) {

					getPill1Editor().setVisible(true);
					getPill2Editor().setVisible(false);

					setState(AudioStudioState.AUDIO_VOICE);
					cssSelected();
					
					event.getTarget().add(AudioStudioEditorMainPanel.this);
					logger.debug("step2.prev");
				}

				
				

				if (event.getName().equals("step2.next")) {

					getPill1Editor().setVisible(false);
					getPill2Editor().setVisible(false);
					getPill3Editor().setVisible(true);

					setState(AudioStudioState.INTEGRATE);
					cssSelected();
					
					event.getTarget().add(AudioStudioEditorMainPanel.this);
					logger.debug("step2.next");
				}
				

				if (event.getName().equals("step3.prev")) {

					getPill1Editor().setVisible(false);
					getPill2Editor().setVisible(true);
					getPill3Editor().setVisible(false);

					setState(AudioStudioState.AUDIO_VOICE_MUSIC);
					cssSelected();
					
					event.getTarget().add(AudioStudioEditorMainPanel.this);
					logger.debug("step3.prev");
				}

				
			}
		});

		
		add(new io.wktui.event.WicketEventListener<AudioStudioSimpleEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof AudioStudioSimpleEvent)
					return true;
				return false;
			}

			@Override
			public void onEvent(AudioStudioSimpleEvent event) {
			}
		});
	}

	
	protected void addInfo() {
		this.info = new InfoAudioStudioEditor("info", getModel(), this.isAccesibleVersion);
		add(this.info);
	}
	
	protected void cssSelected() {

		if (this.getState() == AudioStudioState.AUDIO_VOICE) {
			pill1.setCss("pill pill-selected bg-dark text-white");
			pill2.setCss("pill");
			pill3.setCss("pill");
		} else if (this.getState() == AudioStudioState.AUDIO_VOICE_MUSIC) {
			pill2.setCss("pill pill-selected bg-dark text-white");
			pill1.setCss("pill");
			pill3.setCss("pill");
		} else {
			pill3.setCss("pill pill-selected bg-dark text-white");
			pill1.setCss("pill");
			pill2.setCss("pill");
		}
	}

	protected Panel getPill1Editor() {
		if (this.pillEditor1 == null) {
			this.pillEditor1 = new Step1AudioStudioEditor("step1", getModel(), isAccesibleVersion);
			addOrReplace(this.pillEditor1);
		}
		return this.pillEditor1;
	}

	protected Panel getPill2Editor() {
		if (this.pillEditor2 == null) {
			this.pillEditor2 = new Step2AudioStudioEditor("step2", getModel(), isAccesibleVersion);
			addOrReplace(this.pillEditor2);
		}
		return this.pillEditor2;
	}

	protected Panel getPill3Editor() {
		if (this.pillEditor3 == null) {
			this.pillEditor3 = new Step3AudioStudioEditor("step3", getModel(), isAccesibleVersion );
			addOrReplace(this.pillEditor3);
		}
		return this.pillEditor3;
	}

	protected PillPanel getPillPanel1() {
		if (pill1 == null)
			pill1 = new PillPanel("pill1", getLabel("pill1.title"), getLabel("pill1.text"));

		return this.pill1;
	}

	protected PillPanel getPillPanel2() {
		if (pill2 == null)
			pill2 = new PillPanel("pill2", getLabel("pill2.title"), getLabel("pill2.text"));
		return this.pill2;
	}

	protected PillPanel getPillPanel3() {
		if (pill3 == null)
			pill3 = new PillPanel("pill3", getLabel("pill3.title"), getLabel("pill3.text"));
		return this.pill3;
	}

	protected void setUpModel() {

		Optional<AudioStudio> o_a = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<>(o_a.get()));

		AudioStudioParentObject po = getAudioStudioDBService().findParentObjectWithDeps(getModelObject()).get();

		this.parentName = po.getName();
		this.parentType = getLabel(po.getClass().getSimpleName().toLowerCase());
		this.parentId = po.getId();
		this.parentInfo = po.getInfo();
		this.parentState=po.getState();
		
		
		// getModel().getObject().setName(parentName);
		// getModel().getObject().setInfo(parentInfo);
		
		setState(AudioStudioState.AUDIO_VOICE);
	}

	
	public AudioStudio getModelObject() {
		return getModel().getObject();
	}

	public AudioStudioState getState() {
		return state;
	}

	public String getParentObjectUrl() {
		return parentObjectUrl;
	}

	public String getParentName() {
		return parentName;
	}

	public String getParentInfo() {
		return parentInfo;
	}

	public IModel<String> getParentType() {
		return parentType;
	}

	public Long getParentId() {
		return parentId;
	}

	public InfoAudioStudioEditor getInfo() {
		return info;
	}

	public void setState(AudioStudioState state) {
		this.state = state;
	}

	public void setParentObjectUrl(String parentObjectUrl) {
		this.parentObjectUrl = parentObjectUrl;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setParentInfo(String parentInfo) {
		this.parentInfo = parentInfo;
	}

	public void setParentType(IModel<String> parentType) {
		this.parentType = parentType;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setInfo(InfoAudioStudioEditor info) {
		this.info = info;
	}

	private void addParentObject() {

		Link<Void> li = new Link<Void>("parent-link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RedirectPage(getParentObjectUrl()));
			}

			public boolean isVisible() {
				return getParentObjectUrl() != null;
			}
		};

		Label parentObject = new Label("parent", TextCleaner.truncate(getParentName(), 18) + " (" + getParentType().getObject() + ")");
		li.add(parentObject);
		add(li);

	}
}
