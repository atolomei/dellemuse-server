package dellemuse.server.db.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DelleMuseListIdSerializer extends StdSerializer<List<DelleMuseObject>> {

    private static final long serialVersionUID = 1L;

    public DelleMuseListIdSerializer() {
        this(null);
    }
  
    public DelleMuseListIdSerializer(Class<List<DelleMuseObject>> t) {
        super(t);
    }

    @Override
    public void serialize(List<DelleMuseObject> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (DelleMuseObject o: value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id",   o.getId());
            jgen.writeStringField("displayName", o.getDisplayName());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
    
    

}
