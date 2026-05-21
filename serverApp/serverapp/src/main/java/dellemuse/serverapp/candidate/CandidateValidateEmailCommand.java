package dellemuse.serverapp.candidate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.command.Command;
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.PersistentToken;

public class CandidateValidateEmailCommand extends Command {

	static private Logger logger = Logger.getLogger(CandidateValidateEmailCommand.class.getName());

	@JsonProperty("candidateId")
	private Long candidateId;

	public CandidateValidateEmailCommand(Long aId) {
		this.candidateId = aId;
	}

	public static double vowelRatio(String s) {

		if (s == null || s.isBlank()) {
			return 0.0;
		}

		// vocales comunes en idiomas occidentales
		final String vowels = "aeiou" + "áéíóú" + "àèìòù" + "âêîôû" + "äëïöü" + "ãõ" + "åæøœ" + "AEIOU" + "ÁÉÍÓÚ" + "ÀÈÌÒÙ" + "ÂÊÎÔÛ" + "ÄËÏÖÜ" + "ÃÕ" + "ÅÆØŒ";

		int letters = 0;
		int vowelCount = 0;

		for (char c : s.toCharArray()) {

			if (Character.isLetter(c)) {

				letters++;

				if (vowels.indexOf(c) >= 0) {
					vowelCount++;
				}
			}
		}

		if (letters == 0) {
			return 0.0;
		}

		return (double) vowelCount / letters;
	}

	/**
	 * boolean suspicious = entropy(s) > 4.2 && vowelRatio(s) < 0.25 &&
	 * randomCase(s);
	 * 
	 * Eso detecta muchísimos spambots.
	 **/

	public static double entropy(String s) {

		Map<Character, Integer> freq = new HashMap<>();

		for (char c : s.toCharArray()) {
			freq.put(c, freq.getOrDefault(c, 0) + 1);
		}

		double result = 0.0;

		for (int f : freq.values()) {
			double p = (double) f / s.length();
			result -= p * (Math.log(p) / Math.log(2));
		}

		return result;
	}

	public static boolean randomCase(String s) {

		int transitions = 0;

		for (int i = 1; i < s.length(); i++) {

			boolean prevUpper = Character.isUpperCase(s.charAt(i - 1));
			boolean currUpper = Character.isUpperCase(s.charAt(i));

			if (prevUpper != currUpper) {
				transitions++;
			}
		}

		return transitions > (s.length() / 4.2);
	}

