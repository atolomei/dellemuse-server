package dellemuse.serverapp.serverdb.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;

public class DelleMuseIdNameSerializer extends StdSerializer<DelleMuseObject> {


    private static final long serialVersionUID = 1L;

    public DelleMuseIdNameSerializer() {
        this(null);
    }
  
    public DelleMuseIdNameSerializer(Class<DelleMuseObject> t) {
        super(t);
    }

    @Override
    public void serialize(DelleMuseObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("name", value.getName());
        if (value.getDisplayname()!=null)
            jgen.writeStringField("displayname", value.getDisplayname());
        jgen.writeEndObject();
    }
    
    

}
