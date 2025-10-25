package dellemuse.serverapp.security.google;



import com.google.cloud.translate.*;
import java.io.IOException;



public class GoogleAuth {

	
	public void execute() {

		Translate translate = TranslateOptions.getDefaultInstance().getService();
	
		//Translation translation = translate.translate("¡Hola Mundo!");
		//System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());

		String src = "La iglesia de Nuestra Señora del Pilar es una basílica ubicada en el barrio de Recoleta en Buenos Aires; en su día formó parte del convento de Franciscanos recoletos. Su construcción, que concluyó en 1732, se debe al mecenas aragonés Juan de Narbona.  Desde el siglo XIX es una de las parroquias de la ciudad de Buenos Aires y el segundo templo más antiguo de la ciudad.";

		{
			Translation translation = translate.translate(src, 
					Translate.TranslateOption.sourceLanguage("es"),
					Translate.TranslateOption.targetLanguage("en"));
			System.out.printf("Translated Text es -> en:\n\t%s\n", translation.getTranslatedText());
			}
		
		
		{
		Translation translation = translate.translate(src, 
				Translate.TranslateOption.sourceLanguage("es"),
				Translate.TranslateOption.targetLanguage("pt-BR"));
		System.out.printf("Translated Text es -> pt-BR :\n\t%s\n", translation.getTranslatedText());
		}

	
	
	
	}
	
	
}
