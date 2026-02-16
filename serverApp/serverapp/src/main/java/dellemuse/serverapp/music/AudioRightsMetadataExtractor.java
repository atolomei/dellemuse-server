package dellemuse.serverapp.music;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class AudioRightsMetadataExtractor {

    public static Map<String, String> extract(File file) {
        Map<String, String> result = new HashMap<>();

        try {
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();

            try (FileInputStream stream = new FileInputStream(file)) {
                parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
            }

            // --- common descriptive fields
            copy(metadata, result, "title", "title");
            copy(metadata, result, "artist", "creator");
            copy(metadata, result, "album", "xmpDM:album");
            copy(metadata, result, "composer", "xmpDM:composer");

            // --- copyright / license / rights (important)
            copy(metadata, result, "copyright", "dc:rights");
            copy(metadata, result, "copyright", "copyright");
            copy(metadata, result, "copyright", "xmpDM:copyright");

            copy(metadata, result, "licenseUrl", "xmpRights:WebStatement");
            copy(metadata, result, "usageTerms", "xmpRights:UsageTerms");
            copy(metadata, result, "rightsOwner", "xmpRights:Owner");
            copy(metadata, result, "publisher", "dc:publisher");

            // --- audio technical info
            copy(metadata, result, "duration", "xmpDM:duration");
            copy(metadata, result, "audioFormat", "Content-Type");

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract metadata", e);
        }

        return result;
    }

    private static void copy(Metadata src, Map<String,String> dst,
                             String targetKey, String metadataKey) {

        String value = src.get(metadataKey);
        if (value != null && !value.isBlank()) {
            dst.putIfAbsent(targetKey, value);
        }
    }
}
