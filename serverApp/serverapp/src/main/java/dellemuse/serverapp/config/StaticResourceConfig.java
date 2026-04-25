package dellemuse.serverapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dellemuse.serverapp.ServerDBSettings;

import java.io.File;

/**
 * Serves files from the help directory (helpDir) as static web resources.
 * 
 * Images referenced in help templates as /help/images/filename.ext
 * are served from <helpDir>/images/ on the filesystem.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

	@Autowired
	private ServerDBSettings settings;

	@Override
	public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

		String helpDir = settings.getHelpDir();

		// Resolve to absolute path if relative
		File helpDirFile = new File(helpDir);
		if (!helpDirFile.isAbsolute())
			helpDirFile = helpDirFile.getAbsoluteFile();

		String helpImagesLocation = "file:" + helpDirFile.getAbsolutePath() + File.separator + "images" + File.separator;

		registry.addResourceHandler("/help/images/**")
				.addResourceLocations(helpImagesLocation);
	}
}
