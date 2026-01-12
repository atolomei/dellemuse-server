package dellemuse.serverapp.serverdb.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverapp.serverdb.model.GuideContent;

@Deprecated
public class DelleMuseGuideContentSerializer extends StdSerializer<GuideContent> {

	private static final long serialVersionUID = 1L;

	public DelleMuseGuideContentSerializer() {
		this(null);
	}

	public DelleMuseGuideContentSerializer(Class<GuideContent> t) {
		super(t);
	}

	@Override
	public void serialize(GuideContent value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartObject();

		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("name", value.getName());

		if (value.getArtExhibitionItem() != null) {
			jgen.writeStartObject("artExhibitionItem");
				jgen.writeStringField("id", value.getArtExhibitionItem().getId().toString());
				jgen.writeStringField("name", value.getArtExhibitionItem().getName().toString());
				jgen.writeStartObject("site");
					jgen.writeStringField("id", value.getArtExhibitionItem().getArtExhibition().getSite().getId().toString());
					jgen.writeStringField("name", value.getArtExhibitionGuide().getArtExhibition().getSite().getName().toString());
				jgen.writeEndObject();
			jgen.writeEndObject();
		}

		if (value.getArtExhibitionGuide() != null) {
			jgen.writeStartObject("artExhibitionGuide");
				jgen.writeStringField("id", value.getArtExhibitionGuide().getId().toString());
				jgen.writeStringField("name", value.getArtExhibitionGuide().getName().toString());
			jgen.writeEndObject();
		}
		
		jgen.writeNumberField("guideOrder", value.getGuideOrder());

		if (value.getPhoto() != null) {
			jgen.writeStartObject("photo");
				jgen.writeNumberField("id", value.getPhoto().getId());
			jgen.writeEndObject();
		}
		jgen.writeEndObject();
	}

}
