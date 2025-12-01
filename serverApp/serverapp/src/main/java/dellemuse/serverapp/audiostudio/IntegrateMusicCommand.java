package dellemuse.serverapp.audiostudio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;

import dellemuse.serverapp.command.Command;
import dellemuse.serverapp.command.CommandStatus;
import dellemuse.serverapp.serverdb.model.Resource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

public class IntegrateMusicCommand extends Command {

	static private Logger logger = Logger.getLogger(IntegrateMusicCommand.class.getName());

	@JsonProperty("voiceResourceId")
	private Long voiceResourceId;

	@JsonProperty("musicResourceId")
	private Long musicResourceId;

	@JsonProperty("musicUrl")
	private String musicUrl;

	StringBuilder errorMsg = new StringBuilder();
	
	private Integer introDurationSec = Integer.valueOf(20); // seconds of music before voice
	private Integer fadeDurationSec = Integer.valueOf(12); // duration of fade-out
	private Integer voiceOverlapDurationSec = Integer.valueOf(5); // duration of fade-out
	
	public IntegrateMusicCommand( 	Long voiceResourceId, 
									String musicUrl, 
									Integer introDurationSec, 
									Integer fadeDurationSec, 
									Integer fadeOverlapDurationSec) {
		
		this.voiceResourceId=voiceResourceId;
		this.musicUrl=musicUrl;
		this.introDurationSec=introDurationSec;
		this.fadeDurationSec=fadeDurationSec;
		this.voiceOverlapDurationSec=fadeOverlapDurationSec;
		
		downloadDir = getServerDBSettings().getAudioDownloadCacheDir();
		outputFile="null";
	}
	
	
	String outputFile;
	String downloadDir;
	private boolean success = false;
	
	
	public boolean isSuccess() {
		return this.success;
	}
	
	
	public String getoutputFilePath() {
		return downloadDir + File.separator + outputFile;
	}
	
