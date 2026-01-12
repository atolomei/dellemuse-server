package dellemuse.serverapp.serverdb.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;

public class DelleMuseArtExhibitionItemSerializer extends StdSerializer<ArtExhibitionItem> {

	private static final long serialVersionUID = 1L;

	public DelleMuseArtExhibitionItemSerializer() {
		this(null);
	}

	public DelleMuseArtExhibitionItemSerializer(Class<ArtExhibitionItem> t) {
		super(t);
	}

	@Override
	public void serialize(ArtExhibitionItem value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

		jgen.writeStartObject();

		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("name", value.getName());

		if (value.getArtExhibition() != null) {

			jgen.writeStartObject("artExhibition");
			jgen.writeStringField("id", value.getArtExhibition().getId().toString());
			jgen.writeStringField("name", value.getArtExhibition().getName().toString());

			jgen.writeStartObject("site");
			jgen.writeStringField("id", value.getArtExhibition().getSite().getId().toString());
			jgen.writeStringField("name", value.getArtExhibition().getSite().getName().toString());
			jgen.writeEndObject();

			jgen.writeEndObject();

		}
		if (value.getArtWork() != null) {

			jgen.writeStartObject("artwork");
			jgen.writeStringField("id", value.getArtWork().getId().toString());
			jgen.writeStringField("name", value.getArtWork().getName().toString());

			jgen.writeStartObject("site");
			jgen.writeStringField("id", value.getArtExhibition().getSite().getId().toString());
			jgen.writeStringField("name", value.getArtExhibition().getSite().getName().toString());
			jgen.writeEndObject();

			jgen.writeEndObject();

		}

		jgen.writeEndObject();
	}

}
