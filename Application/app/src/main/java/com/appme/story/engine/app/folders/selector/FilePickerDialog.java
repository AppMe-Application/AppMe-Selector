package com.appme.story.engine.app.folders.selector;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import com.appme.story.R;
import com.appme.story.engine.app.folders.selector.listeners.DialogSelectionListener;
import com.appme.story.engine.app.folders.selector.listeners.NotifyItemChecked;
import com.appme.story.engine.app.folders.selector.adapters.FilePickerAdapter;
import com.appme.story.engine.app.folders.selector.models.DialogConfigs;
import com.appme.story.engine.app.folders.selector.models.DialogProperties;
import com.appme.story.engine.app.folders.selector.models.FilePickerItem;
import com.appme.story.engine.app.folders.selector.models.MarkedItemList;
import com.appme.story.engine.app.folders.selector.utils.ExtensionFilter;
import com.appme.story.engine.app.folders.selector.utils.Utility;
import com.appme.story.engine.widget.MaterialCheckbox;
import com.appme.story.engine.widget.EmptyLayout;


public class FilePickerDialog extends Dialog implements AdapterView.OnItemClickListener {

    private Activity mActivity;
    private Context context;
    private ListView listView;
	private EmptyLayout mEmptyLayout; // this is required to show different layouts (loading or empty or error)
    private DialogProperties properties;
    private DialogSelectionListener callbacks;
    private ArrayList<FilePickerItem> internalList;
    private ExtensionFilter filter;
    private FilePickerAdapter mFileListAdapter;
    private Button select;
    private String positiveBtnNameStr = null;
    private String negativeBtnNameStr = null;
    private String currentDirName;
    private String currentDirPath;

    private View mMenuLayout;
    private View mCreateButtonLayout;
    private Button bt_create_folder;
    private ImageButton btn_menu;
    private ImageButton btn_create;
    private EditText mEditText;

    private LinearLayout lyt_expand_text;
    private ScrollView nested_content;
    private HorizontalScrollView mNavigation;
    private LinearLayout mView;
    private final int text_size = 16;
    private final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;
    private final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;    

    public static final int EXTERNAL_READ_PERMISSION_GRANT = 112;
	Integer shortAnimDuration;

    public FilePickerDialog(Context context) {
        super(context);
        this.context = context;
        properties = new DialogProperties();
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
    }

    public FilePickerDialog(Context context, DialogProperties properties) {
        super(context);
        this.context = context;
        this.properties = properties;
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
    }