	@Override
	public void execute() {

		setStatus(CommandStatus.RUNNING);
		
		if (this.musicUrl==null) {
			logger.error("musicUrl is null");
			errorMsg.append("musicUrl is null");
			return;
		}

		if (this.voiceResourceId==null) {
			logger.error("voiceResourceId is null");
			errorMsg.append("voiceResourceId is null");
			return;
		}
		
		if (this.introDurationSec<=0) {
			logger.error("introDurationSec too low");
			errorMsg.append("introDurationSec too low");
			return;
		}
	
		if (this.fadeDurationSec>this.introDurationSec) {
			logger.error("introDurationSec invalid");
			errorMsg.append("introDurationSec invalid");
			return;
		}
		
		if (this.voiceOverlapDurationSec>this.introDurationSec) {
			logger.error("voiceOverlapDurationSec invalid");
			errorMsg.append("voiceOverlapDurationSec invalid");
			return;
		}
		
		// -----------------------------------------------------------------------
				
		Resource resource = null;
		
		try {
			
			Optional<Resource> o_resource = getResourceDBService().findById(this.voiceResourceId);
			
			if (o_resource.isEmpty()) {
				logger.error("Resource not found -> " + this.voiceResourceId.toString());
				errorMsg.append("Resource not found -> " + this.voiceResourceId.toString());
				return;
			}
		
			resource = o_resource.get();
			 logger.debug(" voice resource ok  -> " + resource.getDisplayname());
	
		} catch (Exception e) {
			logger.error(e);
			errorMsg.append(e.getClass().getSimpleName()  + (e.getMessage()!=null? ("|"+e.getMessage()):""));
			return;
		}
		
		
		
		try {
		
			// mp3 -------------------------------------------------------------
			
			// String mp3FileName = this.extractFileName(this.musicUrl);

			String mp3FileName = "music-for-"+this.voiceResourceId.toString()+".mp3";
			String musicFile = downloadDir + File.separator + mp3FileName;
			
			// download mp3
			//
			try {
				
				
				downloadURL3(this.musicUrl, musicFile);
			} catch (IOException e1) {
				logger.error(e1);
				errorMsg.append(e1.getClass().getSimpleName()  + (e1.getMessage()!=null? ("|"+e1.getMessage()):""));
				return;
			}
			
			logger.debug("downloaded ok -> " + musicFile);
			
			// download voice  -------------------------------------------------------------
			//
			String voiceFileName = resource.getFileName();
			File voiceFile = new File( downloadDir, voiceFileName);
			
			try (InputStream is = getResourceDBService().getResourceService(resource).getInputStream()) {
				 Files.copy(is, voiceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e1) {
				logger.error(e1);
				errorMsg.append(e1.getClass().getSimpleName()  + (e1.getMessage()!=null? ("|"+e1.getMessage()):""));
				return;
			}
			//  -------------------------------------------------------------
			
			String baseName = FilenameUtils.getBaseName(voiceFileName);
			String extension = FilenameUtils.getExtension(voiceFileName);
			
			 outputFile = baseName+"-"+"music"+"."+extension;
			
			// String dir = "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/audioCache";

			int fadeStartSec = introDurationSec - fadeDurationSec; // start fade s before voice

			/**
			 * FFmpeg command explanation: 
			 * // 1. Take first input [0:a] (music) 
			 * // 2. Apply
			 * fade-out effect (starting fadeStartSec, lasting fadeDurationSec)
			 *  // 3. Delay
			 * the voice [1:a] so it starts after introDurationSec 
			 * // 4. Mix both audio
			 * streams
			 *
			 * 
			 * ffmpeg -i
			 * "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/audioCache/moonlight.mp3"
			 * -i
			 * "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/audioCache/411-museo-secreto.mp3"
			 * -filter_complex
			 * "[0:a]afade=t=out:st=25:d=5[aud1];[1:a]adelay=30000|30000[aud2];[aud1][aud2]amix=inputs=2:duration=longest[aout]"
			 * -map "[aout]" -y
			 * "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/audioCache/final.mp3"
			 *
			 *
			 **/

			String ffmpegPrefix = "/opt/homebrew/bin/ffmpeg";
			String ffmpegCmd = String.format(
					ffmpegPrefix  + 
					" -i \"%s\" -i \"%s\" " + 
					"-filter_complex " + 
					"\"[0:a]afade=t=out:st=%d:d=%d[aud1];" + 
					"[1:a]adelay=%d|%d[aud2];" + 
					"[aud1][aud2]amix=inputs=2:duration=shortest[aout]\" " + 
					"-map \"[aout]\" -y \"%s\"",
					 musicFile, 
					downloadDir + File.separator + voiceFileName, 
					fadeStartSec, 
					fadeDurationSec, (introDurationSec - voiceOverlapDurationSec) * 1000, 
					(introDurationSec - voiceOverlapDurationSec) * 1000,
					downloadDir + File.separator + outputFile);
			
			// Run FFmpeg using ProcessBuilder
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", ffmpegCmd);
			pb.inheritIO(); // prints FFmpeg output in console
			Process process;
			try {

				logger.debug("ffmpeg -> " + ffmpegCmd);

				process = pb.start();

				int exitCode = process.waitFor();
				if (exitCode == 0) {
					success  = true;
					logger.debug("Successfully created mixed audio: " + outputFile);
				} else {
					logger.debug("FFmpeg process failed with code: " + exitCode);
					errorMsg.append("FFmpeg process failed with code: " + String.valueOf(exitCode));
					return;
				}
			} catch (IOException e) {
				errorMsg.append(e.getClass().getSimpleName()  + (e.getMessage()!=null? ("|"+e.getMessage()):""));
				return;
			} catch (InterruptedException e) {
				errorMsg.append(e.getClass().getSimpleName()  + (e.getMessage()!=null? ("|"+e.getMessage()):""));
				return;
			}
		}
		catch ( RuntimeException e) {
			logger.error(e);
			errorMsg.append(e.getClass().getSimpleName()  + (e.getMessage()!=null? ("|"+e.getMessage()):""));
		} finally {
			setStatus(CommandStatus.TERMINATED);
		}
	}
	
	public String getErrorMsg() {
		return errorMsg.toString();
	}
	  
	private void downloadURL3(String urlString, String destinationFile) throws IOException {
		URI uri = URI.create(urlString);
		URL url = uri.toURL(); // works on all Java versions
		Path target = Path.of(destinationFile);
		try (InputStream in = url.openStream()) {
			Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading file: " + url, e);
		}
	}
 
	
	/**
	 * 
	 * 
	 * @param urlString
	 * @param destinationFile
	 * @throws IOException
	 */
	private void downloadURL(String urlString, String destinationFile)  {

		URI uri = URI.create(urlString);
		URL url;
	
		try {
	
			url = uri.toURL();
		
		} catch (MalformedURLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		Path target = Path.of(destinationFile);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request;
	
		try {
			
			request = HttpRequest.newBuilder().uri(url.toURI()).GET().build();

			try {
				
				client.send(request, HttpResponse.BodyHandlers.ofFile(target));
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			logger.debug("Downloaded  -> " + target.toAbsolutePath());

		} catch (URISyntaxException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param urlString
	 * @return
	 */
	private String extractFileName(String urlString) {

		URI uri = URI.create(urlString);
		URL url;
	
		try {
	
			url = uri.toURL();
		
		} catch (MalformedURLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		
		try {
		 
			String path = url.getPath();

			// Handle cases where the path might be empty or just a slash
			if (path == null || path.isEmpty() || "/".equals(path)) {
				return ""; // Or handle as an error/special case
			}

			// Find the last occurrence of '/' to get the potential file name
			int lastSlashIndex = path.lastIndexOf('/');
			String fileNameWithQuery = path.substring(lastSlashIndex + 1);

			// Remove any query parameters (e.g., "?key=value")
			int queryStartIndex = fileNameWithQuery.indexOf('?');
			if (queryStartIndex != -1) {
				return fileNameWithQuery.substring(0, queryStartIndex);
			} else {
				return fileNameWithQuery;
			}

		} catch (Exception e) {
			logger.error(e);
			logger.error("Invalid URL: " + urlString);
			throw new RuntimeException(e);
		}
	}
}
