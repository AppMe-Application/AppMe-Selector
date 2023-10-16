package com.appme.story.application;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.appme.story.R;
import com.appme.story.engine.app.fragments.ApplicationFragment;
import com.appme.story.engine.app.folders.preview.IconPreview;


public class ApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Application);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        switchFragment(ApplicationFragment.newInstance("Selector"));

        // start IconPreview class to get thumbnails if BrowserListAdapter
        // request them
        new IconPreview(this);
    }

    @Override
    public void onTrimMemory(int level) {
        IconPreview.clearCache();
    }   

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();
    }
}
/*don't forget to subscribe my YouTube channel for more Tutorial and mod*/
/*
https://youtube.com/channel/UC_lCMHEhEOFYgJL6fg1ZzQA */
