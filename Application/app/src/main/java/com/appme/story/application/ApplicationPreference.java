package com.appme.story.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import com.appme.story.R;
import com.appme.story.engine.Engine;
import com.appme.story.engine.app.folders.FilePickerPreference;
import com.appme.story.engine.app.folders.FilePicker;
import com.appme.story.engine.app.folders.selector.models.DialogProperties;
import com.appme.story.engine.app.folders.selector.models.DialogConfigs;


public class ApplicationPreference extends PreferenceActivity implements Preference.OnPreferenceChangeListener {  

    public static void start(Context c) {
        Intent intent = new Intent(c, ApplicationPreference.class);
        c.startActivity(intent);
    }

	private SharedPreferences mSharedPref;
	private Engine mEngine;
	
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {   setTheme(R.style.AppTheme_Application);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
		mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);	
		mEngine = Engine.with(this);
		
        FilePickerPreference fileDialog = (FilePickerPreference)findPreference("directories");
		//Create a DialogProperties object.
        final DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        //Setting Parent Directory to Default SDCARD.
        properties.root = mEngine.getWorkingDirectory();
        properties.error_dir = new File("/mnt");
		
		String summary = mSharedPref.getString("currentPath", FilePicker.EXTERNAL_STORAGE);
		fileDialog.setTitle("Default Dir");
		fileDialog.setSummary(summary);
		fileDialog.setProperties(properties);
        fileDialog.setOnPreferenceChangeListener(this);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) { 
		    LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
            root.addView(toolbar, 0);
        } else {  
		    ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);
            root.removeAllViews();
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {   height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {   height = toolbar.getHeight();
            }
            content.setPadding(0, height, 0, 0);
            root.addView(content);
            root.addView(toolbar);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Settings");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals("directories")) {   String value=(String)o;
            String arr[] = value.split(":");
            for (String path:arr) {
                preference.setSummary(path);
				SharedPreferences.Editor editor = mSharedPref.edit();
				editor.putString("currentPath", path);
				editor.apply();
                Toast.makeText(ApplicationPreference.this, path, Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }
}
