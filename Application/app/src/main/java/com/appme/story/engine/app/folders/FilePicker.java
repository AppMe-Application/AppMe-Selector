package com.appme.story.engine.app.folders;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

import com.appme.story.engine.app.folders.selector.FilePickerDialog;
import com.appme.story.engine.app.folders.selector.listeners.DialogSelectionListener;
import com.appme.story.engine.app.folders.selector.models.DialogProperties;
import com.appme.story.engine.app.folders.selector.models.DialogConfigs;

public class FilePicker {
    
    public static final String TAG = "FilePicker";
    private AppCompatActivity mActivity;
    public static String DEFAULT_DIR = DialogConfigs.DEFAULT_DIR;
    public static String EXTERNAL_STORAGE = DialogConfigs.EXTERNAL_STORAGE;
    public static final String APK = ".apk", MP4 = ".mp4", MP3 = ".mp3", JPG = ".jpg", JPEG = ".jpeg", PNG = ".png", DOC = ".doc", DOCX = ".docx", XLS = ".xls", XLSX = ".xlsx", PDF = ".pdf";
	public static String DEFAULT_FILE = APK;
    
	
    private FilePicker(AppCompatActivity activity) {
        this.mActivity = activity; 
    }

    public static FilePicker with(AppCompatActivity activity) {
        return new FilePicker(activity);
    }
    
    public void getFolderSelector(final String directory, final OnFilePickerListeners mOnFilePickerListeners){
        mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					getSelector(directory, DialogConfigs.DIR_SELECT, mOnFilePickerListeners);
				}
			});           
    } 
    
    public void getFileSelector(final String directory, final OnFilePickerListeners mOnFilePickerListeners){
        mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					getSelector(directory, DialogConfigs.FILE_SELECT, mOnFilePickerListeners);
				}
			});             
    } 
    
    public void getMultiFileSelector(final String directory, final OnFilePickerListeners mOnFilePickerListeners){
		mActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				getSelector(directory, DialogConfigs.FILE_AND_DIR_SELECT, mOnFilePickerListeners);
			}
		});      
    }      
    
    private void getSelector(String dir, int typeSelect, final OnFilePickerListeners mOnFilePickerListeners){
        //Create a DialogProperties object.
        final DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = typeSelect;
        //Setting Parent Directory to Default SDCARD.
        properties.root = new File(dir);
        properties.error_dir = new File("/mnt");

        //Instantiate FilePickerDialog with Context and DialogProperties.
        FilePickerDialog dialog = new FilePickerDialog(mActivity, properties);
        dialog.setTitle("Select a File");
        dialog.setPositiveBtnName("Select");
        dialog.setNegativeBtnName("Cancel");
        //Set new properties of dialog.
        dialog.setProperties(properties);
        //Method handle selected files.
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(String[] files) {
                    mOnFilePickerListeners.onSelectedFilePaths(files);
                }
            });
        //Showing dialog when Show Dialog button is clicked.
        dialog.show();
    } 
    
    public interface OnFilePickerListeners{
        void onSelectedFilePaths(String[] files);
    }
    
}
