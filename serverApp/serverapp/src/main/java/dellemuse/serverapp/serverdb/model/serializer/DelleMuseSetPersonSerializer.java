package dellemuse.serverapp.serverdb.model.serializer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Person;

public class DelleMuseSetPersonSerializer extends StdSerializer<Set<Person>> {

    private static final long serialVersionUID = 1L;

    public DelleMuseSetPersonSerializer() {
        this(null);
    }
  
    public DelleMuseSetPersonSerializer(Class<Set<Person>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Person> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        
        jgen.writeStartArray();
        for (Person o: value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", o.getId());            
            jgen.writeStringField("name", o.getName());
            jgen.writeStringField("lastname", o.getLastname());
            
            if (o.getUser()!=null) { 
                jgen.writeStartObject("user");
                    jgen.writeNumberField("id", o.getUser().getId());
                    jgen.writeStringField("name", o.getUser().getName());
                jgen.writeEndObject();
            }
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }

}
