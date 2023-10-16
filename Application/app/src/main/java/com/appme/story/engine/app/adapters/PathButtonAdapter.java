package com.appme.story.engine.app.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.appme.story.R;
import com.appme.story.engine.app.listeners.OnItemClickListener;
import com.appme.story.engine.app.listeners.OnItemLongClickListener;


public class PathButtonAdapter extends RecyclerView.Adapter<PathButtonAdapter.ViewHolder> {
    private ArrayList<File> pathList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public File getItem(int position) {
        return pathList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_button_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final File path = pathList.get(position);
        String name = path.getName();
        if ("/".equals(name) || TextUtils.isEmpty(name)) {
            name = holder.textView.getContext().getString(R.string.selector_root_path);
        }
        holder.textView.setText(name);
        holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "";

                    if (path.getPath().isEmpty() || path.getPath().equals("/")) {
                        message = "Warning..!" + "\n" + "Folder Ini Tidak Bisa DiBuka.Coba Folder Lain";   
                    } else if (path.getPath().isEmpty() || path.getPath().equals("/storage")) {
                        message = "Warning..!" + "\n" + "Folder Ini Tidak Bisa DiBuka.Coba Folder Lain";  
                    } else if (path.getPath().isEmpty() || path.getPath().equals("/storage/emulated")) {
                        message = "Warning..!" + "\n" + "Folder Ini Tidak Bisa DiBuka.Coba Folder Lain";
                    } else if (path.getPath().isEmpty() || path.getPath().equals("/storage/emulated/0")) {
                        message = path.getPath();
                    } else if (path.getPath().isEmpty() || path.getPath().equals("/storage/extSdCard")) {
                        message = "Warning..!" + "\n" + "Folder Ini Tidak Bisa DiBuka.Coba Folder Lain";
                    } else if (path.getPath().isEmpty() || path.getPath().equals("/storage/sdcard0")) {
                        message = "Warning..!" + "\n" + "Folder Ini Tidak Bisa DiBuka.Coba Folder Lain";
                    }  else {
                        message = path.getAbsolutePath();
                        if (onItemClickListener != null)
                            onItemClickListener.onItemClick(position, v);
                    }

                    Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();                           
                }
            });
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(position, view);
                    return true;
                }
            });
    }

    @Override
    public int getItemCount() {
        return pathList == null ? 0 : pathList.size();
    }

    public void setPath(File path) {
        if (pathList == null)
            pathList = new ArrayList<>();
        else
            pathList.clear();

        for (;path != null;) {
            pathList.add(path);
            path = path.getParentFile();
        }

        Collections.reverse(pathList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView;
        }
    }
}
