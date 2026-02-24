package dellemuse.serverapp.branded.panel;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;


import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.branded.BrandedArtExhibitionGuidePage;
import dellemuse.serverapp.branded.BrandedGuideContentPage;
import dellemuse.serverapp.branded.BrandedSitePage;
import dellemuse.serverapp.branded.SearchAudioEvent;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.BaseSiteSearcherPanel;
import dellemuse.serverapp.page.site.SearchResultsPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.LinkSubmitButton;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;
 
import io.wktui.form.field.Field;

public class BrandedSiteSearcherPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedSiteSearcherPanel.class.getName());
	
	
	private List<ToolbarItem> listToolbar;
	 
	private Form<Void> form;
	private String audioId = "";
	private TextField<String> aidField;
	
	
	private WebMarkupContainer closeContainer;
	
 	private WebMarkupContainer itemsGuideContentsContainer;
	private WebMarkupContainer itemsArtExhibitionGuideContainer;
	
	private WebMarkupContainer listToolbarContainer;

	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private List<IModel<GuideContent>> gc_list;
	private List<IModel<ArtExhibitionGuide>> ag_list;

	public BrandedSiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	public  BrandedSiteSearcherPanel(String id, IModel<Site> model,  List<IModel<GuideContent>> gc_list,  List<IModel<ArtExhibitionGuide>> ag_list) {
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
		
		// addListToolbar();
		addForm();
		
		
		
		
		final boolean isClose = (gc_list!=null && gc_list.size()>1) || (ag_list!=null && ag_list.size()>0);
	

		closeContainer = new WebMarkupContainer("closeContainer") {
			public boolean isVisible() {
					return isClose;
			}
		};
		
		closeContainer.setOutputMarkupId(true);
		add(closeContainer);
		
		
		 Link<Void> close = new  Link<Void>( "close") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				fire (new SearchAudioEvent("search-audio", BrandedSiteSearcherPanel.this.getModel(), null, null ));
			}
			public boolean isVisible() {
				return isClose;
			}
		};
		closeContainer.add(close);
	  	
	
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
					setResponsePage( new BrandedGuideContentPage(model));
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
					setResponsePage( new BrandedArtExhibitionGuidePage(model));
				}
			};
			itemsArtExhibitionGuideContainer.add( panel );
		}
		
		
		
	 
		
		 
	}
	
	 
	
	protected String getSaveCss() {
		return "btn btn-outline btn-sm";
	}
	
	protected String getSaveStyle() {
	 
	 	return "margin-top: 0px;    padding-top: 8px;    padding-bottom: 5px;    border-top: none;    border-bottom: none;    border-right: none;    border-left: 1px solid #495057;     font-size: 13px;    border-radius: 0 6px 6px 0;    padding-left: 12px;    padding-right: 12px;";
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
				 BrandedSiteSearcherPanel.this.onSubmit();
			}

			public String getIcon() {
				return BrandedSiteSearcherPanel.this.getIcon();
				 
			}

			public String getIconStyle() {
				return  BrandedSiteSearcherPanel.this.getIconStyle();
			}

			public IModel<String> getLabel() {
				return null;
			}

			protected String getSaveCss() {
				return  BrandedSiteSearcherPanel.this.getSaveCss();
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
	
	
	public String getIcon() {
		return "fa-solid fa-magnifying-glass";
	}

	
	
	protected void onSubmit() {
		
		 this.form.updateModel();

		 gc_list = generateGuideContentList();
		 if (gc_list!=null && gc_list.size()==1) {
				setResponsePage( new BrandedGuideContentPage( gc_list.get(0)));
				return;
		 }

		ag_list	= generateArtExhibitionGuideList();
		if (ag_list!=null && ag_list.size()==1) {
			 setResponsePage( new  BrandedArtExhibitionGuidePage( ag_list.get(0)));
			 return;
		 }
	
		fire (new SearchAudioEvent("search-audio", BrandedSiteSearcherPanel.this.getModel(), gc_list, ag_list));
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
				//setObjectStateEnumSelector(event.getObjectStateEnumSelector());
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
		
		getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> list.add(new ObjectModel<GuideContent>(s)));
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
	 	getArtExhibitionGuideDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> list.add(new ObjectModel<ArtExhibitionGuide>(s)));
		return list;
	
	}
	
	 
	
}
