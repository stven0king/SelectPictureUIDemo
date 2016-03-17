package com.tzx.selectpicturedemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tzx.selectpicturedemo.R;
import com.tzx.selectpicturedemo.model.LocalDirImage;

/**
 * Created by Administrator
 * Date: 2016/3/16.
 */
public class PictureDirLLItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private LocalDirImage mData;
    private int currentItem;
    public PictureDirLLItemAdapter(Context mContext, LocalDirImage mData) {
        this.mContext = mContext;
        this.mData = mData;
        currentItem = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.picture_dir_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewholder = null;
        if (holder instanceof MyViewHolder) {
            myViewholder = (MyViewHolder) holder;
        }
        if (myViewholder == null) {
            return;
        }
        if (currentItem == position) {
            myViewholder.checked.setVisibility(View.VISIBLE);
        } else {
            myViewholder.checked.setVisibility(View.GONE);
        }
        String str = mData.getAllDir().get(position);
        myViewholder.image.setImageURI(Uri.parse(SelectPictureGridAdapter.FILE_PREFIX + mData.getCurrentDirFirstMap(str).getPath()));
        myViewholder.num.setText("" + mData.getCurrentDirCount(str) + "张");
        int index = str.lastIndexOf("/");
        if (index > 0) {
            str = str.substring(index + 1, str.length());
        }
        myViewholder.name.setText(str);
    }

    @Override
    public int getItemCount() {
        return mData.getAllDir().size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView name;
        TextView num;
        ImageView checked;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.select_dir_img);
            name = (TextView) itemView.findViewById(R.id.select_dir_name);
            num = (TextView) itemView.findViewById(R.id.dir_pic_num);
            checked = (ImageView) itemView.findViewById(R.id.current_identifer);
        }
    }
}