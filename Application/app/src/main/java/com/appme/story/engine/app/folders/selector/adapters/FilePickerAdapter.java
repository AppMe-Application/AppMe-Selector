package com.appme.story.engine.app.folders.selector.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appme.story.R;
import com.appme.story.engine.app.folders.preview.IconPreview;
import com.appme.story.engine.app.folders.selector.models.DialogConfigs;
import com.appme.story.engine.app.folders.selector.models.DialogProperties;
import com.appme.story.engine.app.folders.selector.models.FilePickerItem;
import com.appme.story.engine.app.folders.selector.models.MarkedItemList;
import com.appme.story.engine.app.folders.selector.listeners.NotifyItemChecked;
import com.appme.story.engine.app.listeners.OnCheckedChangeListener;
import com.appme.story.engine.widget.MaterialCheckbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.io.File;

/* <p>
 * Created by Angad Singh on 09-07-2016.
 * </p>
 */

/**
 * Adapter Class that extends {@link BaseAdapter} that is
 * used to populate {@link ListView} with file info.
 */
public class FilePickerAdapter extends BaseAdapter {
    private ArrayList<FilePickerItem> listItem;
    private Context context;
    private DialogProperties properties;
    private NotifyItemChecked notifyItemChecked;

    public FilePickerAdapter(ArrayList<FilePickerItem> listItem, Context context, DialogProperties properties) {
        this.listItem = listItem;
        this.context = context;
        this.properties = properties;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public FilePickerItem getItem(int i) {
        return listItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_file_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {   
		    holder = (ViewHolder)view.getTag();
        }
        final FilePickerItem item = listItem.get(i);
        if (MarkedItemList.hasItem(item.getLocation())) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.marked_item_animation);
            view.setAnimation(animation);
        } else {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.unmarked_item_animation);
            view.setAnimation(animation);
        }

        // get icon
        IconPreview.getFileIcon(new File(item.getLocation()), holder.type_icon);
        if (item.isDirectory()) {
			/* holder.type_icon.setImageResource(R.mipmap.ic_type_folder);
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			 holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorPrimary,context.getTheme()));
			 }
			 else
			 {   holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
			 }*/
            if (properties.selection_type == DialogConfigs.FILE_SELECT) {   
				holder.fmark.setVisibility(View.INVISIBLE);
            } else {   
			    holder.fmark.setVisibility(View.VISIBLE);
            }
        } else {
            /*holder.type_icon.setImageResource(R.mipmap.ic_type_file);
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			 holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorAccent,context.getTheme()));
			 }
			 else
			 {   holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorAccent));
			 }*/
            if (properties.selection_type == DialogConfigs.DIR_SELECT) { 
				holder.fmark.setVisibility(View.INVISIBLE);
            } else {   
			    holder.fmark.setVisibility(View.VISIBLE);
            }
        }
        holder.type_icon.setContentDescription(item.getFilename());
        holder.name.setText(item.getFilename());
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat stime = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        Date date = new Date(item.getTime());
        if (i == 0 && item.getFilename().startsWith(context.getString(R.string.selector_label_parent_dir))) {
            holder.type.setText(R.string.selector_label_parent_directory);
        } else {
            holder.type.setText(context.getString(R.string.selector_last_edit) + sdate.format(date) + ", " + stime.format(date));
        }
        if (holder.fmark.getVisibility() == View.VISIBLE) {
            if (i == 0 && item.getFilename().startsWith(context.getString(R.string.selector_label_parent_dir))) {  
				holder.fmark.setVisibility(View.INVISIBLE);
            }
            if (MarkedItemList.hasItem(item.getLocation())) {
                holder.fmark.setChecked(true);
            } else {
                holder.fmark.setChecked(false);
            }
        }

        holder.fmark.setOnCheckedChangedListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(MaterialCheckbox checkbox, boolean isChecked) {
					item.setMarked(isChecked);
					if (item.isMarked()) {
						if (properties.selection_mode == DialogConfigs.MULTI_MODE) {
							MarkedItemList.addSelectedItem(item);
						} else {
							MarkedItemList.addSingleFile(item);
						}
					} else {
						MarkedItemList.removeSelectedItem(item.getLocation());
					}
					notifyItemChecked.notifyCheckBoxIsClicked();
				}
			});
        return view;
    }

    private class ViewHolder {   ImageView type_icon;
        TextView name,type;
        MaterialCheckbox fmark;

        ViewHolder(View itemView) {
            name = (TextView)itemView.findViewById(R.id.fname);
            type = (TextView)itemView.findViewById(R.id.ftype);
            type_icon = (ImageView)itemView.findViewById(R.id.image_type);
            fmark = (MaterialCheckbox) itemView.findViewById(R.id.file_mark);
        }
    }

    public void setNotifyItemCheckedListener(NotifyItemChecked notifyItemChecked) {
        this.notifyItemChecked = notifyItemChecked;
    }
}
