package com.appme.story.engine.app.folders.preview;

import android.webkit.MimeTypeMap;

import com.appme.story.R;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

public final class MimeTypes {
    public static final String[] MIME_APK = {
        "apk"
    };
    public static final String[] MIME_TEXT = {
        "ajx", "am", "asa", "asc", "asp", "aspx", "awk", "bat", "c", "cdf", "cf", "cfg", "cfm", "cgi", "cnf", "conf", "cpp", "css", "csv", "ctl", "dat", "dhtml", "diz", "file", "forward", "grp", "h", "hpp", "hqx", "hta", "htaccess", "htc", "htm", "html", "htpasswd", "htt", "htx", "in", "inc", "info", "ini", "ink", "java", "js", "jsp", "key", "log", "logfile", "m3u", "m4", "m4a", "mak", "map", "model", "msg", "nfo", "nsi", "info", "old", "pas", "patch", "perl", "php", "php2", "php3", "php4", "php5", "php6", "phtml", "pix", "pl", "pm", "po", "pwd", "py", "qmail", "rb", "rbl", "rbw", "readme", "reg", "rss", "rtf", "ruby", "session", "setup", "sh", "shtm", "shtml", "sql", "ssh", "stm", "style", "svg", "tcl", "text", "threads", "tmpl", "tpl", "txt", "ubb", "vbs", "xhtml", "xml", "xrc", "xsl"
    };
    public static final String[] MIME_CODE = {
        "xml", "php", "asp", "json", "wiki"
    };
    public static final String[] MIME_HTML = {
        "htm"
    };
    public static final String[] MIME_PICTURE = {
        "png", "jpeg", "jpg", "ico", "gif", "bmp", "tiff"
    };
    public static final String[] MIME_MUSIC = {
        "mp3", "avi", "flac", "mpga"
    };
    public static final String[] MIME_VIDEO = {
        "mp4", "mkv", "wmw"
    };
    public static final String[] MIME_ARCHIVE = {
        "zip", "tar", "gz", "bz2", "rar", "7z"
    };
    private static final HashMap<String, Integer> EXT_ICONS = new HashMap<>();
    private static final HashMap<String, String> MIME_TYPES = new HashMap<>();

