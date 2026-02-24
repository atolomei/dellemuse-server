package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
 
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
 
import io.wktui.form.field.TextField;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;
import wktui.base.ModelPanel;

public abstract class SearchResultsPanel<T extends  MultiLanguageObject> extends DBModelPanel<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SearchResultsPanel.class.getName());
	
	private List<IModel<T>> list;
	private ListPanel<T> itemsPanel;
	private WebMarkupContainer itemsContainer;

	
	public SearchResultsPanel(String id, IModel<Site> model, List<IModel<T>> list) {
		super(id, model);
		this.list=list;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		itemsContainer = new WebMarkupContainer("itemsContainer");
		add(itemsContainer);
		itemsContainer.setVisible(false);
		itemsContainer.setOutputMarkupId(true);
		
		 addItems();

		
		 
	}


	protected IModel<String> getObjectInfo(IModel<T> model) {
		StringBuilder str = new StringBuilder();
	
		/**str.append(TextCleaner.clean(getLanguageObjectService().getInfo(model.getObject(), getLocale()), 480));
		str.append("<br/>");
		str.append("<b>Audio id</b>");
 		str.append(model.getObject().getArtWorkAudioId() != null ? (". " + model.getObject().getArtWorkAudioId().toString()) : ". n/a");
**/
		return Model.of(str.toString());
	}
	
	protected IModel<String> getObjectSubtitle(IModel<T> model) {
		return null;
	}

	protected IModel<String> getObjectTitle(IModel<T> model) {

		StringBuilder str = new StringBuilder();

		T o = model.getObject();

		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		//str.append(model.getObject().getArtWorkAudioId() != null ? ("  <span class=\"small  text-secondary\"> (" + model.getObject().getArtWorkAudioId().toString() + ") </span>" ) : ". n/a");
		
		
		if (model.getObject() instanceof GuideContent) {
		
			str.append("<span class=\"small  text-secondary\"> (" + getLabel("artwork-audio" ) .getObject() + ") </span>");

			
			boolean accesible= getArtExhibitionGuideDBService().findById(((GuideContent) model.getObject()).getArtExhibitionGuide().getId()).get().isAccessible();
			if (accesible) {
				str.append( Icons.ACCESIBLE_ICON_HTML );
			}
		}
		else if  (model.getObject() instanceof ArtExhibitionGuide) {
			
			str.append("<span class=\"small  text-secondary\"> (" + getLabel("audio-guide" ) .getObject() + ") </span>");

			boolean accesible= ((ArtExhibitionGuide) model.getObject()).isAccessible();
			
			if (accesible) {
				str.append( Icons.ACCESIBLE_ICON_HTML );
			}
		}
		
		if (o.getState() == ObjectState.DELETED)
			str.append(Icons.DELETED_ICON_HTML);

		if (o.getState() == ObjectState.EDITION)
			str.append(Icons.EDITION_ICON_HTML);
	
		return Model.of(str.toString());
	}
	
	
	protected String getObjectImageSrc(IModel<T> model) {
		return null;
		//return super.getImageSrc(model.getObject());
	}

	protected void setList(List<IModel<T>> list) {
		this.list = list;
	}

	protected List<IModel<T>> getList() {
		return this.list;
	}
	
	 
	/**
	public Iterable<T> getObjects() {
		return this.getObjects(null, null);
	}
	
	public Iterable<T> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	public abstract Iterable<T> getObjects(ObjectState os1, ObjectState os2);
		// return nullgetSiteGuideContents(getModel().getObject());
	**/
	
	@Override
	public void onDetach() {
		super.onDetach();
		if (this.list != null)
			this.list.forEach(i -> i.detach());
	}
	
	
	protected WebMarkupContainer getMenu(IModel<T> model) {
		return null;
	}
	
	/**
	protected List<IModel<T>> getItems() {
		if (this.list == null) {
			this.list = generateList();
		}
		return this.list;
	}
	
	protected abstract List<IModel<T>> generateList();
	**/
	
	protected Panel getObjectListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {

		//model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get());

		return new ObjectListItemExpandedPanel<T>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return SearchResultsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return SearchResultsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return SearchResultsPanel.this.getObjectImageSrc(getModel());
			}
		};
	}
	
	
	protected void addItems() {

		this.itemsPanel = new ListPanel<T>("items") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<T>model) {
				return SearchResultsPanel.this.getObjectTitle(model);
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<T>model, ListPanelMode mode) {
				return SearchResultsPanel.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<T>model) {

				DelleMuseObjectListItemPanel<T>panel = new DelleMuseObjectListItemPanel<T>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return SearchResultsPanel.this.getObjectTitle(getModel());
					}

					protected String getImageSrc() {
						return SearchResultsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						SearchResultsPanel.this.onClick(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return SearchResultsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return SearchResultsPanel.this.getMenu(getModel());
					}

					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio() != null)
							return Icons.headphoneIcon;
						else
							return null;
					}
				};
				return panel;
			}

			@Override
			public List<IModel<T>> getItems() {
				return SearchResultsPanel.this.getItems();
			}
		};

		itemsContainer.addOrReplace(itemsPanel);
		itemsContainer.setVisible(true);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setItemMenu(false);
		itemsPanel.setHasExpander(true);
	}

	protected List<IModel<T>> getItems() {
		return list;
	}

	protected abstract void onClick(IModel<T> model);
	
	
	
}
