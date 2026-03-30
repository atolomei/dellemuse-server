package dellemuse.serverapp.candidate;

import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;

import dellemuse.serverapp.global.GlobalTopPanel;

import dellemuse.serverapp.page.BasePage;

import dellemuse.serverapp.serverdb.model.Candidate;

import dellemuse.serverapp.serverdb.model.User;

import wktui.base.InvisiblePanel;

@MountPath("/signup/${lang}")
public class CandidateOnboardPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Candidate.class.getName());

	private String slang;

	public CandidateOnboardPage() {
		super();

		this.slang = Locale.getDefault().getLanguage();
		this.setOutputMarkupId(true);

		if (slang != null && slang.equals("spa"))
			slang = "es";
		getSession().setLocale(Locale.forLanguageTag(slang));

	}

	public CandidateOnboardPage(PageParameters parameters) {
		super(parameters);

		if (getPageParameters() != null) {
			slang = getPageParameters().get("lang").toString();
		}

		if (slang == null)
			this.slang = Locale.getDefault().getLanguage();

		getSession().setLocale(Locale.forLanguageTag(slang));
	}

	@Override
	public Locale getLocale() {
		return Locale.forLanguageTag(slang);
	}

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public void onInitialize() {
		super.onInitialize();

		getSession().setLocale(Locale.forLanguageTag(slang));

		logger.debug(getSession().getLocale().getLanguage());

		add(new CandidateOnboardingEditor("candidateEditor", slang));

		add(new GlobalTopPanel("top-panel"));
		add(new InvisiblePanel("footer-panel"));
		add(new InvisiblePanel("error"));

	}

}