    public FilePickerDialog(Context context, DialogProperties properties, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.properties = properties;
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main);
        mActivity = (Activity)context;
        mMenuLayout = findViewById(R.id.menu_button_layout);
        btn_menu = (ImageButton)findViewById(R.id.btn_menu);
		btn_menu.setImageResource(R.drawable.ic_arrow_left);
        btn_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        mCreateButtonLayout = findViewById(R.id.create_button_layout);
        btn_create = (ImageButton)findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toggleMenu(btn_create);
                }
            });

        mNavigation = (HorizontalScrollView)findViewById(R.id.pathScrollView);
        mView = (LinearLayout) findViewById(R.id.directory_buttons);

        mEditText = (EditText)findViewById(R.id.et_folder_name);
        mEditText.setHint("Folder Name");
        bt_create_folder = (Button)findViewById(R.id.create_folder_button);
        bt_create_folder.setEnabled(false);
        bt_create_folder.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {  
					if (!Utility.checkStorageAccessPermissions(context)) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_READ_PERMISSION_GRANT);
						}
					} else {
						final File folder = new File(currentDirPath + File.separator + mEditText.getText().toString().trim());
						File noMedia = new File(folder.getAbsolutePath(), ".nomedia");
						noMedia.getParentFile().mkdirs();
						if (!noMedia.exists()) {
							try {
								boolean success = noMedia.createNewFile();
								if (!success)
									mEditText.setText("");
							} catch (IOException io) {
								io.getMessage();
							}
						}

						mActivity.runOnUiThread(new Runnable(){
								@Override
								public void run() {
									onNavigate(folder.getAbsolutePath());
									Toast.makeText(context, "Create Folder Is Done", Toast.LENGTH_SHORT).show();          
								}
							});

						toggleMenu(btn_create);
					}
                }
            });


        lyt_expand_text = (LinearLayout)findViewById(R.id.lyt_expand_text);
        nested_content = (ScrollView)findViewById(R.id.nested_content);     
        collapse(lyt_expand_text);
        listView = (ListView) findViewById(R.id.fileList);
		// initialize the empty view
		mEmptyLayout = new EmptyLayout(context);
		mEmptyLayout.setListView(listView);
		mEmptyLayout.setErrorButtonClickListener(mErrorClickListener);
		
		
		mFileListAdapter = new FilePickerAdapter(internalList, context, properties);
		listView.setAdapter(mFileListAdapter);
		mFileListAdapter.setNotifyItemCheckedListener(new NotifyItemChecked() {
				@Override
				public void notifyCheckBoxIsClicked() {
					/*  Handler function, called when a checkbox is checked ie. a file is
					 *  selected.
					 */
					positiveBtnNameStr = positiveBtnNameStr == null ? context.getResources().getString(R.string.selector_choose_button_label) : positiveBtnNameStr;
					int size = MarkedItemList.getFileCount();
					if (size == 0) {
						select.setEnabled(false);
						int color;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
							color = context.getResources().getColor(R.color.colorAccent, context.getTheme());
						} else {
							color = context.getResources().getColor(R.color.colorAccent);
						}
						select.setTextColor(Color.argb(128, Color.red(color), Color.green(color), Color.blue(color)));
						select.setText(positiveBtnNameStr);
					} else {
						select.setEnabled(true);
						int color;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
							color = context.getResources().getColor(R.color.colorAccent, context.getTheme());
						} else {
							color = context.getResources().getColor(R.color.colorAccent);
						}
						select.setTextColor(color);
						String button_label = positiveBtnNameStr + " (" + size + ") ";
						select.setText(button_label);
					}
					if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
						/*  If a single file has to be selected, clear the previously checked
						 *  checkbox from the list.
						 */
						mFileListAdapter.notifyDataSetChanged();
					}
				}
			});

        select = (Button) findViewById(R.id.select);
        int size = MarkedItemList.getFileCount();
        if (size == 0) {
            select.setEnabled(false);
            int color;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                color = context.getResources().getColor(R.color.colorAccent, context.getTheme());
            } else {
                color = context.getResources().getColor(R.color.colorAccent);
            }
            select.setTextColor(Color.argb(128, Color.red(color), Color.green(color), Color.blue(color)));
        }

		shortAnimDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        Button cancel = (Button) findViewById(R.id.cancel);
        if (negativeBtnNameStr != null) {
            cancel.setText(negativeBtnNameStr);
        }
        select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*  Select Button is clicked. Get the array of all selected items
                     *  from MarkedItemList singleton.
                     */
                    String paths[] = MarkedItemList.getSelectedPaths();
                    //NullPointerException fixed in v1.0.2
                    if (callbacks != null) {
                        callbacks.onSelectedFilePaths(paths);
                    }
                    dismiss();
                }
            });
        cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    bt_create_folder.setEnabled(!s.toString().trim().isEmpty());               
                }
            });
    }

	@Override
	protected void onStart() {
		super.onStart();
		positiveBtnNameStr = (positiveBtnNameStr == null ? context.getResources().getString(R.string.selector_choose_button_label) : positiveBtnNameStr);			
		select.setText(positiveBtnNameStr);
		if (Utility.checkStorageAccessPermissions(context)) {
			File currLoc;
			internalList.clear();
			if (properties.offset.isDirectory() && validateOffsetPath()) {
				currLoc = new File(properties.offset.getAbsolutePath());
				FilePickerItem parent = new FilePickerItem();
				parent.setFilename(context.getString(R.string.selector_label_parent_dir));
				parent.setDirectory(true);
				parent.setLocation(currLoc.getParentFile().getAbsolutePath());
				parent.setTime(currLoc.lastModified());
				internalList.add(parent);
			} else if (properties.root.exists() && properties.root.isDirectory()) {
				currLoc = new File(properties.root.getAbsolutePath());
			} else {
				currLoc = new File(properties.error_dir.getAbsolutePath());
			}
			currentDirName = currLoc.getName();
			currentDirPath = currLoc.getAbsolutePath();
			setDirectoryButtons(currLoc.getAbsolutePath());
			setCreateButton(currLoc.getAbsolutePath());

			new AsyncTask<File, String, ArrayList<FilePickerItem>>() {
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showLoading();
				}

				@Override
				protected ArrayList<FilePickerItem> doInBackground(File...params) {
					return Utility.prepareFileListEntries(internalList, params[0], filter);
				}

				@Override
				protected void onPostExecute(ArrayList<FilePickerItem> result) {
					super.onPostExecute(result);
					if (result.size() < 2) {
						showEmpty();
					} else {	
					    internalList = result;
						mFileListAdapter.notifyDataSetChanged();
					}
				}
			}.execute(currLoc);      
			listView.setOnItemClickListener(this);
		}
	}

	private boolean validateOffsetPath() {
		String offset_path = properties.offset.getAbsolutePath();
		String root_path = properties.root.getAbsolutePath();
		return !offset_path.equals(root_path) && offset_path.contains(root_path);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		if (internalList.size() > i) {
			FilePickerItem fitem = internalList.get(i);
			if (fitem.isDirectory()) {
				if (new File(fitem.getLocation()).canRead()) {
					File currLoc = new File(fitem.getLocation());
					currentDirName = currLoc.getName();
					currentDirPath = currLoc.getAbsolutePath();
					setCreateButton(currLoc.getAbsolutePath());
					setDirectoryButtons(currLoc.getAbsolutePath());
					internalList.clear();
					if (!currLoc.getName().equals(properties.root.getName())) {
						FilePickerItem parent = new FilePickerItem();
						parent.setFilename(context.getString(R.string.selector_label_parent_dir));
						parent.setDirectory(true);
						parent.setLocation(currLoc.getParentFile().getAbsolutePath());
						parent.setTime(currLoc.lastModified());
						internalList.add(parent);
					}
					new AsyncTask<File, String, ArrayList<FilePickerItem>>() {
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							showLoading();
						}

						@Override
						protected ArrayList<FilePickerItem> doInBackground(File...params) {
							return Utility.prepareFileListEntries(internalList, params[0], filter);
						}

						@Override
						protected void onPostExecute(ArrayList<FilePickerItem> result) {
							super.onPostExecute(result);
							if (result.size() < 1) {
								showEmpty();
							} else {
								internalList = result;
								mFileListAdapter.notifyDataSetChanged();
							}
						}
					}.execute(currLoc);      
				} else {
					showError();
					Toast.makeText(context, R.string.selector_error_dir_access, Toast.LENGTH_SHORT).show();
				}
			} else {
				MaterialCheckbox fmark = (MaterialCheckbox) view.findViewById(R.id.file_mark);
				fmark.performClick();
			}
		}
	}

	private View.OnClickListener mErrorClickListener = new View.OnClickListener() {			
		@Override
		public void onClick(View v) {
			Toast.makeText(context, "Try again button clicked", Toast.LENGTH_LONG).show();			
		}
	};
	
	public DialogProperties getProperties() {
		return properties;
	}

	public void setProperties(DialogProperties properties) {
		this.properties = properties;
		filter = new ExtensionFilter(properties);
	}

	public void setDialogSelectionListener(DialogSelectionListener callbacks) {
		this.callbacks = callbacks;
	}

	@Override
	public void setTitle(CharSequence titleStr) {

	}

	public void setCreateButton(String path) {
		if (path.equals("/mnt") || path.equals("/mnt/obb") || path.equals("/mnt/user") || path.equals("/mnt/m_external_sd")) {
			setCreateButtonVisibility(View.GONE);
			if (mCreateButtonLayout.getVisibility() != View.VISIBLE) {
				collapse(lyt_expand_text); 
				btn_create.animate().setDuration(200).rotation(0);
			}
		} else {
			setCreateButtonVisibility(View.VISIBLE);
		}
	}

	public void setPositiveBtnName(CharSequence positiveBtnNameStr) {
		if (positiveBtnNameStr != null) {
			this.positiveBtnNameStr = positiveBtnNameStr.toString();
		} else {
			this.positiveBtnNameStr = null;
		}
	}

	public void setNegativeBtnName(CharSequence negativeBtnNameStr) {
		if (negativeBtnNameStr != null) {
			this.negativeBtnNameStr = negativeBtnNameStr.toString();
		} else {
			this.negativeBtnNameStr = null;
		}
	}

	// Triggered when "Empty" button is clicked
	public void showEmpty() {
		// clear the list and show the empty layout
		mEmptyLayout.showEmpty();
	}

	// Triggered when "Loading" button is clicked
	public void showLoading() {
		// clear the list and show the loading layout	
		mEmptyLayout.showLoading();
	}

	// Triggered when "Error" button is clicked
	public void showError() {
		// clear the list and show the error layout
		mEmptyLayout.showError();
	}

	// Triggered when "List" button is clicked
	public void onShowList() {
		// show the list
		//populateList();
	}
	
	public void markFiles(List<String> paths) {
		if (paths != null && paths.size() > 0) {
			if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
				File temp = new File(paths.get(0));
				switch (properties.selection_type) {
					case DialogConfigs.DIR_SELECT:
						if (temp.exists() && temp.isDirectory()) {
							FilePickerItem item = new FilePickerItem();
							item.setFilename(temp.getName());
							item.setDirectory(temp.isDirectory());
							item.setMarked(true);
							item.setTime(temp.lastModified());
							item.setLocation(temp.getAbsolutePath());
							MarkedItemList.addSelectedItem(item);
						}
						break;

					case DialogConfigs.FILE_SELECT:
						if (temp.exists() && temp.isFile()) {
							FilePickerItem item = new FilePickerItem();
							item.setFilename(temp.getName());
							item.setDirectory(temp.isDirectory());
							item.setMarked(true);
							item.setTime(temp.lastModified());
							item.setLocation(temp.getAbsolutePath());
							MarkedItemList.addSelectedItem(item);
						}
						break;

					case DialogConfigs.FILE_AND_DIR_SELECT:
						if (temp.exists()) {
							FilePickerItem item = new FilePickerItem();
							item.setFilename(temp.getName());
							item.setDirectory(temp.isDirectory());
							item.setMarked(true);
							item.setTime(temp.lastModified());
							item.setLocation(temp.getAbsolutePath());
							MarkedItemList.addSelectedItem(item);
						}
						break;
				}
			} else {
				for (String path : paths) {
					switch (properties.selection_type) {
						case DialogConfigs.DIR_SELECT:
							File temp = new File(path);
							if (temp.exists() && temp.isDirectory()) {
								FilePickerItem item = new FilePickerItem();
								item.setFilename(temp.getName());
								item.setDirectory(temp.isDirectory());
								item.setMarked(true);
								item.setTime(temp.lastModified());
								item.setLocation(temp.getAbsolutePath());
								MarkedItemList.addSelectedItem(item);
							}
							break;

						case DialogConfigs.FILE_SELECT:
							temp = new File(path);
							if (temp.exists() && temp.isFile()) {
								FilePickerItem item = new FilePickerItem();
								item.setFilename(temp.getName());
								item.setDirectory(temp.isDirectory());
								item.setMarked(true);
								item.setTime(temp.lastModified());
								item.setLocation(temp.getAbsolutePath());
								MarkedItemList.addSelectedItem(item);
							}
							break;

						case DialogConfigs.FILE_AND_DIR_SELECT:
							temp = new File(path);
							if (temp.exists() && (temp.isFile() || temp.isDirectory())) {
								FilePickerItem item = new FilePickerItem();
								item.setFilename(temp.getName());
								item.setDirectory(temp.isDirectory());
								item.setMarked(true);
								item.setTime(temp.lastModified());
								item.setLocation(temp.getAbsolutePath());
								MarkedItemList.addSelectedItem(item);
							}
							break;
					}
				}
			}
		}
	}



	public void onNavigate(String path) {
		File currentFolder = new File(path);
		if (currentFolder.isDirectory()) {
			if (new File(currentFolder.getAbsolutePath()).canRead()) {
				File currLoc = new File(currentFolder.getAbsolutePath());
				currentDirName = currLoc.getName();
				currentDirPath = currLoc.getAbsolutePath();
				setCreateButton(currLoc.getAbsolutePath());
				setDirectoryButtons(currLoc.getAbsolutePath());
				internalList.clear();
				if (!currLoc.getName().equals(properties.root.getName())) {
					FilePickerItem parent = new FilePickerItem();
					parent.setFilename(context.getString(R.string.selector_label_parent_dir));
					parent.setDirectory(true);
					parent.setLocation(currLoc.getParentFile().getAbsolutePath());
					parent.setTime(currLoc.lastModified());
					internalList.add(parent);
				}
				new AsyncTask<File, String, ArrayList<FilePickerItem>>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						showLoading();
					}

					@Override
					protected ArrayList<FilePickerItem> doInBackground(File...params) {
						return Utility.prepareFileListEntries(internalList, params[0], filter);
					}

					@Override
					protected void onPostExecute(ArrayList<FilePickerItem> result) {
						super.onPostExecute(result);
						if (result.size() < 1) {
							showEmpty();
						} else {
							internalList = result;
							mFileListAdapter.notifyDataSetChanged();
						}
					}
				}.execute(currLoc);      

			} else {
				showError();
				Toast.makeText(context, R.string.selector_error_dir_access, Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void show() {
		if (!Utility.checkStorageAccessPermissions(context)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_READ_PERMISSION_GRANT);
			}
		} else {
			super.show();
			positiveBtnNameStr = positiveBtnNameStr == null ?
				context.getResources().getString(R.string.selector_choose_button_label) : positiveBtnNameStr;
			select.setText(positiveBtnNameStr);
			int size = MarkedItemList.getFileCount();
			if (size == 0) {
				select.setText(positiveBtnNameStr);
			} else {
				String button_label = positiveBtnNameStr + " (" + size + ") ";
				select.setText(button_label);
			}
		}
	}

	@Override
	public void onBackPressed() {
        //currentDirName is dependent on dname
		if (internalList.size() > 0) {
			FilePickerItem fitem = internalList.get(0);
			File currLoc = new File(fitem.getLocation());
			if (currentDirName.equals(properties.root.getName())) {
				super.onBackPressed();
			} else {
				currentDirPath = currLoc.getAbsolutePath();
				setDirectoryButtons(currLoc.getAbsolutePath());
				internalList.clear();
				if (!currLoc.getName().equals(properties.root.getName())) {
					FilePickerItem parent = new FilePickerItem();
					parent.setFilename(context.getString(R.string.selector_label_parent_dir));
					parent.setDirectory(true);
					parent.setLocation(currLoc.getParentFile().getAbsolutePath());
					parent.setTime(currLoc.lastModified());
					internalList.add(parent);
				}
				new AsyncTask<File, String, ArrayList<FilePickerItem>>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						showLoading();
					}

					@Override
					protected ArrayList<FilePickerItem> doInBackground(File...params) {
						return Utility.prepareFileListEntries(internalList, params[0], filter);
					}

					@Override
					protected void onPostExecute(ArrayList<FilePickerItem> result) {
						super.onPostExecute(result);
						if (result.size() < 1) {
							showEmpty();
						} else {
							internalList = result;
							mFileListAdapter.notifyDataSetChanged();
						}
					}
				}.execute(currLoc); 
			}
			setCreateButton(currLoc.getAbsolutePath());
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void dismiss() {
		MarkedItemList.clearSelectionList();
		internalList.clear();

		super.dismiss();
	}

	public void setDirectoryButtons(String path) {
		File currentDirectory = new File(path);
		if (!Utility.checkStorageAccessPermissions(context)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_READ_PERMISSION_GRANT);
			}
		} else {
			String dir = "";
			//mEditText.setText(currentDirectory.getAbsolutePath());      
			mView.removeAllViews();

			String[] parts = currentDirectory.getAbsolutePath().split("/");
			// Add home view separately
			TextView t0 = new TextView(context, null, android.R.attr.actionButtonStyle);
			t0.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT,
															 Gravity.CENTER_VERTICAL));
			t0.setText("/");
			t0.setTextSize(text_size);
			t0.setTag(dir);
			t0.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						onNavigate("/");
					}
				});

			t0.setOnLongClickListener(new View.OnLongClickListener() {
					public boolean onLongClick(View view) {
						String dir1 = (String) view.getTag();
						savetoClipBoard(context, dir1);
						return true;
					}
				});

			mView.addView(t0);

			// Add other buttons
			for (int i = 1; i < parts.length; i++) {
				dir += "/" + parts[i];

				// add a LinearLayout as a divider
				FrameLayout fv1 = new FrameLayout(context);
				LayoutInflater il = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View divider = il.inflate(R.layout.item_navigation_divider, null);
				fv1.addView(divider);
				fv1.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER_VERTICAL));


				// add clickable TextView
				TextView t2 = new TextView(context, null, android.R.attr.actionButtonStyle);
				t2.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, Gravity.CENTER_VERTICAL));														 
				t2.setText(parts[i]);
				t2.setTextSize(text_size);
				t2.setTag(dir);
				t2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							String dir1 = (String) view.getTag();
							onNavigate(dir1);
						}
					});

				t2.setOnLongClickListener(new View.OnLongClickListener() {
						public boolean onLongClick(View view) {
							String dir1 = (String) view.getTag();
							savetoClipBoard(context, dir1);
							return true;
						}
					});

				mView.addView(fv1);
				mView.addView(t2);
				mNavigation.postDelayed(new Runnable() {
						@Override
						public void run() {
							mNavigation.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
						}
					}, 100L);
			}
		}
	}

	public void setMenuButtonVisibility(int visible) {
		mMenuLayout.setVisibility(visible);
	}

	public void setCreateButtonVisibility(int visible) {
		mCreateButtonLayout.setVisibility(visible);
	}

	public boolean toggleArrow(View view) {
		if (view.getRotation() == 0) {
			view.animate().setDuration(200).rotation(180);
			return true;
		} else {
			view.animate().setDuration(200).rotation(0);
			return false;
		}
	}

	public void toggleMenu(View view) {
		boolean show = toggleArrow(view);
		if (show) {
			expand(lyt_expand_text, new AnimListener(){
					@Override
					public void onFinish() {
						nestedScrollTo(nested_content, lyt_expand_text);
					}
				});
		} else {
			collapse(lyt_expand_text);
		}
	}

	public interface AnimListener {
		void onFinish();
    }


    public static void expand(final View v, final AnimListener animListener) {
        Animation a = expandAction(v);
        a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animListener.onFinish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        v.startAnimation(a);
    }

    private static Animation expandAction(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetedHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : (int) (targetedHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targetedHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        return a;
    }

    public static void nestedScrollTo(final ScrollView nested, final View targetView) {
        nested.post(new Runnable(){
                @Override
                public void run() {
                    nested.scrollTo(500, targetView.getBottom());
                }
            });
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

	private void crossFade(final View toHide, View toShow) {

        toShow.setAlpha(0f);
        toShow.setVisibility(View.VISIBLE);

        toShow.animate()
            .alpha(1f)
            .setDuration(shortAnimDuration)
            .setListener(null);

        toHide.animate()
            .alpha(0f)
            .setDuration(shortAnimDuration)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toHide.setVisibility(View.GONE);
                }
            });
    }
    // save current string in ClipBoard
    public static void savetoClipBoard(final Context co, String dir1) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) co.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", dir1);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(co, "'" + dir1 + "' " + co.getString(R.string.action_copy_to_clipboard), Toast.LENGTH_SHORT).show();
    }



    public static boolean isLollipopAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
