package dellemuse.serverapp.openai;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;

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
	final LanguageService languageService;

	private final ChatClient chatClient;
	
	public OpenAIService(ChatClient.Builder builder,  ServerDBSettings settings, LanguageService languageService) {
		super(settings);
		this.languageService = languageService;
		 this.chatClient = builder.build();
	}

	 @PostConstruct
	 public void init() {
		 logger.info("OpenAIService initialized");
	 }

	 public String generate (String message) {
	        return chatClient.prompt()
	                .user(message)
	                .call()
	                .content();
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
