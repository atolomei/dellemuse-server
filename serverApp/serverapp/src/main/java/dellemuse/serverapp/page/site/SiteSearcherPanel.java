package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
 
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.LinkSubmitButton;
import io.wktui.form.field.Field;
 
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;

public class SiteSearcherPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteSearcherPanel.class.getName());

	private List<ToolbarItem> listToolbar;
	private ObjectStateEnumSelector oses=ObjectStateEnumSelector.ALL;
	
	private Form<Void> form;
	private String audioId = "";
	private TextField<String> aidField;
	
 	private WebMarkupContainer itemsGuideContentsContainer;
	private WebMarkupContainer itemsArtExhibitionGuideContainer;
	
	private WebMarkupContainer listToolbarContainer;

	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private List<IModel<GuideContent>> gc_list;
	private List<IModel<ArtExhibitionGuide>> ag_list;
	
	public SiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}
	
	public SiteSearcherPanel(String id, IModel<Site> model,  List<IModel<GuideContent>> gc_list,  List<IModel<ArtExhibitionGuide>> ag_list) {
		super(id, model);
		setOutputMarkupId(true);
		this.gc_list=gc_list;
		this.ag_list=ag_list;
	}
	
	public String getAudioId() {
		return this.audioId;
	}

	public void setAudioId(String aid) {
		this.audioId = aid;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addListToolbar();
		addForm();
	
		itemsGuideContentsContainer = new WebMarkupContainer("itemsGuideContentContainer");
		itemsGuideContentsContainer.setOutputMarkupId(true);
		add(itemsGuideContentsContainer);
		
		if (gc_list==null || gc_list.size()==0) {
			itemsGuideContentsContainer.setVisible(false);
			itemsGuideContentsContainer.add(new InvisiblePanel("results"));
		}
		else {
			itemsGuideContentsContainer.setVisible(true);
				SearchResultsPanel<GuideContent> panel = new SearchResultsPanel<GuideContent>("results", getModel(), gc_list) {
				@Override
				protected void onClick(IModel<GuideContent> model) {
					setResponsePage( new GuideContentPage(model));
				}
			};
			itemsGuideContentsContainer.add( panel );
		}
 
		
		
		itemsArtExhibitionGuideContainer = new WebMarkupContainer("itemsArtExhibitionGuideContainer");
		add(itemsArtExhibitionGuideContainer);
		itemsArtExhibitionGuideContainer.setOutputMarkupId(true);
		
		
		if (ag_list==null || ag_list.size()==0) {
			itemsArtExhibitionGuideContainer.setVisible(false);
			itemsArtExhibitionGuideContainer.add(new InvisiblePanel("results"));
		}
		else {
			
			itemsArtExhibitionGuideContainer.setVisible(true);
			SearchResultsPanel<ArtExhibitionGuide> panel = new SearchResultsPanel<ArtExhibitionGuide>("results", getModel(), ag_list) {
				@Override
				protected void onClick(IModel<ArtExhibitionGuide> model) {
					setResponsePage( new ArtExhibitionGuidePage(model));
				}
			};
			itemsArtExhibitionGuideContainer.add( panel );
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		if (getListToolbarItems()!=null)
			getListToolbarItems().forEach(i->i.detach());
		
		 if ( gc_list!=null)
			 gc_list.forEach( i-> i.detach());
		 
		 if (ag_list!=null)
			 ag_list.forEach(i->i.detach());
	}

	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}
	
	
	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}
	
	protected void onClick(IModel<GuideContent> model, AjaxRequestTarget target) {
		
	}
	
	/**
	 * 
	 * 
	 */
	protected void addForm() {

		this.form = new Form<Void>("form");
		add(this.form);

		this.aidField = new TextField<String>("aid", new PropertyModel<String>(this, "audioId"), null);

		this.aidField.setPlaceHolderLabel(getLabel("search-by-aid"));
		this.form.add(aidField);

		LinkSubmitButton<Void> submit = new LinkSubmitButton<Void>("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				 SiteSearcherPanel.this.onSubmit();
			}

			public String getIcon() {
				return SiteSearcherPanel.this.getIcon();
				 
			}

			public String getIconStyle() {
				return  SiteSearcherPanel.this.getIconStyle();
			}

			public IModel<String> getLabel() {
				return null;
			}

			protected String getSaveCss() {
				return  SiteSearcherPanel.this.getSaveCss();
			}
		};

		this.aidField.setCss("form-control text-start text-md-start text-lg-start text-xl-start text-xxl-start");

		submit.setRowCss("d-inline-block");
		submit.setColCss("w-100 mt-xxl-0 mt-xl-0 mt-lg-0 mt-md-0 mt-sm-0 mt-xs-0 pt-0");

		if (getSaveStyle() != null)
			submit.setStrStyle(getSaveStyle());

		this.form.add(submit);
		this.form.setFormState(FormState.EDIT);
		this.form.visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();
			}
		});
	}
	
	
	protected void onSubmit() {
		
		 this.form.updateModel();

		 gc_list = generateGuideContentList();
		 if (gc_list!=null && gc_list.size()==1 && (getAudioId()!=null)) {
				setResponsePage( new GuideContentPage( gc_list.get(0)));
				return;
		 }

		ag_list	= generateArtExhibitionGuideList();
		if (ag_list!=null && ag_list.size()==1 && (getAudioId()!=null)) {
			 setResponsePage( new ArtExhibitionGuidePage( ag_list.get(0)));
			 return;
		}
	
		setResponsePage(new SiteSearcherPage( getModel(), gc_list, ag_list));
	}
	
	protected void loadResultsPage(List<IModel<GuideContent>> gc_list, List<IModel<ArtExhibitionGuide>> ag_list) {
		logger.debug("search");
		
		if (gc_list!=null && gc_list.size()>0) 
			setResponsePage( new GuideContentPage( gc_list.get(0)));
		else
			setResponsePage( new SiteSearcherPage( getModel(), gc_list,  ag_list));
	}

	protected String getSaveCss() {
		return "btn btn-primary btn-sm";
	}

	protected String getSaveStyle() {
		return null;
	}

	public String getIcon() {
		return "fa-solid fa-magnifying-glass";
	}

	public String getIconStyle() {
		 
		return null;

	}

	
	protected void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return (getListToolbarItems() != null && getListToolbarItems().size() > 0);
			}
		};

		this.listToolbarContainer.setOutputMarkupId(true);
		add(this.listToolbarContainer);

		List<ToolbarItem> list = getListToolbarItems();

		if (list != null && list.size() > 0) {
			Toolbar toolbarItems = new Toolbar("listToolbar");
			list.forEach(t -> toolbarItems.addItem(t));
			this.listToolbarContainer.add(toolbarItems);
		} else {
			this.listToolbarContainer.add(new InvisiblePanel("listToolbar"));
		}
	}
	
 
	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				//reloadList();
				//event.getTarget().add( getItemsGuideContentContainer());
				//event.getTarget().add( getListToolbarContainer());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});
	}
	
	/**
	@Override
	protected void onClick(IModel<GuideContent> model) {
		setResponsePage(new GuideContentPage(model,getItems()));
	}
	**/

	 
	protected  List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		//IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		//ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		//listToolbar.add(s);

		return listToolbar;
	}

	 
	protected synchronized  List<IModel<GuideContent>> generateGuideContentList() {

		List<IModel<GuideContent>> list = new ArrayList<IModel<GuideContent>>();
		
		Long l_aid = null;
		
		try {
			if (getAudioId()!=null && getAudioId().length()>0)
				l_aid = Long.valueOf(getAudioId());
		} catch (Exception e) {
			l_aid = Long.valueOf(-1);
		}
		
	 	if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> list.add(new ObjectModel<GuideContent>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.EDITION).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.DELETED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
		
		return list;
	
	}
	
	protected synchronized  List<IModel<ArtExhibitionGuide>> generateArtExhibitionGuideList() {

		List<IModel<ArtExhibitionGuide>> list = new ArrayList<IModel<ArtExhibitionGuide>>();
		
		Long l_aid = null;
		
		 
		try {
			if (getAudioId()!=null && getAudioId().length()>0)
				l_aid = Long.valueOf(getAudioId());
		} catch (Exception e) {
			l_aid = Long.valueOf(-1);
		}
	 
		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<ArtExhibitionGuide>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> list.add(new ObjectModel<ArtExhibitionGuide>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION).forEach(s ->  list.add(new ObjectModel<ArtExhibitionGuide>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid).forEach(s ->  list.add(new ObjectModel<ArtExhibitionGuide>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.DELETED).forEach(s ->  list.add(new ObjectModel<ArtExhibitionGuide>(s)));
		
		return list;
	
	}
	
	
}
