package dellemuse.serverapp.branded.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import dellemuse.serverapp.branded.SearchGlobalTopPanelEvent;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.serverdb.model.AccesibilityMode;

public class BrandedSearchTopPanel extends ObjectModelPanel<Void> {
	private static final long serialVersionUID = 1L;

	public BrandedSearchTopPanel(String id) {
		super(id, null);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		AjaxLink<Void> link = new AjaxLink<Void>("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				fire(new SearchGlobalTopPanelEvent("search", target));
			}
		};
		add(link);

		link.add(new org.apache.wicket.AttributeModifier("class", "btn border bg-dark "));

	}

	protected String getDisplayValue(AccesibilityMode value) {
		return value.getLabel(getLocale());
	}

	protected String getIdValue(AccesibilityMode value) {
		return String.valueOf(value.getId());
	}

}
