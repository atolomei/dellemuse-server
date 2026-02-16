package dellemuse.serverapp.music;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class AudioFileMetadataExtractor {

    public static Map<String, String> extractMetadata(File audioFile) {
        Map<String, String> metadata = new HashMap<>();

        try {
            AudioFile file = AudioFileIO.read(audioFile);

            // --- audio technical info (always available)
            if (file.getAudioHeader() != null) {
                metadata.put("bitrate", file.getAudioHeader().getBitRate());
                metadata.put("sampleRate", file.getAudioHeader().getSampleRate());
                metadata.put("durationSeconds",
                        String.valueOf(file.getAudioHeader().getTrackLength()));
                metadata.put("format", file.getAudioHeader().getFormat());
            }

            // --- tag info (may not exist)
            Tag tag = file.getTag();

            if (tag != null) {
                put(metadata, "title", tag.getFirst(FieldKey.TITLE));
                put(metadata, "artist", tag.getFirst(FieldKey.ARTIST));
                put(metadata, "album", tag.getFirst(FieldKey.ALBUM));
                put(metadata, "composer", tag.getFirst(FieldKey.COMPOSER));
                put(metadata, "genre", tag.getFirst(FieldKey.GENRE));
                put(metadata, "year", tag.getFirst(FieldKey.YEAR));
                put(metadata, "track", tag.getFirst(FieldKey.TRACK));
                put(metadata, "comment", tag.getFirst(FieldKey.COMMENT));
            }

        } catch (CannotReadException e) {
            // OGG without metadata or unsupported variant
            metadata.put("warning", "No readable metadata block found");

        } catch (Exception e) {
            throw new RuntimeException("Error reading audio file", e);
        }

        return metadata;
    }

    private static void put(Map<String,String> map, String key, String value) {
        if (value != null && !value.isBlank()) {
            map.put(key, value);
        }
    }
}
