package dellemuse.serverapp.branded.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemPage;
import dellemuse.serverapp.branded.BrandedGuideContentPage;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

public class BrandedArtExhibitionGuidePanel extends DBModelPanel<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedArtExhibitionGuidePanel.class.getName());

	private List<IModel<GuideContent>> guideContentsList;
	private List<IModel<ArtExhibitionItem>> itemsList;
	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private ObjectStateEnumSelector oses;

	private FormState state = FormState.VIEW;

	private ListPanel<GuideContent> itemsPanel;
	private WebMarkupContainer listToolbarContainer;
	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private WebMarkupContainer infoContainer;
	private Boolean isArtExhibitionGuideInfo;

	public BrandedArtExhibitionGuidePanel(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		 
		try {
			addInfo();
			
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("infoContainer", e));
		}
 
		
		try {
			
			addGuideContents();
			
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("items", e));

		}
	}

	protected void addDescription() {
		WebMarkupContainer descContainer = new WebMarkupContainer("descriptionContainer");
		addOrReplace(descContainer);
		ExpandableReadPanel desc = new ExpandableReadPanel("description", getArtExhibitionGuideInfo());
		descContainer.add(desc);
		descContainer.setVisible(isArtExhibitionGuideInfo());
	}

	protected void addInfo() {

		infoContainer = new WebMarkupContainer("infoContainer");
		add(infoContainer);

		WebMarkupContainer audioContainer = new WebMarkupContainer("audioContainer");
		infoContainer.addOrReplace(audioContainer);
		infoContainer.add(new InvisiblePanel("error"));

		try {

			//String intro = getLanguageObjectService().getIntro(getArtExhibitionModel().getObject(), getLocale());
			//IModel<String> m = Model.of(intro != null ? intro : "");
			//infoContainer.add(((new Label("intro", m)).setEscapeModelStrings(false)));

			/** intro audio guide */

			Resource r = getLanguageObjectService().getAudio(getModel().getObject(), getLocale());

			if (r != null) {

				int c = getLanguageObjectService().compareAudioLanguage(getModel().getObject(), getLocale());
				if (c != 0) {
					infoContainer.addOrReplace(new AlertPanel<Void>("error", AlertPanel.INFO, getLabel("audio-other", Language.of(getModel().getObject().getMasterLanguage()).getLabel(getLocale()))));
				}

				WebMarkupContainer audioIntroContainer = new WebMarkupContainer("intro-audio");
				audioContainer.add(audioIntroContainer);
				String as = getPresignedUrl(getModel().getObject().getAudio());
				Url url = Url.parse(as);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				Audio audio = new Audio("audioIntro", resourceReference);
				audioIntroContainer.add(audio);
				
				Label aid = new Label("aid", getModel().getObject().getAudioId()!=null?getModel().getObject().getAudioId().toString():"");
				audioContainer.add(aid);
				
				
			} else {
				audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
			}
			audioContainer.setVisible(r != null);

		} catch (Exception e) {
			logger.error(e);
			infoContainer.addOrReplace(new Label("intro", ""));
			audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
			audioContainer.setVisible(false);
			addOrReplace(new ErrorPanel("descriptionContainer", e));
		}
	}

	private boolean isArtExhibitionGuideInfo() {

		if (isArtExhibitionGuideInfo == null)
			isArtExhibitionGuideInfo = Boolean.valueOf(getArtExhibitionGuideInfo() != null && getArtExhibitionGuideInfo().getObject() != null && getArtExhibitionGuideInfo().getObject().length() > 0);
		return this.isArtExhibitionGuideInfo.booleanValue();
	}

	 

	private IModel<String> getArtExhibitionGuideInfo() {

		if (getModel().getObject().getInfo() != null)
			return new Model<String>(getModel().getObject().getInfo());

		if (getArtExhibitionModel().getObject().getInfo() != null)
			return new Model<String>(getArtExhibitionModel().getObject().getInfo());

		return new Model<String>("");

	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	protected void loadList() {

		this.guideContentsList = new ArrayList<IModel<GuideContent>>();

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getObjects(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getObjects(ObjectState.PUBLISHED).forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getObjects(ObjectState.EDITION).forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == null)
			getObjects().forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getObjects().forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getObjects(ObjectState.DELETED).forEach(s -> this.guideContentsList.add(new ObjectModel<GuideContent>(s)));

		 
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadList();
				event.getTarget().add(BrandedArtExhibitionGuidePanel.this.itemsPanel);
				event.getTarget().add(BrandedArtExhibitionGuidePanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.guideContentsList != null)
			this.guideContentsList.forEach(t -> t.detach());

		if (siteModel != null)
			siteModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();

		if (itemsList != null)
			this.itemsList.forEach(t -> t.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	public IModel<String> getObjectTitle(IModel<GuideContent> model) {

		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(model.getObject(), getLocale()));

		GuideContent o = model.getObject();
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON);

		return Model.of(str.toString());
	}

	public Iterable<GuideContent> getObjects() {
		return this.getObjects(null, null);
	}

	public Iterable<GuideContent> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		ArtExhibitionGuide guide = getModel().getObject();
		if (os1 == null && os2 == null)
			return service.getGuideContents(guide);
		if (os2 == null)
			return service.getArtExhibitionGuideContents(guide, os1);
		if (os1 == null)
			return service.getArtExhibitionGuideContents(guide, os2);
		return service.getArtExhibitionGuideContents(guide, os1, os2);
	}

	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		GuideContent gc = super.findGuideContentWithDeps(model.getObject().getId()).get();
		model.setObject(gc);

		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return BrandedArtExhibitionGuidePanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				
				return BrandedArtExhibitionGuidePanel.this.getGuideContentSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return BrandedArtExhibitionGuidePanel.this.getObjectImageSrc(getModel());
			}

			@Override
			protected String getIcon() {
				return isAudio(getModel()) ? "fa-solid fa-headphones iconOver" : null;
			}

		};
	}

	protected void onObjectSelect(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {

		ArtExhibitionItem item = model.getObject();
		super.addItem(getModel().getObject(), item, getUserDBService().findRoot());
		resetList();
		target.add(this.itemsPanel);
	}

	protected void resetList() {
		this.guideContentsList = null;
	}

	protected IModel<String> getObjectInfo(IModel<GuideContent> model) {
		
		if (!model.getObject().isDependencies())
			model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get());
		
		String s=getLanguageObjectService().getIntro(model.getObject(), getLocale());
		
		if (s!=null && s.length()>0)
			return Model.of(TextCleaner.clean(s, ServerConstant.INTRO_MAX));
		
		return Model.of(TextCleaner.clean(getLanguageObjectService().getInfo(model.getObject(), getLocale()), ServerConstant.INTRO_MAX));
	}

	
	
	protected IModel<String> getGuideContentSubtitle(IModel<GuideContent> model) {
		
		
		GuideContent content = null;
		
		if (!model.getObject().isDependencies()) {
			content=getGuideContentDBService().findWithDeps(model.getObject().getId()).get();
		}
		else
			content=model.getObject();
			
		ArtExhibitionItem item = content.getArtExhibitionItem();

		if (!item.isDependencies()) {
				item=getArtExhibitionItemDBService().findWithDeps(item.getId()).get();
		}
		String s=getArtistStr(item.getArtWork());
		return Model.of(TextCleaner.truncate(s, 280));
	}

	protected String getObjectImageSrc(IModel<GuideContent> model) {
		return super.getImageSrc(model.getObject());
	}

	protected WebMarkupContainer getMenu(IModel<GuideContent> model) {

		return null;
		/**
		 * NavDropDownMenu<GuideContent> menu = new
		 * NavDropDownMenu<GuideContent>("menu", model, null) { private static final
		 * long serialVersionUID = 1L;
		 * 
		 * public boolean isVisible() { return true; } };
		 * 
		 * menu.setOutputMarkupId(true);
		 * 
		 * menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline
		 * d-lg-none d-xl-none d-xxl-none ps-1 pe-1"); menu.setIconCss("fa-solid
		 * fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline
		 * d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");
		 */

		/**
		 * menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<GuideContent> getItem(String id) {
		 * 
		 *           return new AjaxLinkMenuItem<GuideContent>(id) {
		 * 
		 *           private static final long serialVersionUID = 1L;
		 * 
		 * @Override public void onClick(AjaxRequestTarget target) { // refresh(target);
		 *           }
		 * 
		 * @Override public IModel<String> getLabel() { return getLabel("open"); }
		 * 
		 *           }; } });
		 * 
		 *           return menu;
		 * 
		 **/

	}

	private List<IModel<GuideContent>> getItems() {

		if (this.guideContentsList == null) {
			loadList();
		}
		return this.guideContentsList;
	}

	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		//IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));

		
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);
		return listToolbar;
	}

	 
	
	
	
	private void addGuideContents() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<GuideContent> model) {
				return getObjectTitle(model.getObject());
			}
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return BrandedArtExhibitionGuidePanel.this.getObjectListItemExpandedPanel(model, mode);

			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {

				DelleMuseObjectListItemPanel<GuideContent> panel = new DelleMuseObjectListItemPanel<GuideContent>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio() != null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return BrandedArtExhibitionGuidePanel.this.getObjectTitle(getModel().getObject());
					}

					@Override
					protected IModel<String> getObjectSubtitle() {
						return BrandedArtExhibitionGuidePanel.this.getGuideContentSubtitle(getModel() );
					}
					
					@Override
					protected String getImageSrc() {
						return BrandedArtExhibitionGuidePanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new BrandedGuideContentPage(getModel(), BrandedArtExhibitionGuidePanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return BrandedArtExhibitionGuidePanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return BrandedArtExhibitionGuidePanel.this.getMenu(getModel());
					}

					@Override
					protected String getIcon() {
						return isAudio(getModel()) ? "fa-solid fa-headphones iconOver" : null;
					}

				};
				return panel;
			}

			@Override
			public List<IModel<GuideContent>> getItems() {
				return BrandedArtExhibitionGuidePanel.this.getItems();
			}
			
			@Override
			protected boolean isToolbar() {
				return false;
			}
			
		};
		add(itemsPanel);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(false);
	}

	protected void setList(List<IModel<GuideContent>> list) {
		this.guideContentsList = list;
	}

	public FormState getState() {
		return this.state;
	}

	public void setState(FormState state) {
		this.state = state;
	}

	protected boolean isAudio(IModel<GuideContent> model) {
		return model.getObject().getAudio() != null;
	}

	private void setUpModel() {
		ArtExhibition ae = getModel().getObject().getArtExhibition();
		setArtExhibitionModel(new ObjectModel<ArtExhibition>(getArtExhibitionDBService().findById(ae.getId()).get()));
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
	}

}
