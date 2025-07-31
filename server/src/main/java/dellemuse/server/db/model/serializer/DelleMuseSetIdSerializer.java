package dellemuse.server.db.model.serializer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.server.db.model.DelleMuseObject;

public class DelleMuseSetIdSerializer extends StdSerializer<Set<DelleMuseObject>> {


    private static final long serialVersionUID = 1L;

    public DelleMuseSetIdSerializer() {
        this(null);
    }
  
    public DelleMuseSetIdSerializer(Class<Set<DelleMuseObject>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<DelleMuseObject> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (DelleMuseObject o: value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", o.getId());            
            jgen.writeEndObject();
        }
        //jgen.writeStringField("name", value.getName());
        jgen.writeEndArray();
    }
    
    

}
