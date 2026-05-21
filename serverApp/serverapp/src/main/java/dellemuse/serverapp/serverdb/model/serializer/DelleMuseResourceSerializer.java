package dellemuse.serverapp.serverdb.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dellemuse.serverapp.serverdb.model.Resource;

public class DelleMuseResourceSerializer extends StdSerializer<Resource> {

    private static final long serialVersionUID = 1L;

    public DelleMuseResourceSerializer() {
        super(Resource.class);
    }

    @Override
    public void serialize(Resource value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", (long) value.getId());
        jgen.writeStringField("name", value.getName());
        if (value.getMedia() != null)
            jgen.writeStringField("media", value.getMedia());
        if (value.getBucketName() != null)
            jgen.writeStringField("bucketName", value.getBucketName());
        if (value.getObjectName() != null)
            jgen.writeStringField("objectName", value.getObjectName());
        jgen.writeNumberField("size", value.getSize());
        jgen.writeStringField("usethumbnail", Boolean.valueOf(value.isUsethumbnail()).toString());
        jgen.writeEndObject();
    }
}