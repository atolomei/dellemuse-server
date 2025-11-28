package dellemuse.serverapp.audit.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionGuidesPanel;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuideContentsPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.DateTimeService;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.ModelPanel;

public class AuditPanel<T extends DelleMuseObject> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	static private Logger logger = Logger.getLogger(AuditPanel.class.getName());

	private ListPanel<DelleMuseAudit> itemsPanel;
	private List<IModel<DelleMuseAudit>> items;
	 
	public AuditPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		if (items!=null)
			items.forEach( c -> c.detach());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Label title = new Label( "title", getLabel("audit-title", getModel().getObject().getName()));
		add(title);
		
		addItems();
	}


	
	protected List<IModel<DelleMuseAudit>> getItems() {
	
		if (items!=null)
			return items;
		
		items = new ArrayList<IModel<DelleMuseAudit>>();
		
		getDelleMuseAuditDBService().getAudit(getModel().getObject().getId(), getModel().getObject().getObjectClassName()).forEach( 
				
				v -> {
						v = getDelleMuseAuditDBService().findWithDeps(v.getId()).get();
						items.add( new dellemuse.serverapp.audit.AuditModel(v));
		});
	
		return items;
	}

	protected DelleMuseAuditDBService getDelleMuseAuditDBService() {
		return (DelleMuseAuditDBService) ServiceLocator.getInstance().getBean(DelleMuseAuditDBService.class);
	}

	protected DateTimeService getDateTimeService() {
		return (DateTimeService) ServiceLocator.getInstance().getBean(DateTimeService.class);
	}

	protected WebMarkupContainer getObjectListItemExpandedPanel(IModel<DelleMuseAudit> model, ListPanelMode mode) {
			
		//model.setObject( super.findArtExhibitionGuideWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<DelleMuseAudit>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return  AuditPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return AuditPanel.this.getObjectSubtitle(getModel());
			}
		};
	}


	protected IModel<String> getObjectSubtitle(IModel<DelleMuseAudit> model) {
		return null;
	}

	
	protected IModel<String> getObjectInfo(IModel<DelleMuseAudit> model) {
		return Model.of( model.getObject().getDescription() );
	}

	private IModel<String> getItemTitle( IModel<DelleMuseAudit> m) {
	
		DelleMuseAudit o = m.getObject();
		StringBuilder str = new StringBuilder();
		
		str.append(  getDateTimeService().format(o.getLastModified()) );
	
		if (o.isDependencies()) {
			str.append(" - ");
			str.append( o.getUser().getUsername());
		}

		str.append(" - ");
		str.append(  o.getAction().getLabel() );

		
		return Model.of( str.toString() );
		
		
	}

	private void addItems() {

		this.itemsPanel = new ListPanel<DelleMuseAudit>("items") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<DelleMuseAudit>> filter(List<IModel<DelleMuseAudit>> initialList,
					String filter) {
				List<IModel<DelleMuseAudit>> list = new ArrayList<IModel<DelleMuseAudit>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<DelleMuseAudit> model, ListPanelMode mode) {
				return AuditPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}

			@Override
			protected Panel getListItemPanel(IModel<DelleMuseAudit> model) {
				
				ObjectListItemPanel<DelleMuseAudit> panel = new ObjectListItemPanel<DelleMuseAudit>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected String getTitleIcon() {
							return null;
					}

					@Override
					protected boolean isEqual(DelleMuseAudit o1, DelleMuseAudit o2) {
						return false;
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return  getItemTitle (getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return Model.of( getModel().getObject().toString() );
					}
							
				};
				return panel;
			}
		
			@Override
			public List<IModel<DelleMuseAudit>> getItems()  {
				return  AuditPanel.this.getItems();
			}
		};
		add(itemsPanel);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}
	

	
}

