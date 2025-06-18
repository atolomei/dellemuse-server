package dellemuse.server.db.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.server.db.model.DelleMuseObject;
import dellemuse.server.db.model.Resource;

public class DelleMuseResourceSerializer extends StdSerializer<Resource> {


    private static final long serialVersionUID = 1L;

    public DelleMuseResourceSerializer() {
        this(null);
    }
  
    public DelleMuseResourceSerializer(Class<Resource> t) {
        super(t);
    }

    @Override
    public void serialize(Resource value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();

        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("name", value.getName());
        
        if (value.getMedia()!=null)
            jgen.writeStringField("media", value.getMedia());
        
        if (value.getBucketName()!=null)
            jgen.writeStringField("bucketName", value.getBucketName());
        
        if (value.getObjectName()!=null)
            jgen.writeStringField("objectName", value.getObjectName());
        
        jgen.writeEndObject();
    }
    
    

}
