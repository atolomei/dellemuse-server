package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.candidate.CandidatePage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.ModelPanel;

public class HomeAdminCandidatesPanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HomeAdminCandidatesPanel.class.getName());

	private List<IModel<Candidate>> list;

	public HomeAdminCandidatesPanel(String id, IModel<User> model) {
		super(id, model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (list != null)
			list.forEach(m -> m.detach());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		loadList();

		setVisible(!list.isEmpty());

		ListPanel<Candidate> candidateList = new ListPanel<Candidate>("candidateList") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<Candidate> model) {
				return getObjectTitle(model);
			}

			@Override
			public List<IModel<Candidate>> getItems() {
				return HomeAdminCandidatesPanel.this.list;
			}

			@Override
			protected Panel getListItemPanel(IModel<Candidate> model, ListPanelMode mode) {
				return new DelleMuseObjectListItemPanel<Candidate>("row-element", model, mode) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new CandidatePage(getModel(), HomeAdminCandidatesPanel.this.list));
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return HomeAdminCandidatesPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return HomeAdminCandidatesPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected IModel<String> getObjectSubtitle() {
						return HomeAdminCandidatesPanel.this.getObjectSubtitle(getModel());
					}
				};
			}
		};

		candidateList.setHasExpander(false);
		candidateList.setLiveSearch(false);
		candidateList.setSettings(false);
		add(candidateList);
	}

	private void loadList() {
		list = new ArrayList<>();
		getCandidateDBService()
				.findByStatus(CandidateStatus.SUBMITTED, CandidateStatus.EVALUATION)
				.forEach(c -> list.add(new ObjectModel<Candidate>(c)));
	}

	protected IModel<String> getObjectTitle(IModel<Candidate> model) {
		Candidate c = model.getObject();
		StringBuilder str = new StringBuilder();
		if (c.getPersonLastname() != null && !c.getPersonLastname().isBlank())
			str.append(c.getPersonLastname());
		if (c.getPersonName() != null && !c.getPersonName().isBlank()) {
			if (str.length() > 0) str.append(", ");
			str.append(c.getPersonName());
		}
		if (str.length() == 0)
			str.append(c.getDisplayname());
		return Model.of(str.toString());
	}

	protected IModel<String> getObjectSubtitle(IModel<Candidate> model) {
		Candidate c = model.getObject();
		String status = c.getStatus() != null ? c.getStatus().getLabel() : "";
		String institution = c.getInstitutionName() != null ? c.getInstitutionName() : "";
		if (!institution.isBlank() && !status.isBlank())
			return Model.of(institution + " — " + status);
		if (!institution.isBlank())
			return Model.of(institution);
		return Model.of(status);
	}

	protected IModel<String> getObjectInfo(IModel<Candidate> model) {
		return Model.of(TextCleaner.clean(model.getObject().getComments(), 280));
	}

	protected CandidateDBService getCandidateDBService() {
		return (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);
	}
}
