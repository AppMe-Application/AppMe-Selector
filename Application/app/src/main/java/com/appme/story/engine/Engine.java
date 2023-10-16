package com.appme.story.engine;

import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import static android.graphics.Typeface.BOLD_ITALIC;

import java.io.File;

import com.appme.story.engine.app.folders.FilePicker;
import com.appme.story.engine.widget.LoadToast;
import com.appme.story.engine.widget.Toasty;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class Engine {
    
    private Context mContext;
	private LoadToast mToast;
	private SharedPreferences mSharedPref;    
	
    private Engine(Context context){
		this.mContext = context;
		this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);	
		this.mToast = new LoadToast(context);
		this.mToast.setProgressColor(Color.RED);
		this.mToast.setBorderColor(Color.LTGRAY);
	}
	
	public static Engine with(Context context){
		return new Engine(context);
	}
	
	public File getWorkingDirectory() {
        return new File(mSharedPref.getString("currentPath", FilePicker.EXTERNAL_STORAGE));
    }

    public void setWorkingDirectory(String dir) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString("currentPath", dir);
        editor.apply();
    }
	
	public String getWorkingFile() {
        return mSharedPref.getString("currentFile", FilePicker.DEFAULT_FILE);
    }

    public void setWorkingFile(String dir) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString("currentFile", dir);
        editor.apply();
    }
	
	public void loadToast(String message){
		mToast.setText(message);
		mToast.setTranslationY(100);
		mToast.show();
	}
	
	public void hideToast(){
		mToast.hide();
	}
	
	public void showInfo(String message){
		Toasty.info(mContext, message, Toast.LENGTH_SHORT, true).show();	
	}
	
	public void showWarning(String message){
		Toasty.warning(mContext, message, Toast.LENGTH_SHORT, true).show();	
	}
	
	public void showError(String message){
		Toasty.error(mContext, message, Toast.LENGTH_SHORT, true).show();	
	}
	
	public void showToast(String message){
		Toasty.normal(mContext, message).show();
	}
	
	public void showToast(String message, int iconId){
		Drawable icon = mContext.getResources().getDrawable(iconId);
		Toasty.normal(mContext, message, icon).show();
	}
	
	public CharSequence getFormattedMessage() {
        final String prefix = "Formatted ";
        final String highlight = "bold italic";
        final String suffix = " text";
        SpannableStringBuilder ssb = new SpannableStringBuilder(prefix).append(highlight).append(suffix);
        int prefixLen = prefix.length();
        ssb.setSpan(new StyleSpan(BOLD_ITALIC),
					prefixLen, prefixLen + highlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }
}
