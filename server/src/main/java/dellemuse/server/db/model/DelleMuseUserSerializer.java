package dellemuse.server.db.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DelleMuseUserSerializer extends StdSerializer<User> {
            
    private static final long serialVersionUID = 1L;

    public DelleMuseUserSerializer() {
        this(null);
    }
  
    public DelleMuseUserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("username", value.getUsername());
        jgen.writeEndObject();
    }
    

}
