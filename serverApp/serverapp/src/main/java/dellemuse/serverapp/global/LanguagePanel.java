package dellemuse.serverapp.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.IValueMap;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.form.field.ChoiceField;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;

public class LanguagePanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(LanguagePanel.class.getName());

	private DropDownChoice<Language> selector;
	private List<Language> languages;
	private Language lang;
	
	public LanguagePanel(String id) {
		this(id, null);
	}
	
	public LanguagePanel(String id, IModel<User> model) {
		super(id, model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setLanguage(Language.of(getModel().getObject().getLocale().getLanguage()));
		
	 
		languages = getLanguageService().getLanguagesSorted(getModel().getObject().getLocale());
		 

		this.selector = new DropDownChoice<Language>("languages", getChoices()) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			protected String getNullValidDisplayValue() {
				return "null";
			}

			@Override
			public boolean isNullValid() {
				return false;
			}

			@Override
			public boolean isRequired() {
				return true;
			}

			@Override
			protected void onComponentTag(final ComponentTag tag) {
				// IValueMap attributes = tag.getAttributes();
				// if (autofocus())
				// attributes.putIfAbsent("autofocus", "");
				
				
				super.onComponentTag(tag);
			
				//logger.debug("aca");
					
			}
		};

		selector.setModel(new PropertyModel<Language>(this, "language"));

		selector.setChoiceRenderer(new ChoiceRenderer<Language>() {

			private static final long serialVersionUID = 1L;

			public String getIdValue(Language value, int index) {
				return LanguagePanel.this.getIdValue(value);
			};

			public String getDisplayValue(Language value) {
				return LanguagePanel.this.getDisplayValue(value);
			};
		});

		// if (getModel().getObject()!=null)
		// setValue( ChoiceField.this.getDisplayValue(getModel().getObject()));

		add(selector);
	}

	
	public Language getLanguage() {
		return this.lang;
	}

	public void setLanguage(Language lang)  {
		this.lang=lang;
	}
	
	protected List<Language> getChoices() {
		return languages;
	}

	protected String getDisplayValue(Language value) {
		return value.getLabel(getLocale());
	}

	protected String getIdValue(Language value) {
		return value.getLanguageCode();
	}

	public LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