    static {
        // BINARY
        EXT_ICONS.put("a", R.drawable.ic_file_question);
        EXT_ICONS.put("bin", R.drawable.ic_file_question);
        EXT_ICONS.put("class", R.drawable.ic_file_question);
        EXT_ICONS.put("com", R.drawable.ic_file_question);
        EXT_ICONS.put("dex", R.drawable.ic_file_question);
        EXT_ICONS.put("dump", R.drawable.ic_file_question);
        EXT_ICONS.put("exe", R.drawable.ic_file_question);
        EXT_ICONS.put("dat", R.drawable.ic_file_question);
        EXT_ICONS.put("dll", R.drawable.ic_file_question);
        EXT_ICONS.put("lib", R.drawable.ic_file_question);
        EXT_ICONS.put("o", R.drawable.ic_file_question);
        EXT_ICONS.put("obj", R.drawable.ic_file_question);
        EXT_ICONS.put("pyc", R.drawable.ic_file_question);
        EXT_ICONS.put("pyo", R.drawable.ic_file_question);
        EXT_ICONS.put("ser", R.drawable.ic_file_question);
        EXT_ICONS.put("swf", R.drawable.ic_file_question);
        EXT_ICONS.put("so", R.drawable.ic_file_question);

        // Shell
        EXT_ICONS.put("bar", R.drawable.ic_file_question);
        EXT_ICONS.put("csh", R.drawable.ic_file_text);
        EXT_ICONS.put("ksh", R.drawable.ic_file_text);
        EXT_ICONS.put("sh", R.drawable.ic_file_text);

        // TEXT
        EXT_ICONS.put("csv", R.drawable.ic_file_text);
        EXT_ICONS.put("diff", R.drawable.ic_file_text);
        EXT_ICONS.put("in", R.drawable.ic_file_text);
        EXT_ICONS.put("list", R.drawable.ic_file_text);
        EXT_ICONS.put("log", R.drawable.ic_file_text);
        EXT_ICONS.put("rc", R.drawable.ic_file_text);
        EXT_ICONS.put("text", R.drawable.ic_file_text);
        EXT_ICONS.put("txt", R.drawable.ic_file_text);
        EXT_ICONS.put("tsv", R.drawable.ic_file_text);
        EXT_ICONS.put("md", R.drawable.ic_file_text);
        // Properties
        EXT_ICONS.put("properties", R.drawable.ic_file_settings);
        EXT_ICONS.put("conf", R.drawable.ic_file_settings);
        EXT_ICONS.put("config", R.drawable.ic_file_settings);
        EXT_ICONS.put("prop", R.drawable.ic_file_settings);

        // HTML
        EXT_ICONS.put("htm", R.drawable.ic_file_code);
        EXT_ICONS.put("html", R.drawable.ic_file_code);
        EXT_ICONS.put("mhtml", R.drawable.ic_file_code);
        EXT_ICONS.put("xhtml", R.drawable.ic_file_code);

        // XML
        EXT_ICONS.put("xml", R.drawable.ic_file_code);
        EXT_ICONS.put("mxml", R.drawable.ic_file_code);

        // DOCUMENT
        EXT_ICONS.put("doc", R.drawable.ic_file_word);
        EXT_ICONS.put("docx", R.drawable.ic_file_text);
        EXT_ICONS.put("odp", R.drawable.ic_file_text);
        EXT_ICONS.put("odt", R.drawable.ic_file_text);
        EXT_ICONS.put("rtf", R.drawable.ic_file_text);
        EXT_ICONS.put("ods", R.drawable.ic_file_text);
        EXT_ICONS.put("xls", R.drawable.ic_file_text);
        EXT_ICONS.put("xlsx", R.drawable.ic_file_text);

        // Presentation
        EXT_ICONS.put("ppt", R.drawable.ic_file_text);
        EXT_ICONS.put("pptx", R.drawable.ic_file_text);

        // PDF
        EXT_ICONS.put("pdf", R.drawable.ic_file_pdf);
        EXT_ICONS.put("fdf", R.drawable.ic_file_pdf);
        EXT_ICONS.put("ldwf", R.drawable.ic_file_pdf);

        // Package
        EXT_ICONS.put("ace", R.drawable.ic_file_archive);
        EXT_ICONS.put("bz", R.drawable.ic_file_archive);
        EXT_ICONS.put("bz2", R.drawable.ic_file_archive);
        EXT_ICONS.put("cab", R.drawable.ic_file_archive);
        EXT_ICONS.put("cpio", R.drawable.ic_file_archive);
        EXT_ICONS.put("gz", R.drawable.ic_file_archive);
        EXT_ICONS.put("lha", R.drawable.ic_file_archive);
        EXT_ICONS.put("lrf", R.drawable.ic_file_archive);
        EXT_ICONS.put("lzma", R.drawable.ic_file_archive);
        EXT_ICONS.put("rar", R.drawable.ic_file_archive);
        EXT_ICONS.put("tar", R.drawable.ic_file_archive);
        EXT_ICONS.put("tgz", R.drawable.ic_file_archive);
        EXT_ICONS.put("xz", R.drawable.ic_file_archive);
        EXT_ICONS.put("zip", R.drawable.ic_file_archive);
        EXT_ICONS.put("Z", R.drawable.ic_file_archive);
        EXT_ICONS.put("7z", R.drawable.ic_file_archive);
        EXT_ICONS.put("rar", R.drawable.ic_file_archive);
        EXT_ICONS.put("tar", R.drawable.ic_file_archive);
        EXT_ICONS.put("jar", R.drawable.ic_file_archive);

        // Image
        EXT_ICONS.put("bmp", R.drawable.ic_file_image);
        EXT_ICONS.put("cgm", R.drawable.ic_file_image);
        EXT_ICONS.put("g3", R.drawable.ic_file_image);
        EXT_ICONS.put("gif", R.drawable.ic_file_image);
        EXT_ICONS.put("ief", R.drawable.ic_file_image);
        EXT_ICONS.put("jpe", R.drawable.ic_file_image);
        EXT_ICONS.put("jpeg", R.drawable.ic_file_image);
        EXT_ICONS.put("jpg", R.drawable.ic_file_image);
        EXT_ICONS.put("png", R.drawable.ic_file_image);
        EXT_ICONS.put("btif", R.drawable.ic_file_image);
        EXT_ICONS.put("svg", R.drawable.ic_file_image);
        EXT_ICONS.put("svgz", R.drawable.ic_file_image);
        EXT_ICONS.put("tif", R.drawable.ic_file_image);
        EXT_ICONS.put("tiff", R.drawable.ic_file_image);
        EXT_ICONS.put("psd", R.drawable.ic_file_image);
        EXT_ICONS.put("dwg", R.drawable.ic_file_image);
        EXT_ICONS.put("dxf", R.drawable.ic_file_image);
        EXT_ICONS.put("fbs", R.drawable.ic_file_image);
        EXT_ICONS.put("fpx", R.drawable.ic_file_image);
        EXT_ICONS.put("fst", R.drawable.ic_file_image);
        EXT_ICONS.put("mmr", R.drawable.ic_file_image);
        EXT_ICONS.put("rlc", R.drawable.ic_file_image);
        EXT_ICONS.put("mdi", R.drawable.ic_file_image);
        EXT_ICONS.put("npx", R.drawable.ic_file_image);
        EXT_ICONS.put("wbmp", R.drawable.ic_file_image);
        EXT_ICONS.put("xif", R.drawable.ic_file_image);
        EXT_ICONS.put("ras", R.drawable.ic_file_image);
        EXT_ICONS.put("ico", R.drawable.ic_file_image);
        EXT_ICONS.put("pcx", R.drawable.ic_file_image);
        EXT_ICONS.put("pct", R.drawable.ic_file_image);
        EXT_ICONS.put("pic", R.drawable.ic_file_image);
        EXT_ICONS.put("xbm", R.drawable.ic_file_image);
        EXT_ICONS.put("xwd", R.drawable.ic_file_image);
        EXT_ICONS.put("bpg", R.drawable.ic_file_image);

        // Audio
        EXT_ICONS.put("aac", R.drawable.ic_file_music);
        EXT_ICONS.put("adp", R.drawable.ic_file_music);
        EXT_ICONS.put("aif", R.drawable.ic_file_music);
        EXT_ICONS.put("aifc", R.drawable.ic_file_music);
        EXT_ICONS.put("aiff", R.drawable.ic_file_music);
        EXT_ICONS.put("amr", R.drawable.ic_file_music);
        EXT_ICONS.put("ape", R.drawable.ic_file_music);
        EXT_ICONS.put("au", R.drawable.ic_file_music);
        EXT_ICONS.put("dts", R.drawable.ic_file_music);
        EXT_ICONS.put("eol", R.drawable.ic_file_music);
        EXT_ICONS.put("flac", R.drawable.ic_file_music);
        EXT_ICONS.put("kar", R.drawable.ic_file_music);
        EXT_ICONS.put("lvp", R.drawable.ic_file_music);
        EXT_ICONS.put("m2a", R.drawable.ic_file_music);
        EXT_ICONS.put("m3a", R.drawable.ic_file_music);
        EXT_ICONS.put("m3u", R.drawable.ic_file_music);
        EXT_ICONS.put("m4a", R.drawable.ic_file_music);
        EXT_ICONS.put("mid", R.drawable.ic_file_music);
        EXT_ICONS.put("mid", R.drawable.ic_file_music);
        EXT_ICONS.put("mka", R.drawable.ic_file_music);
        EXT_ICONS.put("mp2", R.drawable.ic_file_music);
        EXT_ICONS.put("mp3", R.drawable.ic_file_music);
        EXT_ICONS.put("mpga", R.drawable.ic_file_music);
        EXT_ICONS.put("oga", R.drawable.ic_file_music);
        EXT_ICONS.put("ogg", R.drawable.ic_file_music);
        EXT_ICONS.put("pya", R.drawable.ic_file_music);
        EXT_ICONS.put("ram", R.drawable.ic_file_music);
        EXT_ICONS.put("rmi", R.drawable.ic_file_music);
        EXT_ICONS.put("snd", R.drawable.ic_file_music);
        EXT_ICONS.put("spx", R.drawable.ic_file_music);
        EXT_ICONS.put("wav", R.drawable.ic_file_music);
        EXT_ICONS.put("wax", R.drawable.ic_file_music);
        EXT_ICONS.put("wma", R.drawable.ic_file_music);
        EXT_ICONS.put("xmf", R.drawable.ic_file_music);

        // Video
        EXT_ICONS.put("3gp", R.drawable.ic_file_video);
        EXT_ICONS.put("3gpp", R.drawable.ic_file_video);
        EXT_ICONS.put("3g2", R.drawable.ic_file_video);
        EXT_ICONS.put("3gpp2", R.drawable.ic_file_video);
        EXT_ICONS.put("h261", R.drawable.ic_file_video);
        EXT_ICONS.put("h263", R.drawable.ic_file_video);
        EXT_ICONS.put("h264", R.drawable.ic_file_video);
        EXT_ICONS.put("jpgv", R.drawable.ic_file_video);
        EXT_ICONS.put("jpgm", R.drawable.ic_file_video);
        EXT_ICONS.put("jpm", R.drawable.ic_file_video);
        EXT_ICONS.put("mj2", R.drawable.ic_file_video);
        EXT_ICONS.put("mp4", R.drawable.ic_file_video);
        EXT_ICONS.put("mp4v", R.drawable.ic_file_video);
        EXT_ICONS.put("mpg4", R.drawable.ic_file_video);
        EXT_ICONS.put("m1v", R.drawable.ic_file_video);
        EXT_ICONS.put("m2v", R.drawable.ic_file_video);
        EXT_ICONS.put("mpa", R.drawable.ic_file_video);
        EXT_ICONS.put("mpe", R.drawable.ic_file_video);
        EXT_ICONS.put("mpg", R.drawable.ic_file_video);
        EXT_ICONS.put("mpeg", R.drawable.ic_file_video);
        EXT_ICONS.put("ogv", R.drawable.ic_file_video);
        EXT_ICONS.put("mov", R.drawable.ic_file_video);
        EXT_ICONS.put("qt", R.drawable.ic_file_video);
        EXT_ICONS.put("fvt", R.drawable.ic_file_video);
        EXT_ICONS.put("m4u", R.drawable.ic_file_video);
        EXT_ICONS.put("pyv", R.drawable.ic_file_video);
        EXT_ICONS.put("viv", R.drawable.ic_file_video);
        EXT_ICONS.put("f4v", R.drawable.ic_file_video);
        EXT_ICONS.put("fli", R.drawable.ic_file_video);
        EXT_ICONS.put("flv", R.drawable.ic_file_video);
        EXT_ICONS.put("m4v", R.drawable.ic_file_video);
        EXT_ICONS.put("asf", R.drawable.ic_file_video);
        EXT_ICONS.put("asx", R.drawable.ic_file_video);
        EXT_ICONS.put("avi", R.drawable.ic_file_video);
        EXT_ICONS.put("wmv", R.drawable.ic_file_video);
        EXT_ICONS.put("wmx", R.drawable.ic_file_video);
        EXT_ICONS.put("mkv", R.drawable.ic_file_video);
        EXT_ICONS.put("divx", R.drawable.ic_file_video);

        // Application
        EXT_ICONS.put("apk", R.drawable.ic_file_apk);

        /*
         * ================= MIME TYPES ====================
         */
        MIME_TYPES.put("asm", "text/x-asm");
        MIME_TYPES.put("def", "text/plain");
        MIME_TYPES.put("in", "text/plain");
        MIME_TYPES.put("rc", "text/plain");
        MIME_TYPES.put("list", "text/plain");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("pl", "text/plain");
        MIME_TYPES.put("prop", "text/plain");
        MIME_TYPES.put("properties", "text/plain");
        MIME_TYPES.put("rc", "text/plain");

        MIME_TYPES.put("epub", "application/epub+zip");
        MIME_TYPES.put("ibooks", "application/x-ibooks+zip");

        MIME_TYPES.put("ifb", "text/calendar");
        MIME_TYPES.put("eml", "message/rfc822");
        MIME_TYPES.put("msg", "application/vnd.ms-outlook");

        MIME_TYPES.put("ace", "application/x-ace-compressed");
        MIME_TYPES.put("bz", "application/x-bzip");
        MIME_TYPES.put("bz2", "application/x-bzip2");
        MIME_TYPES.put("cab", "application/vnd.ms-cab-compressed");
        MIME_TYPES.put("gz", "application/x-gzip");
        MIME_TYPES.put("lrf", "application/octet-stream");
        MIME_TYPES.put("jar", "application/java-archive");
        MIME_TYPES.put("xz", "application/x-xz");
        MIME_TYPES.put("Z", "application/x-compress");

        MIME_TYPES.put("bat", "application/x-msdownload");
        MIME_TYPES.put("ksh", "text/plain");
        MIME_TYPES.put("sh", "application/x-sh");

        MIME_TYPES.put("db", "application/octet-stream");
        MIME_TYPES.put("db3", "application/octet-stream");

        MIME_TYPES.put("otf", "x-font-otf");
        MIME_TYPES.put("ttf", "x-font-ttf");
        MIME_TYPES.put("psf", "x-font-linux-psf");

        MIME_TYPES.put("cgm", "image/cgm");
        MIME_TYPES.put("btif", "image/prs.btif");
        MIME_TYPES.put("dwg", "image/vnd.dwg");
        MIME_TYPES.put("dxf", "image/vnd.dxf");
        MIME_TYPES.put("fbs", "image/vnd.fastbidsheet");
        MIME_TYPES.put("fpx", "image/vnd.fpx");
        MIME_TYPES.put("fst", "image/vnd.fst");
        MIME_TYPES.put("mdi", "image/vnd.ms-mdi");
        MIME_TYPES.put("npx", "image/vnd.net-fpx");
        MIME_TYPES.put("xif", "image/vnd.xiff");
        MIME_TYPES.put("pct", "image/x-pict");
        MIME_TYPES.put("pic", "image/x-pict");

        MIME_TYPES.put("adp", "audio/adpcm");
        MIME_TYPES.put("au", "audio/basic");
        MIME_TYPES.put("snd", "audio/basic");
        MIME_TYPES.put("m2a", "audio/mpeg");
        MIME_TYPES.put("m3a", "audio/mpeg");
        MIME_TYPES.put("oga", "audio/ogg");
        MIME_TYPES.put("spx", "audio/ogg");
        MIME_TYPES.put("aac", "audio/x-aac");
        MIME_TYPES.put("mka", "audio/x-matroska");

        MIME_TYPES.put("jpgv", "video/jpeg");
        MIME_TYPES.put("jpgm", "video/jpm");
        MIME_TYPES.put("jpm", "video/jpm");
        MIME_TYPES.put("mj2", "video/mj2");
        MIME_TYPES.put("mjp2", "video/mj2");
        MIME_TYPES.put("mpa", "video/mpeg");
        MIME_TYPES.put("ogv", "video/ogg");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("mkv", "video/x-matroska");
    }

    public static int getIconForExt(String ext) {
        final Integer res = EXT_ICONS.get(ext);
        return res == null ? 0 : res;
    }

    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return null;
        }

        String type = null;
        final String extension = FilenameUtils.getExtension(file.getName());

        if (extension != null && !extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
            if (type == null) {
                type = MIME_TYPES.get(extensionLowerCase);
            }
        }

        if(type == null)
            type="*/*";
        return type;
    }

    private static boolean mimeTypeMatch(String mime, String input) {
        return Pattern.matches(mime.replace("*", ".*"), input);
    }

    public static boolean isPicture(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("image/*", mime);
    }

    public static boolean isVideo(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("video/*", mime);
    }
}

