package dellemuse.serverapp.help;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import jakarta.annotation.PostConstruct;

@Service
public class HelpService extends BaseService {

	private static Logger logger = Logger.getLogger(HelpService.class.getName());

	private Map<String, String> help = new ConcurrentHashMap<String, String>();

	@JsonIgnore
	private final OffsetDateTime created = OffsetDateTime.now();

	@Autowired
	public HelpService(ServerDBSettings settings) {
		super(settings);
	}

	public String gethelp(String key, String ilang) {

		String lang = getLanguageService().normalizeLanguage(ilang);

		String hk = hKey(key, lang);

		if (!logger.isDebugEnabled()) {
			if (help.containsKey(hk))
				return help.get(hk);
		}

		add(key, lang);

		return help.get(hk);

	}

	public void add(String key, String lang) {

		String dir = getSettings().getHelpDir() + File.separator + lang;

		File file = new File(dir, key + ".html");

		if (file.exists()) {
			try {
				String value = Files.readString(file.toPath());
				help.put(hKey(key, lang), value);
			} catch (IOException e) {
				logger.error(e, file.getAbsolutePath());
			}
		}
	}

	@PostConstruct
	protected void onInit() {

		try {
			setStatus(ServiceStatus.RUNNING);
		} catch (Exception e) {
			setStatus(ServiceStatus.STOPPED);
		}
	}

	public OffsetDateTime getOffsetDateTimeCreated() {
		return this.created;
	}

	private String hKey(String key, String lang) {
		return key + "#" + lang;
	}
}
