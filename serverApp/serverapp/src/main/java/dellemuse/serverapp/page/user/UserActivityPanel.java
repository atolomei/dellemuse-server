package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.DTFormatter;
import dellemuse.serverapp.service.DateTimeService;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.ModelPanel;

public class UserActivityPanel extends DBModelPanel<User> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	static private Logger logger = Logger.getLogger(UserActivityPanel.class.getName());

	private ListPanel<DelleMuseAudit> itemsPanel;
	private List<IModel<DelleMuseAudit>> items;
	private IModel<String> title;

	public UserActivityPanel(String id, IModel<User> model) {
		super(id, model);
	}

	public IModel<String> getTitle() {
		return this.title;
	}

	public void setTitle(IModel<String> t) {
		this.title = t;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (items != null)
			items.forEach(c -> c.detach());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		getPersonDBService().getByUser(getModel().getObject()).ifPresent(p -> {
			setTitle(getLabel("activity-title", p.getDisplayname()));
		});

		if (getTitle() == null) {
			setTitle(Model.of(getModel().getObject().getDisplayname()));
		}
		
		add(new Label("title", getTitle()));
		addItems();
	}

	protected List<IModel<DelleMuseAudit>> getItems() {
		if (items != null)
			return items;

		items = new ArrayList<IModel<DelleMuseAudit>>();

		getDelleMuseAuditDBService().getObjectActivity(getModel().getObject()).forEach(v -> {
			v = getDelleMuseAuditDBService().findWithDeps(v.getId()).get();
			items.add(new dellemuse.serverapp.audit.AuditModel(v));
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
		return new ObjectListItemExpandedPanel<DelleMuseAudit>("expanded-panel", model, mode) {
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return UserActivityPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return UserActivityPanel.this.getObjectSubtitle(getModel());
			}
		};
	}

	protected IModel<String> getObjectSubtitle(IModel<DelleMuseAudit> model) {
		return null;
	}

	protected IModel<String> getObjectInfo(IModel<DelleMuseAudit> model) {
		return Model.of(model.getObject().getDescription());
	}

	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	private IModel<String> getItemTitle(IModel<DelleMuseAudit> m) {
		DelleMuseAudit o = m.getObject();
		StringBuilder str = new StringBuilder();

		str.append(getDateTimeService().format(
				o.getLastModified(), 
				getSessionUser().get().getZoneId().getId(), 
				getSessionUser().get().getLocale(),
				DTFormatter.Day_Month_Year_hh_mm_ss_zzz));

		
		if (o.getAction()!=AuditAction.SIGNIN && o.getAction()!=AuditAction.SIGNOUT) {
			str.append(" - ");
			str.append(o.getObjectClassName());
			str.append(" ( ");
			str.append(o.getObjectId());
			str.append(" ) ");
				
		}
		str.append(" - ");
		str.append(o.getAction().getLabel());

		if (m.getObject().getDescription() != null) {
			str.append(" - ");
			str.append(m.getObject().getDescription());
		}

		return Model.of(str.toString());
	}

	private void addItems() {
		this.itemsPanel = new ListPanel<DelleMuseAudit>("items") {
			private static final long serialVersionUID = 1L;

			protected List<IModel<DelleMuseAudit>> filter(List<IModel<DelleMuseAudit>> initialList, String filter) {
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
			public IModel<String> getItemLabel(IModel<DelleMuseAudit> model) {
				return UserActivityPanel.this.getItemTitle(model);
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<DelleMuseAudit> model, ListPanelMode mode) {
				return UserActivityPanel.this.getObjectListItemExpandedPanel(model, mode);
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
						return getItemTitle(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return Model.of(getModel().getObject().toString());
					}
				};
				return panel;
			}

			@Override
			public List<IModel<DelleMuseAudit>> getItems() {
				return UserActivityPanel.this.getItems();
			}
		};
		add(itemsPanel);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}
}
