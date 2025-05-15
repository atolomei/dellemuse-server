package dellemuse.server.db.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DelleMuseSetIdNameSerializer extends StdSerializer<Set<DelleMuseObject>> {

    private static final long serialVersionUID = 1L;

    public DelleMuseSetIdNameSerializer() {
        this(null);
    }
  
    public DelleMuseSetIdNameSerializer(Class<Set<DelleMuseObject>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<DelleMuseObject> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (DelleMuseObject o: value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", o.getId());            
            jgen.writeStringField("name", o.getName());
            jgen.writeEndObject();
        }
        //jgen.writeStringField("name", value.getName());
        jgen.writeEndArray();
    }
    
    

}