	@Override
	public void execute() {

		
		
		if (this.candidateId == null) {
			logger.error(this.getClass().getSimpleName() + ": candidateId is null", ServerConstant.NOT_THROWN);
			return;
		}

		Candidate c = getCandidateDBService().findById(candidateId).orElse(null);


		if (c == null)
			return;

		if (c.isEmailValidated())
			return;

		if (c.getValidationEmailSent() != null)
			return;

		if (c.getState() == ObjectState.DELETED)
			return;

		if (c.getState() == ObjectState.EDITION)
			return;

		if (c.getStatus() != CandidateStatus.SUBMITTED)
			return;

		logger.debug("Executing " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());

		
		if (c.getEmail() == null || c.getEmail().length() == 0) {
			logger.error("email is null", ServerConstant.NOT_THROWN);
			logger.debug("Aborting " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());
			return;
		}

		if (!c.getEmail().matches("^[\\w+\\-]+(\\.[\\w+\\-]+)?@[\\w\\-]+(\\.[\\w\\-]+)*\\.[a-zA-Z]{2,}$")) {
			logger.error("invalid email address -> " + c.getEmail(), ServerConstant.NOT_THROWN);
			c.setBotSuspected(true);
			getCandidateDBService().save(c);
			logger.debug("Aborting " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());
			return;
			 
		}

		
		
		// -- Option 1: Honeypot check - bots tend to fill all fields
		if (c.getHoneypot() != null && !c.getHoneypot().isEmpty()) {
			logger.error("honeypot triggered for candidate -> " + c.getEmail(), ServerConstant.NOT_THROWN);
			c.setInternalcomments(" honeypot triggered"); 
			c.setBotSuspected(true);
			getCandidateDBService().save(c);
			logger.debug("Aborting " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());
			return;
		}

		// -- Option 2: Submission time check - bots submit instantly
		if (c.getFormOpenedAt() != null) {
			long seconds = java.time.Duration.between(c.getFormOpenedAt(), c.getCreated()).getSeconds();
			if (seconds < 5) {
				logger.error("form submitted too fast (bot suspected) " + seconds + "s -> " + c.getEmail(), ServerConstant.NOT_THROWN);
				c.setInternalcomments("form submitted too fast (bot suspected) -Z " + seconds + "secs " );
				c.setBotSuspected(true);
				getCandidateDBService().save(c);
				logger.debug("Aborting " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());
				return;
			}
		}

		

		StringBuilder sb = new StringBuilder();

		sb.append(c.getPersonName() != null ? c.getPersonName() : "");
		sb.append(sb.length() > 0 ? " " : "");
		sb.append(c.getPersonLastname() != null ? c.getPersonLastname() : "");
		sb.append(sb.length() > 0 ? " " : "");
		sb.append(c.getInstitutionName() != null ? c.getInstitutionName() : "");
		sb.append(sb.length() > 0 ? " " : "");
		sb.append(c.getComments() != null ? c.getComments() : "");

		// double entropy = entropy(sb.toString());

		String botSuspected = "bot suspected ->  entropy " + "( " + entropy(sb.toString()) + " )" + " | vowel ratio" + "( " + (vowelRatio(sb.toString())) + " )" + " | high case transitions " + "( " + randomCase(sb.toString()+")");

		
		

		boolean suspicious = (entropy(sb.toString()) > 3.9 && vowelRatio(sb.toString()) < 0.25) || randomCase(sb.toString());
		if (suspicious) {
			
			logger.error(botSuspected, ServerConstant.NOT_THROWN);
			logger.debug("Aborting " + this.getClass().getSimpleName() + " for candidate -> " + c.getDisplayname());

			c.setInternalcomments(botSuspected);
			c.setBotSuspected(true);
			getCandidateDBService().save(c);
			return;
		}

		//
		//
		// TBA
		//
		// -- Option 3: Cloudflare Turnstile captcha verification
		// if (!verifyCaptchaToken(c.getCaptchaToken())) {
		// logger.error("captcha verification failed for candidate -> " + c.getEmail(),
		// ServerConstant.NOT_THROWN);
		// return;
		// }


		// -- token for email validation
		// -------------------------------------------------------
		//

		String tokenValue = getSecurityService().nextSecureToken();

		@SuppressWarnings("unused")
		PersistentToken token = getPersistentTokenDBServiceDBService().create(c.getId().toString(), Candidate.class.getSimpleName(), tokenValue, OffsetDateTime.now().plusDays(7));

		String personName = c.getPersonName() != null ? c.getPersonName() : "";
		String personLastname = c.getPersonLastname() != null ? c.getPersonLastname() : "";
		String name = (personName + " " + personLastname).trim();

		String to = c.getEmail();

		String subject = "Dellemuse - Sign up confirmation";

		String url = getServerDBSettings().getEmailValidationServer() + "/" + DellemuseServer.URL_CANDIDATE_VALIDATE_EMAIL + "/" + c.getId().toString() + "-" + tokenValue + "-" + c.getLanguage();

		// --------- Send email to Candidate to validate email -----------
		//

		logger.debug("----------------");
		logger.debug("Candidate email validation url -> " + url);
		logger.debug("application -> " + DellemuseServer.APPNAME);
		logger.debug("personName -> " + name);
		logger.debug("email -> " + c.getEmail());
		logger.debug("institution -> " + c.getInstitutionName());
		logger.debug("lang -> " + c.getLanguage());
		logger.debug("----------------");

		String lang = c.getLanguage();

		if (lang == null)
			lang = getSettings().getDefaultLocale().getLanguage();

		String text = getEmailTemplateService().render(EmailTemplateService.CANDIDATE_EMAIL_VALIDATION, lang, Map.of("confirmationLink", url, "application", DellemuseServer.APPNAME, "personName", name));

		try {
			String sendEmail;
			sendEmail = getEmailService().sendHTML(to, subject, text);

			c.setValidationEmailSent(OffsetDateTime.now());
			getCandidateDBService().save(c);

			logger.debug("Candidate email validation response -> " + sendEmail);

		} catch (IOException | InterruptedException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}

		// --------- Send email to Admin ------------------------------------
		//

		try {

			String textAdmin = getEmailTemplateService().render(EmailTemplateService.CANDIDATE_SUBMT_NOTIFY_ADMIN,
					Map.of("application", DellemuseServer.APPNAME, "name", (c.getPersonName() != null ? c.getPersonName() : "null"), "lastname", (c.getPersonLastname() != null ? c.getPersonLastname() : "null"), "institution",
							(c.getInstitutionName() != null ? c.getInstitutionName() : "null"), "address", (c.getInstitutionAddress() != null ? c.getInstitutionAddress() : "null"), "email", (c.getEmail() != null ? c.getEmail() : "null"),
							"phone", (c.getPhone() != null ? c.getPhone() : "null"), "comments", (c.getComments() != null ? c.getComments() : "null")));

			String toAdmin = getRootUser().getEmail();
			String subjectAdmin = "Institution registration";

			logger.debug("----------------");
			logger.debug("Institution registration");
			logger.debug("----------------");

			String sendEmailAdmin = getEmailService().sendText(toAdmin, subjectAdmin, textAdmin);

			logger.debug("Email to Admin sent response -> " + sendEmailAdmin);

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}

	/**
	 * Verifies the Cloudflare Turnstile captcha token against the Turnstile API.
	 * Returns false if the token is null, blank, or verification fails. Configure
	 * the secret key in application.properties:
	 * {@code captcha.turnstile.secret.key=YOUR_SECRET_KEY}
	 */
	private boolean verifyCaptchaToken(String token) {
		if (token == null || token.isBlank()) {
			logger.error("captcha token is null or blank", ServerConstant.NOT_THROWN);
			return false;
		}
		try {
			String secret = getServerDBSettings().getCaptchaSecretKey();
			if (secret == null || secret.isBlank()) {
				logger.error("captcha.turnstile.secret.key is not configured", ServerConstant.NOT_THROWN);
				return false;
			}
			String body = "secret=" + secret + "&response=" + token;
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://challenges.cloudflare.com/turnstile/v0/siteverify")).header("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(body)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			boolean success = response.body().contains("\"success\":true");
			if (!success)
				logger.error("Turnstile response -> " + response.body(), ServerConstant.NOT_THROWN);
			return success;
		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return false;
		}
	}

}
