package dellemuse.serverdb.model.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverdb.model.DelleMuseObject;

public class DelleMuseListIdNameSerializer extends StdSerializer<List<DelleMuseObject>> {

    private static final long serialVersionUID = 1L;

    public DelleMuseListIdNameSerializer() {
        this(null);
    }
  
    public DelleMuseListIdNameSerializer(Class<List<DelleMuseObject>> t) {
        super(t);
    }

    @Override
    public void serialize(List<DelleMuseObject> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (DelleMuseObject o: value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", o.getId());         
            jgen.writeStringField("name", o.getName());
            if (o.getTitle()!=null)
                jgen.writeStringField("title", o.getTitle());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}
