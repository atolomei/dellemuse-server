package dellemuse.server.db.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DelleMuseIdSerializer extends StdSerializer<DelleMuseObject> {


    private static final long serialVersionUID = 1L;

    public DelleMuseIdSerializer() {
        this(null);
    }
  
    public DelleMuseIdSerializer(Class<DelleMuseObject> t) {
        super(t);
    }

    @Override
    public void serialize(DelleMuseObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        //jgen.writeStringField("name", value.getName());
        jgen.writeEndObject();
    }
    
    

}
