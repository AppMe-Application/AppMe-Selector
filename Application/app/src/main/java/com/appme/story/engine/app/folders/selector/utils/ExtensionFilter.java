package com.appme.story.engine.app.folders.selector.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

import com.appme.story.engine.app.folders.selector.models.DialogConfigs;
import com.appme.story.engine.app.folders.selector.models.DialogProperties;

public class ExtensionFilter implements FileFilter {
    private final String[] validExtensions;
    private DialogProperties properties;

    public ExtensionFilter(DialogProperties properties) {
        if (properties.extensions != null) {
            this.validExtensions = properties.extensions;
        } else {
            this.validExtensions = new String[]{""};
        }
        this.properties = properties;
    }

    /**Function to filter files based on defined rules.
     */
    @Override
    public boolean accept(File file) {
        //All directories are added in the least that can be read by the Application
        if (file.isDirectory() && file.canRead()) {   return true;
        } else if (properties.selection_type == DialogConfigs.DIR_SELECT) {   /*  True for files, If the selection type is Directory type, ie.
             *  Only directory has to be selected from the list, then all files are
             *  ignored.
             */
            return false;
        } else {   /*  Check whether name of the file ends with the extension. Added if it
             *  does.
             */
            String name = file.getName().toLowerCase(Locale.getDefault());
            for (String ext : validExtensions) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    static String[][] sMediaTypes ={
        {"3gp","video/3gpp"},
        {"apk","application/vnd.android.package-archive"},
        {"asf","video/x-ms-asf"},
        {"avi","video/x-msvideo"},
        {"bin","application/octet-stream"},
        {"bmp","image/bmp"},
        {"c","text/plain"},
        {"class","application/octet-stream"},
        {"conf","text/plain"},
        {"cpp","text/plain"},
        {"doc","application/msword"},
        {"docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {"xls","application/vnd.ms-excel"},
        {"xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {"exe","application/octet-stream"},
        {"gif","image/gif"},
        {"gtar","application/x-gtar"},
        {"gz","application/x-gzip"},
        {"h","text/plain"},
        {"htm","text/html"},
        {"html","text/html"},
        {"jar","application/java-archive"},
        {"java","text/plain"},
        {"jpeg","image/jpeg"},
        {"jpg","image/jpeg"},
        {"js","application/x-JavaScript"},
        {"log","text/plain"},
        {"m3u","audio/x-mpegurl"},
        {"m4a","audio/mp4a-latm"},
        {"m4b","audio/mp4a-latm"},
        {"m4p","audio/mp4a-latm"},
        {"m4u","video/vnd.mpegurl"},
        {"m4v","video/x-m4v"},
        {"mov","video/quicktime"},
        {"mp2","audio/x-mpeg"},
        {"mp3","audio/x-mpeg"},
        {"mp4","video/mp4"},
        {"mpc","application/vnd.mpohun.certificate"},
        {"mpe","video/mpeg"},
        {"mpeg","video/mpeg"},
        {"mpg","video/mpeg"},
        {"mpg4","video/mp4"},
        {"mpga","audio/mpeg"},
        {"msg","application/vnd.ms-outlook"},
        {"ogg","audio/ogg"},
        {"pdf","application/pdf"},
        {"png","image/png"},
        {"pps","application/vnd.ms-powerpoint"},
        {"ppt","application/vnd.ms-powerpoint"},
        {"pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        {"prop","text/plain"},
        {"rc","text/plain"},
        {"rmvb","audio/x-pn-realaudio"},
        {"rtf","application/rtf"},
        {"sh","text/plain"},
        {"tar","application/x-tar"},
        {"tgz","application/x-compressed"},
        {"txt","text/plain"},
        {"wav","audio/x-wav"},
        {"wma","audio/x-ms-wma"},
        {"wmv","audio/x-ms-wmv"},
        {"wps","application/vnd.ms-works"},
        {"xml","text/plain"},
        {"z","application/x-compress"},
        {"zip","application/x-zip-compressed"},
        {"","*/*"}};

    /**
     * return the extension of the file
     * @param name name or path of the file
     * */
    public static String getExtension(String name) {
        int offset = name.lastIndexOf("/");//可能为路径,且路径中有"."
        String suffix = name.substring(offset + 1, name.length());
        offset = suffix.lastIndexOf(".");
        if (offset > 0) {
            return suffix.substring(offset + 1, suffix.length());
        }
        return "";
    }


}
