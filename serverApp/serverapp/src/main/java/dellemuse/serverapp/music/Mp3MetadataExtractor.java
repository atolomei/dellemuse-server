package dellemuse.serverapp.music;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;


public class Mp3MetadataExtractor {

	
	 public static Map<String, String> extractMetadata(File mp3File) {
	        Map<String, String> metadata = new HashMap<>();

	        try {
	            AudioFile audioFile = AudioFileIO.read(mp3File);
	            Tag tag = audioFile.getTag();

	            if (tag == null) {
	                return metadata;
	            }

	            putIfPresent(metadata, "title", tag.getFirst(FieldKey.TITLE));
	            putIfPresent(metadata, "artist", tag.getFirst(FieldKey.ARTIST));
	            putIfPresent(metadata, "album", tag.getFirst(FieldKey.ALBUM));
	            putIfPresent(metadata, "composer", tag.getFirst(FieldKey.COMPOSER));
	            putIfPresent(metadata, "genre", tag.getFirst(FieldKey.GENRE));
	            putIfPresent(metadata, "year", tag.getFirst(FieldKey.YEAR));
	            putIfPresent(metadata, "track", tag.getFirst(FieldKey.TRACK));
	            putIfPresent(metadata, "comment", tag.getFirst(FieldKey.COMMENT));
	            putIfPresent(metadata, "albumArtist", tag.getFirst(FieldKey.ALBUM_ARTIST));
	            putIfPresent(metadata, "lyrics", tag.getFirst(FieldKey.LYRICS));
	            putIfPresent(metadata, "record_label", tag.getFirst(FieldKey.RECORD_LABEL));
	            putIfPresent(metadata, "disc_no", tag.getFirst(FieldKey.DISC_NO));
	            
	        } catch (Exception e) {
	            throw new RuntimeException("Error reading MP3 metadata", e);
	        }

	        return metadata;
	    }

	    private static void putIfPresent(Map<String, String> map, String key, String value) {
	        if (value != null && !value.isBlank()) {
	            map.put(key, value);
	        }
	    }

	    
}
