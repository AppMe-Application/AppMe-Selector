package com.appme.story.engine.app.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


import com.appme.story.R;
import com.appme.story.settings.theme.ThemePreference;
import com.appme.story.application.ApplicationPreference;
import com.appme.story.engine.Engine;
import com.appme.story.engine.app.folders.FilePicker;
import com.appme.story.engine.app.folders.HeaderFolder;
import com.appme.story.engine.app.models.ActionItem;

public class ApplicationFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";
    private AppCompatActivity mActivity;
    private Context mContext;
	private Engine mEngine;
    private static final int ID_FILE_APK = 1;
    private static final int ID_FILE_IMAGE = 2;
    private static final int ID_FILE_MUSIC = 3;
    private static final int ID_FILE_VIDEO = 4;


    private HeaderFolder mHeaderFolder;
    private TextView mFileList;
    public String mCurrentFolder;
    public String mCurrentFile;

    private File currentDir = null;
	private String currentFile = null;
	private Handler mHandler = new Handler();
    private Runnable mRunner = new Runnable(){
        @Override
        public void run() {
			currentDir = mEngine.getWorkingDirectory();
			currentFile = mEngine.getWorkingFile();
			navigateTo(currentDir.getAbsolutePath());   
        }
    };
	
    public static ApplicationFragment newInstance(String text) {
        ApplicationFragment fragment = new ApplicationFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_application, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final String text = getArguments().getString(EXTRA_TEXT);
        mActivity = (AppCompatActivity)getActivity();
        mContext = getActivity();
        mEngine = Engine.with(mContext);
		currentDir = mEngine.getWorkingDirectory();
		currentFile = mEngine.getWorkingFile();
		
        mHeaderFolder = (HeaderFolder)view.findViewById(R.id.header);
        mFileList = (TextView)view.findViewById(R.id.file_list);
         
        mHeaderFolder.setMenuButton(R.drawable.ic_arrow_down);
        ActionItem fileApkItem      = new ActionItem(ID_FILE_APK, "Apk", getResources().getDrawable(R.drawable.ic_file_apk));
        ActionItem fileImageItem   = new ActionItem(ID_FILE_IMAGE, "Image", getResources().getDrawable(R.drawable.ic_file_image));
        ActionItem fileMp3Item   = new ActionItem(ID_FILE_MUSIC, "Mp3", getResources().getDrawable(R.drawable.ic_file_music));
        ActionItem fileVideoItem   = new ActionItem(ID_FILE_VIDEO, "Mp4", getResources().getDrawable(R.drawable.ic_file_video));
        mHeaderFolder.addActionItem(fileApkItem);
        mHeaderFolder.addActionItem(fileImageItem);
        mHeaderFolder.addActionItem(fileMp3Item);
        mHeaderFolder.addActionItem(fileVideoItem);
        mHeaderFolder.setOnActionItemClickListener(new HeaderFolder.OnActionItemClickListener() {
                @Override
                public void onItemClick(HeaderFolder quickAction, int pos, int actionId) {
                    ActionItem actionItem = quickAction.getActionItem(pos);
                    if (actionId == ID_FILE_APK) {  
                        mCurrentFile = FilePicker.APK;      
                        showMessage("File Apk");
                    } else if (actionId == ID_FILE_IMAGE) {
                        mCurrentFile = FilePicker.PNG;      
                        showMessage("File Image");
                    } else if (actionId == ID_FILE_MUSIC) {
                        mCurrentFile = FilePicker.MP3;      
                        showMessage("File Audio");
                    } else if (actionId == ID_FILE_VIDEO) {
                        mCurrentFile = FilePicker.MP4;            
                        showMessage("File Video");
                    } else {
                        showMessage(actionItem.getTitle() + " selected");
                    }                         
                    mEngine.setWorkingFile(mCurrentFile); 
					mHandler.postDelayed(mRunner, 1200);   
                }
            });
        mHeaderFolder.setFolderButton(R.drawable.ic_folder_settings);
        mHeaderFolder.setOnFolderButtonClickListener(new HeaderFolder.OnFolderButtonListener(){
                @Override
                public void onClick(View view) {
					showMessage("Open Picker");
                    FilePicker.with(mActivity).getFolderSelector(FilePicker.EXTERNAL_STORAGE, new FilePicker.OnFilePickerListeners(){
                            @Override
                            public void onSelectedFilePaths(String[] items) {
                                for (int i = 0; i < items.length; i++) {
                                    switchToPath(items[i]);
                                }                       
                            }
                        });
                }
            });
        mHeaderFolder.setOnNavigationListener(new HeaderFolder.OnNavigateListener(){
                @Override
                public void onNavigate(String path) {
                    switchToPath(path);
                }
            });  
		
        navigateTo(currentDir.getAbsolutePath());
    }

	@Override
	public void onResume() {
		super.onResume();
		currentDir = mEngine.getWorkingDirectory();
		currentFile = mEngine.getWorkingFile();
        navigateTo(currentDir.getAbsolutePath());
	}
	
    

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_application, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
				FilePicker.with(mActivity).getFileSelector(currentDir.getAbsolutePath(), new FilePicker.OnFilePickerListeners(){
						@Override
						public void onSelectedFilePaths(String[] items) {
							for (int i = 0; i < items.length; i++) {
								mFileList.setText(items[i]);
							}                       
						}
					});
                return true;
            case R.id.action_settings:
                ApplicationPreference.start(mContext);
                return true;    
            case R.id.action_exit:
				mEngine.showWarning("Anda Akan Keluar Dari Aplikasi");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchToPath(String dir) {
        mEngine.setWorkingDirectory(dir);
        navigateTo(dir);
    }

    public void navigateTo(String dir) {
        mHeaderFolder.setDirectoryButtons(dir);  
		StringBuilder sb = new StringBuilder();
		sb.append("Type File : ").append(" ").append(currentFile);
		sb.append("\n");
		sb.append(dir);
        mFileList.setText(sb.toString());
    }

	public void showMessage(String msg){
		mEngine.showToast(msg);
	}
}


