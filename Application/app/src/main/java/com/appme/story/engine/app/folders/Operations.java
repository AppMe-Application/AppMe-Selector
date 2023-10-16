package com.appme.story.engine.app.folders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

public class Operations {

    public static final String TAG = "Operations";

    // reserved characters by OS, shall not be allowed in file names
    private static final String FOREWARD_SLASH = "/";
    private static final String BACKWARD_SLASH = "\\";
    private static final String COLON = ":";
    private static final String ASTERISK = "*";
    private static final String QUESTION_MARK = "?";
    private static final String QUOTE = "\"";
    private static final String GREATER_THAN = ">";
    private static final String LESS_THAN = "<";

    private static final String FAT = "FAT";

    public interface ErrorCallBack {

        /**
         * Callback fired when file being created in process already exists
         *
         * @param file
         */
        void exists(File file);

        /**
         * Callback fired when creating new file/directory and required storage access framework permission
         * to access SD Card is not available
         *
         * @param file
         */
        void launchSAF(File file);    
        /**
         * Callback fired when we're done processing the operation
         *
         * @param hFile
         * @param b     defines whether operation was successful
         */
        void done(File hFile, boolean b);

        /**
         * Callback fired when an invalid file name is found.
         *
         * @param file
         */
        void invalidName(File file);
    }

    public static void mkdir(final Context context,  @NonNull final File file, 
                             @NonNull final ErrorCallBack errorCallBack) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {
                // checking whether filename is valid or a recursive call possible
                if (isNewDirectoryRecursive(file) || !Operations.isFileNameValid(file.getName())) {
                    errorCallBack.invalidName(file);
                    return null;
                }

                if (file.exists()) {
                    errorCallBack.exists(file);
                    return null;
                }
                if (!file.isDirectory()) {
                    int mode = checkFolder(new File(file.getParent()), context);
                    if (mode == 2) {
                        errorCallBack.launchSAF(file);
                        return null;
                    }
                    if (mode == 1 || mode == 0) {
                        boolean isDone = FileUtil.mkdir(file, context);
                        errorCallBack.done(file, isDone);
                        return null;
                    }
                }

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private static int checkFolder(final File folder, Context context) {
        boolean lol = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (lol) {

            boolean ext = FileUtil.isOnExtSdCard(folder, context);
            if (ext) {

                if (!folder.exists() || !folder.isDirectory()) {
                    return 0;
                }

                // On Android 5, trigger storage access framework.
                if (!FileUtil.isWritableNormalOrSaf(folder, context)) {
                    return 2;
                }
                return 1;
            }
        } else if (Build.VERSION.SDK_INT == 19) {
            // Assume that Kitkat workaround works
            if (FileUtil.isOnExtSdCard(folder, context)) return 1;

        }

        // file not on external sd card
        if (FileUtil.isWritable(new File(folder, "DummyFile"))) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * Validates file name
     * special reserved characters shall not be allowed in the file names on FAT filesystems
     *
     * @param fileName the filename, not the full path!
     * @return boolean if the file name is valid or invalid
     */
    public static boolean isFileNameValid(String fileName) {
        //String fileName = builder.substring(builder.lastIndexOf("/")+1, builder.length());

        // TODO: check file name validation only for FAT filesystems
        return !(fileName.contains(ASTERISK) || fileName.contains(BACKWARD_SLASH) ||
            fileName.contains(COLON) || fileName.contains(FOREWARD_SLASH) ||
            fileName.contains(GREATER_THAN) || fileName.contains(LESS_THAN) ||
            fileName.contains(QUESTION_MARK) || fileName.contains(QUOTE));
    }

    /**
     * @deprecated use {@link #getName(Context)}
     * @return
     */
    public static String getName(String path) {
        StringBuilder builder = new StringBuilder(path);
        String name = builder.substring(builder.lastIndexOf("/") + 1, builder.length());
        return name;
    }

    public static String getParentName(String path) {
        StringBuilder builder = new StringBuilder(path);
        StringBuilder parentPath = new StringBuilder(builder.substring(0, builder.length() - (getName(path).length() + 1)));
        String parentName = parentPath.substring(parentPath.lastIndexOf("/") + 1,
                                                 parentPath.length());
        return parentName;
    }
    /**
     * Check whether creation of new directory is inside the same directory with the same name or not
     * Directory inside the same directory with similar filename shall not be allowed
     * Doesn't work at an OTG path
     *
     * @param file
     * @return
     */
    public static boolean isNewDirectoryRecursive(File file) {
        return getName(file.getAbsolutePath()).equals(getParentName(file.getAbsolutePath()));
    }
}
