package com.appme.story.engine.app.folders.selector;

import android.os.AsyncTask;
import java.util.ArrayList;

import com.appme.story.engine.app.folders.selector.models.FilePickerItem;

public class FilePickerTask extends AsyncTask<String, String, ArrayList<FilePickerItem>> {

	private ArrayList<FilePickerItem> internalList;
    
	public FilePickerTask(ArrayList<FilePickerItem> internalList) {
		this.internalList = internalList;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected ArrayList<FilePickerItem> doInBackground(String[] p1) {
		
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<FilePickerItem> result) {
		super.onPostExecute(result);
	}
    
}
