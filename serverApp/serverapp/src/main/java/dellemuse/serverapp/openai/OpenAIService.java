package dellemuse.serverapp.openai;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;

import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.language.LanguageService;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class OpenAIService extends BaseService {

	static private Logger logger = Logger.getLogger(OpenAIService.class.getName());
	 
	 
	public record ArtWorkData(
			    String year,
			    String dimensions,
			    String technique,
			    String description) {
	}

	@JsonIgnore
	private final LanguageService languageService;

	@JsonIgnore
	private final ObjectStorageService objectStorageService;

	private final ChatClient chatClient;
	
	
	
	public OpenAIService(ChatClient.Builder builder,  ServerDBSettings settings, LanguageService languageService, ObjectStorageService objectStorageService) {
		super(settings);
		this.languageService = languageService;
		this.objectStorageService = objectStorageService;
		this.chatClient = builder.build();
	}

	 @PostConstruct
	 public void init() {
		 logger.info("OpenAIService initialized");
	 }
	 
	 
	 
	 public String generateGuide(GuideContent guideContent, ArtWork artWork, Site site, String language) {
		 

		 String siteName = site.getName();
		 String name = artWork.getName();
		 String artists = artWork.getArtists().stream().map(x -> x.getFirstLastname()).collect(Collectors.joining(", "));

		 Resource resource = artWork.getPhoto();
		 
		 
		 Map<String, String> map = Map.of(
				 "artWorkName", name,
				 "artists", artists,
				 "siteName", siteName,
				 "language", language
		 );

		
		 String prompt = "Generate the text of an audio guide of the artwork '${artWorkName}' by ${artists}, exhibited at ${siteName}: \n"+
			"An image of the artwork may be provided.\n"+
			"\n "+
			 "Requirements:\n "+
			 "- Language: ${language}\n "+
			 "- Use simple, precise language.\n"+
			 "- Describe relevant historic context and relevance of the art work." +
			 "- Briefly describe oncepts of the composition, use of light, use of colors and tones.\n "+
			 "- Historic context and relevance should be prioritized over visual description.\n "+
			 "- If the image is available, use it to enrich the description.\n "+
			 "- Do not include text like 'This is an audio guide...' or similar, just the content.\n "+
			 "- Do not include the name of the art work or the site where it is exhibited.\n "+
			 "- If the image cannot be analyzed, still produce a complete result based information or images available online.\n "+
			 "- Maximum length: no more than 280 words.";

		 // Replace macros in prompt using the map
		 for (Map.Entry<String, String> entry : map.entrySet()) {
			 prompt = prompt.replace("${" + entry.getKey() + "}", entry.getValue());
		 }

		 // Resolve the actual MIME type from the Resource metadata
		 MimeType mimeType = (resource.getMedia() != null)
				 ? MimeType.valueOf(resource.getMedia())
				 : MimeTypeUtils.IMAGE_JPEG;

		 try {

			 // Read the image bytes directly from object storage
			 byte[] imageBytes;
			 try (InputStream in = objectStorageService.getObject(resource.getBucketName(), resource.getObjectName())) {
				 imageBytes = in.readAllBytes();
			 }

			 logger.debug();
			 logger.debug("Sending image to OpenAI -> file: " + resource.getFileName()
					 + " | size: " + imageBytes.length + " bytes"
					 + " | mimeType: " + mimeType
					 + " | prompt: " + prompt);
			 logger.debug();
				
			 // Build a Media object with the raw image bytes for proper base64 inline encoding
			 Media imageMedia = Media.builder()
					 .mimeType(mimeType)
					 .data(imageBytes)
					 .name(resource.getFileName())
					 .build();

			 final String finalPrompt = prompt;

			 // Call OpenAI with the photo attached as media
			 String response = chatClient.prompt()

					 .options(OpenAiChatOptions.builder()
						        .model("gpt-5.4")
						        .maxCompletionTokens(1200)
						        .temperature(0.7)
						        .build())
					 .system("You are an expert in museum audio guides. Always provide helpful, descriptive, and safe outputs.")
					 .user(userSpec -> userSpec
							 .text(finalPrompt)
							 .media(imageMedia))
					 .call()
					 .content();

			 logger.debug();
			 logger.debug("Response from OpenAI for accessible content guide: " + name);
			 logger.debug(response);
			 logger.debug();
				
			 
			 return response;

		 } catch (IOException e) {
			 logger.error(e, "Error generating accessible content guide for: " + name, ServerConstant.NOT_THROWN);
			 return null;
		 }
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	 }

	 
	 public String generateAccesibleContentGuide(String sourceText, ArtWork artWork, Site site, String language) {

		 String source =sourceText;
	 

		 String siteName = site.getName();
		 String name = artWork.getName();
		 String artists = artWork.getArtists().stream().map(x -> x.getFirstLastname()).collect(Collectors.joining(", "));

		 Resource resource = artWork.getPhoto();

		 Map<String, String> map = Map.of(
				 "artWorkName", name,
				 "artists", artists,
				 "siteName", siteName,
				 "source", source,
				 "language", language
		 );

		
		 
		 		 
		 String prompt = "The following is the text of an audio guide of the artwork '${artWorkName}' by ${artists}, exhibited at ${siteName}: \n"+
		"\n"+
		"'${source}'\n"+
			" \n"+
			"An image of the artwork may be provided.\n"+
			"\n "+
			"Generate a new accessible audio guide text for people who are blind or have reduced vision. "+
			 "\n "+
			 "Requirements:\n "+
			 "- Language: ${language}\n "+
			 "- Use simple, precise, and sensory language.\n"+
			 "- Include a clear and vivid visual description of the artwork.\n "+
			 "- The new audio guide must be an extension of the source text, it must include the main concepts of the source text.\n "+
			 "- Describe the composition, use of light, use of colors and tones.\n "+
			 "- Prioritize the information from the original text.\n "+
			 "- If the image is available, use it to enrich the description.\n "+
			 "- Do not include text like 'This is an audio guide...' or similar, just the content.\n "+
			 "- Do not include the name of the art work or the site where it is exhibited.\n "+
			 "- If the image cannot be analyzed, still produce a complete result based only on the text.\n "+
			 "- This transformation is for accessibility purposes (assistive content for visually impaired users).\n" +
			 "- Maximum length: no more than 340 words and no more than 140% larger than the original.";

		 // Replace macros in prompt using the map
		 for (Map.Entry<String, String> entry : map.entrySet()) {
			 prompt = prompt.replace("${" + entry.getKey() + "}", entry.getValue());
		 }

		 // Resolve the actual MIME type from the Resource metadata
		 MimeType mimeType = (resource.getMedia() != null)
				 ? MimeType.valueOf(resource.getMedia())
				 : MimeTypeUtils.IMAGE_JPEG;

		 try {

			 // Read the image bytes directly from object storage
			 byte[] imageBytes;
			 try (InputStream in = objectStorageService.getObject(resource.getBucketName(), resource.getObjectName())) {
				 imageBytes = in.readAllBytes();
			 }

			 logger.debug();
			 logger.debug("Sending image to OpenAI -> file: " + resource.getFileName()
					 + " | size: " + imageBytes.length + " bytes"
					 + " | mimeType: " + mimeType
					 + " | prompt: " + prompt);
			 logger.debug();
				
			 // Build a Media object with the raw image bytes for proper base64 inline encoding
			 Media imageMedia = Media.builder()
					 .mimeType(mimeType)
					 .data(imageBytes)
					 .name(resource.getFileName())
					 .build();

			 final String finalPrompt = prompt;

			 // Call OpenAI with the photo attached as media
			 String response = chatClient.prompt()

					 .options(OpenAiChatOptions.builder()
						        .model("gpt-5.4")
						        .maxCompletionTokens(1200)
						        .temperature(0.7)
						        .build())
					 
					 .system("You are an expert in accessible museum audio guides for blind and visually impaired people. Always provide helpful, descriptive, and safe outputs.")
					  
					 .user(userSpec -> userSpec
							 .text(finalPrompt)
							 .media(imageMedia))
					 
					 .call()
					 .content();

			 
			 logger.debug();
			 logger.debug("Response from OpenAI for accessible content guide: " + name);
			 logger.debug(response);
			 logger.debug();
				
			 
			 return response;

		 } catch (IOException e) {
			 logger.error(e, "Error generating accessible content guide for: " + name, ServerConstant.NOT_THROWN);
			 return null;
		 }
	 }
	 
	 
	 public String generate (String message) {
	        return chatClient.prompt()
	                .user(message)
	                .call()
	                .content();
    }

	 public String getName() { 
		 return "Open AI";
	 }
	 
	 /**
	 private final static PromptTemplate PROMPT_ARTWORK_TEMPLATE = 
			 new PromptTemplate("generate a JSON return Write a {genre} haiku about {theme} following the traditional 5-7-5 syllable structure.");
	 
	 public ArtWorkData generateArtWorkData(String name, String artist, String language) {
		
		 OpenAiChatOptions options = OpenAiChatOptions.builder()
				    .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, myJsonSchema))
				    .build();

				ChatResponse response = chatClient.call(new Prompt("Your request...", options));
				
		/**
			 Prompt prompt = PROMPT_TEMPLATE
			   .create(Map.of(
			     "genre", genre,
			     "theme", theme));
			 return chatClient
			   .prompt(prompt)
			   .call()
			   .entity(Poem.class);
		 
		 *
		 *
		 *
		 *
		 *
		 *complete method getTypeAttributeValue 
it must identify the Attribute whose name starts with "tipo" and return a String with the values of that Attribute
		 *
		 *
		 *
		 *
		 *
		 *
		 *- 
		 
		 
 complete method generateAccesibleContentGuide
- download the Resource's photo to a temporary file, same way as ResourceMetadataCommand
- use the Map<String, String> map to replace the macros in the String prompt
- the method should call OpenAI service using Spring Open AI library 
- the prompt should include the photo as an attachment, same way as in the example of OpenAI documentation: https://docs.spring.io/spring-ai/docs/current/reference/html/#openai-chat-attachments

		 
		 *
		 **/
		 
		 
	 }
	 
 
			
			
			
	 
	
	 
	 
	 
	 
	 

 



/**


private final static PromptTemplate PROMPT_TEMPLATE
= new PromptTemplate("Write a {genre} haiku about {theme} following the traditional 5-7-5 syllable structure.");

Poem generate(String genre, String theme) {
Prompt prompt = PROMPT_TEMPLATE
  .create(Map.of(
    "genre", genre,
    "theme", theme));
return chatClient
  .prompt(prompt)
  .call()
  .entity(Poem.class);
}

*/
