package dellemuse.util;

import org.springframework.http.MediaType;

public class MediaUtil {
    
    public static MediaType estimateContentType(String f_name) {

        if (f_name == null)
            return MediaType.valueOf("application/octet-stream");

        if (isPdf(f_name))
            return MediaType.valueOf("application/pdf");

        if (isAudio(f_name))
            return MediaType.valueOf("audio/mpeg");

        if (isVideo(f_name))
            return MediaType.valueOf("video/mpeg");

        if (isJpeg(f_name))
            return MediaType.valueOf("image/jpeg");

        if (isPng(f_name))
            return MediaType.valueOf("image/png");

        if (isGif(f_name))
            return MediaType.valueOf("image/gif");

        if (isWebp(f_name))
            return MediaType.valueOf("image/svg+xml");

        if (isExcel(f_name))
            return MediaType.valueOf("application/vnd.ms-excel");

        if (isWord(f_name))
            return MediaType.valueOf("application/msword");

        if (isPowerPoint(f_name))
            return MediaType.valueOf("application/vnd.ms-powerpoint");

        return MediaType.valueOf("application/octet-stream");
    }

    static public boolean isPowerPoint(String name) {
        return name.toLowerCase().matches("^.*\\.(ppt|pptx)$");
    }

    static public boolean isWord(String name) {
        return name.toLowerCase().matches("^.*\\.(doc|docx|rtf)$");
    }

    static public boolean isVideo(String filename) {
        return (filename.toLowerCase().matches("^.*\\.(mp4|flv|aac|ogg|wmv|3gp|avi|swf|svi|wtv|fla|mpeg|mpg|mov|m4v)$"));
    }

    static public boolean isAudio(String filename) {
        return filename.toLowerCase().matches("^.*\\.(mp3|wav|ogga|ogg|aac|m4a|m4a|aif|wma)$");
    }

    static public boolean isExcel(String name) {
        return name.toLowerCase().matches("^.*\\.(xls|xlsx|xlsm)$");
    }

    static public boolean isJpeg(String string) {
        return string.toLowerCase().matches("^.*\\.(jpg|jpeg)$");
    }

    static public boolean isPdf(String string) {
        return string.toLowerCase().matches("^.*\\.(pdf)$");
    }

    static public boolean isGif(String string) {
        return string.toLowerCase().matches("^.*\\.(gif)$");
    }

    static public boolean isWebp(String string) {
        return string.toLowerCase().matches("^.*\\.(webp)$");
    }

    static public boolean isPng(String string) {
        return string.toLowerCase().matches("^.*\\.(png)$");
    }

    static public boolean isGeneralImage(String string) {
        return string.toLowerCase().matches("^.*\\.(png|jpg|jpeg|gif|bmp|heic)$");
    }

    static public boolean isImage(String string) {
        return isGeneralImage(string) || string.toLowerCase().matches("^.*\\.(webp)$");
    }

}
